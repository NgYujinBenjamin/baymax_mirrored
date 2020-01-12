import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import { SET_BASELINE, SET_STATUS_BASELINE, SET_LINEARIZE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_LINEARIZE } from '../types';
import XLSX from 'xlsx';
const { ipcRenderer } = window.require("electron")

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        linearize: [],
        bays: '',
        histories: [],
        history: {},
        loading: false,
        hasBaseline: false,
        postResult: []
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    const getResult = (objs, bay) => {
        const preResult = { bay: bay, response: [...objs] }
        // const preResult = [{bay: bay}, ...objs];
        ipcRenderer.send('getResult:send', JSON.stringify(preResult));
    }

    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    const updateLinearize = (data) => dispatch({ type: UPDATE_LINEARIZE, payload: data })

    const setLinearize = (file) => {
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

    const setStatusBaseline = () => dispatch({ type: SET_STATUS_BASELINE })

    const setLoading = () => dispatch({ type: SET_LOADING })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            linearize: state.linearize,
            bays: state.bays,
            histories: state.histories,
            history: state.history,
            loading: state.loading,
            hasBaseline: state.hasBaseline,
            setBaseline,
            setStatusBaseline,
            setLinearize,
            setBays,
            updateLinearize,
            getResult,
            clearPreresult,
            setLoading
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
