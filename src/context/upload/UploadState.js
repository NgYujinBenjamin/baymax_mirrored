import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import { SET_BASELINE, SET_LINEARIZE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_LINEARIZE, CREATE_RESULT } from '../types';
import XLSX from 'xlsx';
const { ipcRenderer } = window.require("electron")

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
    const createResult = (objs, bay) => {
        setLoading();

        const preResult = { bay: bay, data: [...objs] }
        // const preResult = [{bay: bay}, ...objs];
        ipcRenderer.send('getResult:send', JSON.stringify(preResult));
        ipcRenderer.once('getResult:received', (event, res) => {
            const response = JSON.parse(res);
            
            if(response.type === 'SUCCESS'){
                // console.log(response.data)
                dispatch({
                    type: CREATE_RESULT,
                    payload: response.data
                })
            }
        })
    }

    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    const updateLinearize = (data) => dispatch({ type: UPDATE_LINEARIZE, payload: data })

    const setLinearize = (file) => {
        setLoading();

        let reader = new FileReader();
        reader.readAsArrayBuffer(file);
        reader.onload = (e) => {
            let data = new Uint8Array(e.target.result);
            let workbook = XLSX.read(data, { type: 'array', cellDates: true });
            let firstSheetName = workbook.SheetNames[0];
            let worksheet = workbook.Sheets[firstSheetName];
            let excelData = XLSX.utils.sheet_to_json(worksheet);

            excelData.forEach((obj) => {
                obj['MRP Date'] = obj['MRP Date'] === undefined ? '' : obj['MRP Date'].toLocaleDateString('en-GB');
                obj['Created On'] = obj['Created On'] === undefined ? '' : obj['Created On'].toLocaleDateString('en-GB');
                obj['Created Time'] = obj['Created Time'] === undefined ? '' : obj['Created Time'].toLocaleString('en-GB', { timeZone: 'UTC' });
                obj['SAP Customer Req Date'] = obj['SAP Customer Req Date'] === undefined ? '' : obj['SAP Customer Req Date'].toLocaleDateString('en-GB');
                obj['Ship Recog Date'] = obj['Ship Recog Date'] === undefined ? '' : obj['Ship Recog Date'].toLocaleDateString('en-GB');
                obj['Slot Request Date'] = obj['Slot Request Date'] === undefined ? '' : obj['Slot Request Date'].toLocaleDateString('en-GB');
            });

            excelData[excelData.length - 1]['Argo ID'] === undefined && excelData.pop();
            
            dispatch({
                type: SET_LINEARIZE,
                payload: excelData
            });
        }
    }

    const setBaseline = (file) => {
        let reader = new FileReader();
        reader.readAsArrayBuffer(file);
        reader.onload = (e) => {
            let data = new Uint8Array(e.target.result);
            let workbook = XLSX.read(data, { type: 'array' });
            let firstSheetName = workbook.SheetNames[0];
            let worksheet = workbook.Sheets[firstSheetName];
            let excelData = XLSX.utils.sheet_to_json(worksheet);
            
            dispatch({
                type: SET_BASELINE,
                payload: excelData
            });
        }
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
            setLoading
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
