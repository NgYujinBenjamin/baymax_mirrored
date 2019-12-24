import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import { SET_BASELINE, SET_STATUS_BASELINE } from '../types';
import XLSX from 'xlsx';

const UploadState = (props) => {
    const initialState = {
        baseline: [],
        linearize: [],
        histories: [],
        history: {},
        loading: false,
        hasBaseline: false
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
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

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            linearize: state.linearize,
            histories: state.histories,
            history: state.history,
            loading: state.loading,
            hasBaseline: state.hasBaseline,
            setBaseline,
            setStatusBaseline
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
