import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import axios from 'axios';
import { SET_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, CLEAR_ALL, SAVE_RESULT } from '../types';
// const { ipcRenderer } = window.require("electron")

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        schedule: null,
        bays: '',
        loading: false,
        postResult: null,
        scheduleDone: false
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    //export file
    const createExport = (file) => {
        setLoading();

        const scheduleResult = file.schedule;

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWsOne = XLSX.utils.json_to_sheet(scheduleResult);

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

    //save file
    const saveFile = async (file) => {
        setLoading();

        const output = file.schedule;

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            await axios.post('http://localhost:8080/<PATH>', output, config)
            dispatch({
                type: SAVE_RESULT
            })
        } catch (err) {
            //prompt error

        }
    }

    //clear all - back to default state
    const clearAll = () => dispatch({ type: CLEAR_ALL })

    //create result - mass slot upload
    const createResult = async (objs, bay, baseline) => {
        setLoading();
        let preResult = null;
        baseline !== null ? preResult = { bay: bay, data: [...objs], baseline: baseline } : preResult = { bay: bay, data: [...objs] }

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/<PATH>', preResult, config);
            console.log(res)
            const output = [];
            res.data.schedule.forEach(arr => {
                arr.schedule.forEach(val => {
                    output.push(val);
                })
            })
            res.data.schedule = output;

            dispatch({
                type: CREATE_RESULT,
                payload: res.data
            })
        } catch (err) {
            //prompt error

        }

        // /* ELECTRON CODE */
        // ipcRenderer.send('getResult:send', JSON.stringify(preResult));
        // ipcRenderer.once('getResult:received', (event, res) => {
        //     const response = JSON.parse(res);
        //     const output = [];

        //     // response.data.schedule = [];
        //     response.data.schedule.forEach(arr => {
        //         arr.schedule.forEach(val => {
        //             output.push(val);
        //         })
        //     })

        //     response.data.schedule = output;
            
        //     console.log(response);

        //     if(response.type === 'SUCCESS'){
        //         // console.log(response.data)
        //         dispatch({
        //             type: CREATE_RESULT,
        //             payload: response.data
        //         })
        //     }
        // })
    }

    //import masterops
    const setSchedule = async (file) => {
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
            type: SET_SCHEDULE,
            payload: filtered
        });
    }

    //update masterops
    const updateSchedule = (data) => dispatch({ type: UPDATE_SCHEDULE, payload: data })

    //set bays
    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    //clear preresult
    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    //set baseline
    const setBaseline = async (file) => {
        const data = await convertExcelToJSON(file);
        console.log(data);
        dispatch({
            type: SET_BASELINE,
            payload: data
        })
    }

    //convert excel to json
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

    //set loading
    const setLoading = () => dispatch({ type: SET_LOADING })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            schedule: state.schedule,
            bays: state.bays,
            loading: state.loading,
            scheduleDone: state.scheduleDone,
            postResult: state.postResult,
            setBaseline,
            setSchedule,
            setBays,
            updateSchedule,
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
