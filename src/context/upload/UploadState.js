import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import axios from 'axios';
import { SET_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR } from '../types';
// const { ipcRenderer } = window.require("electron")

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        schedule: null,
        bays: '',
        loading: false,
        postResult: null,
        scheduleDone: false,
        error: null
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    //export file
    const createExport = (file) => {
        setLoading();

        const allProductResult = file.allProduct;

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWsOne = XLSX.utils.json_to_sheet(allProductResult);

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

        const output = file.allProduct;

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            await axios.post('http://localhost:8080/save', output, config)
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

        objs.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })

        let preResult = null;
        // baseline !== null ? preResult = { bay: bay, data: [...objs], baseline: baseline } : preResult = { bay: bay, data: [...objs] }
        preResult =  [...objs];
        // console.log(preResult)

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/algo', preResult, config);
            console.log(res.data)

            //change schedule formatting
            const output = [];
            res.data.schedule.forEach(arr => {
                arr.schedule.forEach(val => {
                    output.push(val);
                })
            })
            res.data.schedule = output;

            // change date formatting - allProduct
            res.data.allProduct.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')                 
            })

            // change date formatting - schedule
            res.data.schedule.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')                  
            })

            // change date formatting - unfulfilled
            res.data.unfulfilled.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')  
            })

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
        //     let response = JSON.parse(res);
        //     console.log(response)
        //     const output = [];
        //     response.data.schedule = [];
        //     response.data.schedule.forEach(arr => {
        //         arr.schedule.forEach(val => {
        //             output.push(val);
        //         })
        //     })
        //     response.data.schedule = output;

            // console.log(new Date(response.data.allProduct[0].MRPDate).toLocaleDateString('en-GB'))
            // console.log(typeof Date.parse(response.data.allProduct[0].MRPDate))
            
            // console.log(response);

            //change date formatting - allProduct
            // response.data.allProduct.forEach(val => {
            //     val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
            //     val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
            //     val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
            //     val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
            //     val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')
            // })

            //change date formatting - schedule
            // response.data.schedule.forEach(val => {
            //     val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
            //     val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
            //     val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
            //     val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
            //     val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')   
            // })

            //change date formatting - unfulfilled
            // response.data.unfulfilled.forEach(val => {
            //     val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
            //     val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
            //     val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
            //     val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
            //     val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')   
            // })

            // if(response.type === 'SUCCESS'){
            //     // console.log(response.data)
            //     dispatch({
            //         type: CREATE_RESULT,
            //         payload: response.data
            //     })
            // }
        // })
    }

    //import masterops
    const setSchedule = async (file) => {
        setLoading();

        let data = await convertExcelToJSON(file);
        let scheduleCounter = false;

        //if excelfile is not masterops data/excel file
        const firstTenData = data.slice(0,10);
        firstTenData.forEach(val => {
            if(!('Argo ID' in val) && !('Slot ID/UTID' in val) && !('Build Product' in val) && !('Cycle Time Days' in val) && !('MRP Date' in val)){
                scheduleCounter = true
            }
        })

        if(scheduleCounter){
            dispatch({
                type: UPLOAD_ERROR,
                payload: 'Please upload a proper Masterops excel file'
            })
        } else {
            //else (masterops data/excel file)
            let filtered = data.filter(obj => obj['Plan Product Type'] === 'Tool');
            filtered.forEach(obj => {
                obj['MRP Date'] = obj['MRP Date'] === undefined ? '' : obj['MRP Date'].toLocaleDateString('en-GB');
                obj['Created On'] = obj['Created On'] === undefined ? '' : obj['Created On'].toLocaleDateString('en-GB');
                obj['Created Time'] = obj['Created Time'] === undefined ? '' : obj['Created Time'].toLocaleDateString('en-GB');
                obj['SAP Customer Req Date'] = obj['SAP Customer Req Date'] === undefined ? '' : obj['SAP Customer Req Date'].toLocaleDateString('en-GB');
                obj['Ship Recog Date'] = obj['Ship Recog Date'] === undefined ? '' : obj['Ship Recog Date'].toLocaleDateString('en-GB');
                obj['Slot Request Date'] = obj['Slot Request Date'] === undefined ? '' : obj['Slot Request Date'].toLocaleDateString('en-GB');
                obj['Int. Ops Ship Readiness Date'] = obj['Int. Ops Ship Readiness Date'] === undefined ? '' : obj['Int. Ops Ship Readiness Date'].toLocaleDateString('en-GB');
                obj['MFG Commit Date'] = obj['MFG Commit Date'] === undefined ? '' : obj['MFG Commit Date'].toLocaleDateString('en-GB');
                obj['Div Commit Date'] = obj['Div Commit Date'] === undefined ? '' : obj['Div Commit Date'].toLocaleDateString('en-GB');
                obj['Changed On'] = obj['Changed On'] === undefined ? '' : obj['Changed On'].toLocaleDateString('en-GB');
                obj['Last Changed Time'] = obj['Last Changed Time'] === undefined ? '' : obj['Last Changed Time'].toLocaleDateString('en-GB');

            });

            filtered[filtered.length - 1]['Argo ID'] === undefined && filtered.pop();

            console.log(JSON.stringify(filtered))

            dispatch({
                type: SET_SCHEDULE,
                payload: filtered
            });
        }
    }

    //update masterops
    const updateSchedule = (objs) => {
        //convert cycle time days to integer
        objs.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })
        dispatch({ type: UPDATE_SCHEDULE, payload: objs })
    }

    //set bays
    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    //clear preresult
    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    //clear upload errors
    const uploadClearError = () => dispatch({ type: UPLOAD_CLEAR_ERROR })

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
            error: state.error,
            setBaseline,
            setSchedule,
            setBays,
            updateSchedule,
            createResult,
            clearPreresult,
            setLoading,
            createExport,
            saveFile,
            clearAll,
            uploadClearError
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
