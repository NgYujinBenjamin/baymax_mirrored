import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import { SET_BASELINE, SET_LINEARIZE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_LINEARIZE, CREATE_RESULT, EXPORT_RESULT, CLEAR_ALL } from '../types';
// const { ipcRenderer } = window.require("electron")

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        linearize: null,
        bays: '',
        loading: false,
        postResult: null,
        linearizeDone: false
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    const createExport = (file) => {
        setLoading();

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWsOne = XLSX.utils.json_to_sheet(file);

        //parse in a worksheet into the workbook
        //1st arg: workbook, 2nd arg: worksheet, 3rd: name of worksheet
        XLSX.utils.book_append_sheet(massWB, massWsOne, 'SAP Document Export');

        //write workbook to file
        //1st arg: workbook, 2nd arg: name of file
        XLSX.writeFile(massWB, 'Mass_Slot_Upload.xlsx');

        dispatch({
            type: EXPORT_RESULT
        })
    }

    const saveFile = (file) => {
        console.log();
    }

    const clearAll = () => dispatch({ type: CLEAR_ALL })

    const createResult = (objs, bay) => {
        setLoading();

        const preResult = { bay: bay, data: [...objs] }
        // const preResult = [{bay: bay}, ...objs];
        // ipcRenderer.send('getResult:send', JSON.stringify(preResult));
        // ipcRenderer.once('getResult:received', (event, res) => {
        //     const response = JSON.parse(res);
            
        //     if(response.type === 'SUCCESS'){
        //         // console.log(response.data)
        //         dispatch({
        //             type: CREATE_RESULT,
        //             payload: response.data
        //         })
        //     }
        // })
    }

    const setLinearize = async (file) => {
        setLoading();

        let data = await convertExcelToJSON(file);
        let filtered = data.filter(obj => obj['Plan Product Type'] === 'Tool');
        filtered.forEach(obj => {
            obj['MRP Date'] = obj['MRP Date'] === undefined ? '' : obj['MRP Date'].toLocaleDateString('en-GB');
            obj['Created On'] = obj['Created On'] === undefined ? '' : obj['Created On'].toLocaleDateString('en-GB');
            obj['Created Time'] = obj['Created Time'] === undefined ? '' : obj['Created Time'].toLocaleString('en-GB', { timeZone: 'UTC' });
            obj['SAP Customer Req Date'] = obj['SAP Customer Req Date'] === undefined ? '' : obj['SAP Customer Req Date'].toLocaleDateString('en-GB');
            obj['Ship Recog Date'] = obj['Ship Recog Date'] === undefined ? '' : obj['Ship Recog Date'].toLocaleDateString('en-GB');
            obj['Slot Request Date'] = obj['Slot Request Date'] === undefined ? '' : obj['Slot Request Date'].toLocaleDateString('en-GB');
        });

        filtered[filtered.length - 1]['Argo ID'] === undefined && filtered.pop();

        dispatch({
            type: SET_LINEARIZE,
            payload: filtered
        });
    }

    const updateLinearize = (data) => dispatch({ type: UPDATE_LINEARIZE, payload: data })

    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    const setBaseline = async (file) => {
        const data = await convertExcelToJSON(file);
        console.log(data);
        dispatch({
            type: SET_BASELINE,
            payload: data
        })
    }

    const convertExcelToJSON = async (file) => {
        return new Promise((resolve, reject) => {
            let reader = new FileReader();
            reader.onload = (e) => {
                let data = new Uint8Array(e.target.result);
                let workbook = XLSX.read(data, { type: 'array', cellDates: true });
                let firstSheetName = workbook.SheetNames[0];
                let worksheet = workbook.Sheets[firstSheetName];
                let excelData = XLSX.utils.sheet_to_json(worksheet);
                resolve(excelData);
            };
            reader.readAsArrayBuffer(file);
        });
    }

    const setLoading = () => dispatch({ type: SET_LOADING })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            linearize: state.linearize,
            bays: state.bays,
            loading: state.loading,
            linearizeDone: state.linearizeDone,
            postResult: state.postResult,
            setBaseline,
            setLinearize,
            setBays,
            updateLinearize,
            createResult,
            clearPreresult,
            setLoading,
            createExport,
            saveFile,
            clearAll
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
