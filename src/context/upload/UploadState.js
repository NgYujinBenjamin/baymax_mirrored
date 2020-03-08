import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import axios from 'axios';
import { SET_BASELINE, UPDATE_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, EXPORT_SCHEDULE, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO, SET_STEPS, UPDATE_POST_RESULT, UPDATE_QUARTER, UPDATE_DATA, UPDATE_SAVE, UPDATE_POST_RESULT_FORMAT, UPDATE_RESCHEDULE, RESCHEDULE_POST_RESULT, UPDATE_TABCHECKER,CREATE_RESULT_ERROR, SET_MIN_GAP, SET_MAX_GAP } from '../types';

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        newBaseline: null,
        schedule: null,
        bays: '',
        minGap: '',
        maxGap: '',
        loading: false,
        postResult: null,
        scheduleDone: false,
        error: null,
        postResultDone: null,
        stepcount: 0,
        steps: ['Upload Bay Requirement', 'Input guidelines & upload MasterOpsPlan', 'Edit MasterOpsPlan', 'Schedule Generated'],
        currentQuarter: null,
        postResultErrors: {},
        saveHistory: false,
        reschedule: false,
        tabUpdate: false
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    //export bay_requirement_schedule
    const createExportSchedule = file => {
        setLoading();
        let headerDates = new Set()
        let quarterIdArr = {}

        for(const property in file.bayOccupancy){
          quarterIdArr[property] = {}
          file.bayOccupancy[property][0].forEach(val => {
            headerDates.add(val)
          })
        }

        //no duplicates, array of dates
        let headerDatesArr = Array.from(headerDates)

        //sorts the dates from smallest to biggest
        headerDatesArr.sort((a, b) => new Date(a) - new Date(b));

        const dateoptions = { year: 'numeric', month: 'long', day: 'numeric' };
        const formatedHeaderDates = headerDatesArr.map(val => val = new Date(val).toLocaleDateString('en-GB', dateoptions))

        //form the headers for the excel
        const excelHeader = [ ['Slot/UTID', 'Fab Name', 'Build Product', 'Configuration', 'Tool Start', 'MRP Date', 'Mfg Commit Date', 'Gap','Cycle Time Day', 'Actual Cycle Time', ...formatedHeaderDates] ]

        //form each row => how to display the 'e','o','e','o', now theres different dates
        //form the 'e','o', for entire quarter
        let bayIndex = 1;
        for(const property in file.bayOccupancy){

          file.bayOccupancy[property].slice(1).forEach(arr => {
            let name = `quarter${bayIndex}`
            headerDatesArr.forEach(dte => arr[0][dte] = '')

            quarterIdArr[property][name] = {}

            file.bayOccupancy[property][0].forEach(val => {
              quarterIdArr[property][name][val] = ''
            })

            arr.slice(1).forEach((val,idx) => {
              let key = Object.keys(quarterIdArr[property][name])[idx]
              quarterIdArr[property][name][key] = val
            })

            bayIndex++

          })
        }

        //put in each quarter -> within each quarter { date: value }
        //now the date is inside each quarter
        //value => E, O, E, O, follow each quarter 
        //insert the value accordingly to each quarter
        //CY19Q4 -> CY20Q1 -> CY20Q2 -> CY20Q3 -> CY20Q4
        let tempindex = 1;
        for(const property in file.bayOccupancy){
          // console.log(property)
          file.bayOccupancy[property].slice(1).forEach(arr => {
            // console.log(arr[0])
            let name = `quarter${tempindex}`
            // console.log(quarterIdArr[property][])
            for(const inProp in quarterIdArr[property][name]){
              // console.log(`${inProp} : ${quarterIdArr[property][name][inProp]}`)
              if(arr[0].hasOwnProperty(inProp)){
                arr[0][inProp] = quarterIdArr[property][name][inProp]
              }
            }
            tempindex++
          })
        }

        // now all the e, o are in the right place
        // left the empty spaces -> put all 'O' into empty space
        for(const property in file.bayOccupancy){
          file.bayOccupancy[property].slice(1).forEach(arr => {
            // console.log(arr[0])
            Object.entries(arr[0]).forEach(( [key, value] ) => {
              if(arr[0][key] === ''){
                arr[0][key] = 'E'
              }
            })
          })
        }

        // console.log(file.bayOccupancy)
        //form array of objects
        let excelFormat = [];

        //delete all the unnecessary keys inside file bayoccupancy
        for(const property in file.bayOccupancy){
          file.bayOccupancy[property].slice(1).forEach(arr => {

            checkDeleteProperty(arr[0],[
              'argoID',
              'plant',
              'buildComplete',
              'slotStatus',
              'planProductType',
              'buildCategory',
              'shipRevenueType',
              'salesOrder',
              'forecastID',
              'productPN',
              'committedShip$',
              'intOpsShipReadinessDate',
              'shipRecogDate',
              'quantity',
              'RMATool',
              'new_Used',
              'fabID',
              'buildQtr',
              'endDate',
              'leaveBayDate'
            ])
            
            //add into the format XLSX needs
            excelFormat.push({
              'slotID_UTID': arr[0].slotID_UTID,
              'fabName': arr[0].fabName,
              'buildProduct': arr[0].buildProduct,
              'configuration': '',
              'toolStartDate': arr[0].toolStartDate,
              'MRPDate': arr[0].MRPDate,
              'MFGCommitDate': arr[0].MFGCommitDate,
              'gapDays': arr[0].gapDays,
              'cycleTimeDays': arr[0].cycleTimeDays,
              'actualCycleTime': Math.round((new Date(arr[0].MFGCommitDate) - new Date(arr[0].toolStartDate)) / (1000 * 60 * 60 * 24)),
              ...arr[0]
            })

          })
        }

        //format date
        excelFormat.forEach(val => {
          changeDateFormat(val, ['toolStartDate','MRPDate','MFGCommitDate'])

        })

        //count the number of O
        let bayAllCount = {};
        bayAllCount = headerDatesArr.reduce((ac, ci) => (ac[ci] = 0, ac), {})
        excelFormat.forEach(val => {
          Object.entries(val).forEach(( [key, value] ) => {
            if(value === 'O'){
              if( !(bayAllCount.hasOwnProperty(key)) ){
                bayAllCount[key] = 1
              } else {
                bayAllCount[key] += 1
              }
            }
          })
        })
        let excelCount = []
        excelCount.push(bayAllCount);

        //putting it into excel
        const wb = XLSX.utils.book_new();

        const ws = XLSX.utils.json_to_sheet(excelFormat, {
          skipHeader: true,
          origin: 1
        });

        XLSX.utils.sheet_add_aoa(ws, excelHeader, {
          origin: 0
        })

        XLSX.utils.sheet_add_aoa(ws, [['No. of bays required']], {
          origin: { r: excelFormat.length+2, c: 9 }
        })

        XLSX.utils.sheet_add_json(ws, excelCount, {
          skipHeader: true,
          origin: { r: excelFormat.length+2, c: 10 }
        })

        XLSX.utils.book_append_sheet(wb, ws, 'Shipment Plan');

        XLSX.writeFile(wb, `Bay_Requirement_${new Date().toLocaleDateString('en-GB')}.xlsx`);

        dispatch({
          type: EXPORT_SCHEDULE
        });
    }

    //export file - mass_slot_upload
    const createExport = file => {
        // setLoading();
        const output = []

        for(const property in file.bayOccupancy){
          file.bayOccupancy[property].slice(1).forEach(arr => {
            output.push({
              ...arr[0]
            })
          })
        }

        output.forEach(val => {
          checkDeleteProperty(val, ['committedShip$','buildQtr','endDate','leaveBayDate','gapDays'])
          changeDateFormat(val, ['MRPDate','intOpsShipReadinessDate','MFGCommitDate','shipRecogDate','toolStartDate'])
        })

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWS = XLSX.utils.json_to_sheet(output, {
          skipHeader: true,
          origin: 1
        });

        const massHeader = [ [
          'Argo ID',
          'Plant',
          'Build Complete',
          'Slot Status',
          'Plan Product Type',
          'Build Category',
          'Ship Revenue Type',
          'Sales Order',
          'Forecast ID',
          'Slot ID/UTID',
          'Fab Name',
          'Build Product',
          'Product PN',
          'MRP Date',
          'Int. Ops Ship Readiness Date',
          'MFG Commit Date',
          'Ship Recog Date',
          'Cycle Time Days',
          'Quantity',
          'RMA Tool',
          'New Used',
          'Fab ID',
          'Tool Start Date'
        ] ]

        XLSX.utils.sheet_add_aoa(massWS, massHeader, {
          origin: 0
        })

        //parse in a worksheet into the workbook
        //1st arg: workbook, 2nd arg: worksheet, 3rd: name of worksheet
        XLSX.utils.book_append_sheet(massWB, massWS, 'SAP Document Export');

        //write workbook to file
        //1st arg: workbook, 2nd arg: name of file
        XLSX.writeFile(massWB, 'Mass_Slot_Upload.xlsx');

        dispatch({
            type: EXPORT_RESULT
        })
    }

    //submethod - check and delete property
    //1st arg: obj, 2nd arg: array of property names inside obj (string)
    const checkDeleteProperty = (obj, arr) => {
      arr.forEach(val => obj.hasOwnProperty(val) && delete obj[val])
    }

    //submethod - change date format
    //1st arg: obj, 2nd arg: array of property names inside obj (string)
    const changeDateFormat = (obj, arr) => {
      arr.forEach(val => obj[val] = new Date(obj[val]).toLocaleDateString('en-GB'))
    }

    //clear all - back to default state
    const clearAll = () => dispatch({ type: CLEAR_ALL })

    //update current quarter
    const updateCurrentQuarter = (result) => dispatch({ type: UPDATE_QUARTER, payload: result })

    //update data
    const updateCurrentData = (result) => {
      dispatch({ 
        type: UPDATE_DATA, 
        payload: result 
      })
    }

    // ##################################################################################################
    // ######################################## FIELD VALIDATIONS #######################################
    // ##################################################################################################

    const validateDate = (value) => {
      const dateParts = value.split("/");
      // const date = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]);
      if ( (!isNaN(dateParts[0]) && dateParts[0].length == 2) && (!isNaN(dateParts[1]) && dateParts[1].length == 2) && (!isNaN(dateParts[2]) && dateParts[2].length == 4) ) {
      // if ((date.getDate() == dateParts[0] && dateParts[0].length == 2) && (date.getMonth() == (dateParts[1] - 1) && dateParts[1].length == 2) && (date.getFullYear() == dateParts[2]) && dateParts[2].length == 4) {
          return null;
      }
      return 'Invalid Date (dd/mm/yyyy)';
    }

    const validateNum = (value) => {
        let errorMsg = isNaN(value) ? 'Invalid number' : null;

        return errorMsg;
    }

    // ##################################################################################################
    // ######################################## POST RESULT START #######################################
    // ##################################################################################################

    // send to backend for rescheduling (Have not implemented this!)
    const reschedulePostResult = async (postResultDone, bays, mingap, maxgap) => {
      postResultDone.numBays = parseInt(bays);
      postResultDone.minGap = parseInt(mingap);
      postResultDone.maxGap = parseInt(maxgap);

      console.log(postResultDone);
      
      const config = {
        headers: {
          'Content-Type': 'application/json'
        }
      }
      
      try {
        const res = await axios.post('http://localhost:8080/subseqScheduling', postResultDone, config);

        dispatch({ 
          type: RESCHEDULE_POST_RESULT, 
          payload: res.data 
        })

      } catch (err) {
        dispatch({
          type: CREATE_RESULT_ERROR,
          payload: err.response.data.message
        })
      }
    }

    // get all quarters
    const getQtrs = (postResult) => {
      const qtrs = new Array();
      Object.keys(postResult.bayOccupancy).map(quarterName => 
          qtrs.push(quarterName)
      )

      return qtrs;
    }

    // first quarter unique dates
    const getUniqueDates = (postResult) => {
      const qtrs = getQtrs(postResult);
      const firstQtr = qtrs[0];
      const SfirstQtrDates = postResult.bayOccupancy[firstQtr][0];
      const BfirstQtrDates = postResult.baseLineOccupancy[firstQtr][0];

      let allDates = SfirstQtrDates.concat(BfirstQtrDates);
      allDates = Array.from(new Set(allDates));

      return allDates;
    }

    // update post result data "E"s
    const updatePostResultEmpties = (postResult, minGapDays) => {
      let postResultUpdate = JSON.parse(JSON.stringify(postResult));

      const qtrs = getQtrs(postResultUpdate);
      const firstQtrDates = getUniqueDates(postResultUpdate);

      // update baseline 
      let result = postResultUpdate.baseLineOccupancy[qtrs[0]];
      for( let i = 1; i < result.length; i++){
        let EOcount = result[i].slice(1).length;
        for (let j = EOcount; j < firstQtrDates.length; j++){
          result[i].push("E");
        }
      }
      result[0] = firstQtrDates;
      postResultUpdate.baseLineOccupancy[qtrs[0]] = result;

      // update scheduled
      result = postResultUpdate.bayOccupancy[qtrs[0]];
      for( let i = 1; i < result.length; i++){
        let EOcount = result[i].slice(1).length;
        for (let j = EOcount; j < firstQtrDates.length; j++){
          result[i].splice(1, 0, "E");
          result[i].join();
        }
      }
      
      result[0] = firstQtrDates;
      postResultUpdate.bayOccupancy[qtrs[0]] = result;

      setPostResult(postResultUpdate, minGapDays);
    }

    //update post result data when user click "Save"
    const updatePostResult = (postResultDone, objs, quarter) => {
      objs = JSON.parse(objs);
      const dates = postResultDone.bayOccupancy[quarter][0];

      objs.unshift(dates);
      postResultDone.bayOccupancy[quarter] = objs;

      dispatch({ 
        type: UPDATE_POST_RESULT,
        payload: postResultDone 
      })
    }

    const dateConversion = (dateString) => {
      let output = dateString.split("/");
      return new Date(output[2], output[1]-1, output[0]); 
    }

    const endDateCheck = (qtrObj, key, minGap) => {
      let intRedDate = dateConversion(qtrObj.intOpsShipReadinessDate);
      let MFGCommit = dateConversion(qtrObj.MFGCommitDate);
      {qtrObj.fabID == "OPEN" ? 
      qtrObj[key] = new Date(intRedDate.setTime(intRedDate.getTime() - minGap)).toLocaleDateString('en-GB') : qtrObj[key] = new Date(MFGCommit.setTime(MFGCommit.getTime() - minGap)).toLocaleDateString('en-GB');
      }
    }

    //set post result dates
    const setPostResult = (postResultDone, minGapDays) => {
      const minGap = (24*60*60*1000) * minGapDays;
      const objItemsToChange = ["sendToStorageDate", "MRPDate", "intOpsShipReadinessDate", "MFGCommitDate", "shipRecogDate", "toolStartDate", 'endDate'];

      Object.keys(postResultDone).map( occupancy => {
        Object.keys(postResultDone[occupancy]).map( quarter => {
          
          let currentQtr = postResultDone[occupancy][quarter];
          for(let i=0; i<currentQtr[0].length; i++){
            
            // sort the dates in ascending order for table header to output
            currentQtr[0].sort(function(a,b){
              return new Date(a) - new Date(b);
            });
            
            const month = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];
            let eachDate = new Date(currentQtr[0][i]);
            currentQtr[0][i] = eachDate.getDate() + " " + month[eachDate.getMonth()] + " " + eachDate.getFullYear();
          }

          for(let i=1; i<currentQtr.length; i++){
            objItemsToChange.forEach(key => {
              if(key !== "endDate"){
                if(currentQtr[i][0][key] !== null){
                  currentQtr[i][0][key] = new Date(currentQtr[i][0][key]).toLocaleDateString('en-GB');
                } else {
                  currentQtr[i][0][key] = "";
                }
              } else{
                endDateCheck(currentQtr[i][0], key, minGap);
              }

            })
          }

        })
      })

      dispatch({ 
        type: UPDATE_POST_RESULT_FORMAT,
        payload: postResultDone 
      })

    }

    const handlePostResultError = (postResultErrors, uniqueID, errorMsg, type) => {
      if(errorMsg == null && uniqueID in postResultErrors){
        delete postResultErrors[uniqueID];
      } 

      if(errorMsg !== null && !(uniqueID in postResultErrors)){
        postResultErrors[uniqueID] = errorMsg;
      }

      // no need to dispatch since it's for instant check
    }
    
    const updateReschedule = (res) => dispatch({ type: UPDATE_RESCHEDULE, payload: res })
    const tabChecker = (res) => dispatch({ type: UPDATE_TABCHECKER, payload: res })

    //save to history
    const saveResult = async (postResultDone) => {
      setLoading();

      const config = {
          headers: {
              'Content-Type': 'application/json'
          }
      }

      try {
          await axios.post('http://localhost:8080/save', postResultDone, config);

          dispatch({
              type: SAVE_RESULT
          })
      } catch (err) {
          //prompt error

      }
    }

    // ##################################################################################################
    // ######################################### POST RESULT END ########################################
    // ##################################################################################################

    //create result
    const createResult = async (newbaseline, masterops, bays, mingap, maxgap) => {
        setLoading();

        masterops.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })

        let preResult = { baseline: newbaseline,  masterOps: masterops, bay: bays, minGap: mingap, maxGap: maxgap}

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/firstScheduling', preResult, config);

            dispatch({
                type: CREATE_RESULT,
                payload: res.data
            })
        } catch (err) {
            dispatch({
              type: CREATE_RESULT_ERROR,
              payload: err.response.data.message
            })
        }
    }

    //import masterops
    const setSchedule = async (file, minGap, baseFile) => {
        setLoading();

        let data = await convertExcelToJSON(file);
        let scheduleCounter = false;

        data[data.length - 1]['Argo ID'] === undefined && data.pop();

        //if excelfile is not masterops data/excel file
        data.forEach(val => {
          if( !(val.hasOwnProperty('Argo ID')) && !(val.hasOwnProperty('Slot ID/UTID')) && !(val.hasOwnProperty('Build Product')) && !(val.hasOwnProperty('Cycle Time Days')) && !(val.hasOwnProperty('MRP Date')) && !(val.hasOwnProperty('MFG Commit Date')) && !(val.hasOwnProperty('Int. Ops Ship Readiness Date'))){
            scheduleCounter = true
          }
        })

        if(scheduleCounter){
            dispatch({
                type: UPLOAD_ERROR,
                payload: 'Please upload a proper Masterops excel file'
            })
        } else {
            //filter by Tool
            let filtered_one = data.filter(obj => obj['Plan Product Type'] === 'Tool');

            //remove all the slotid/utid thats is the same as baseline inside masterops && put in inside baseline instead
            let filtered_two = filterAndInsertToBaseline(filtered_one, baseFile)

            //check of previously shipped products (today's date & mfg commit date)
            let filtered = filtered_two.filter(obj => obj['MFG Commit Date'] >= new Date());

            //check date in right format & setup for end date
            filtered.forEach(obj => {
              checkDatesValue(obj, ['MRP Date','Created On','Created Time','SAP Customer Req Date','Ship Recog Date','Slot Request Date','Int. Ops Ship Readiness Date','MFG Commit Date','Div Commit Date','Changed On','Last Changed Time'])

              obj['Lock MRP Date'] = false

              let output = obj['Slot Status'] === 'OPEN' ? obj['Int. Ops Ship Readiness Date'] : obj['MFG Commit Date']
              let outDates = output.split('/')
              let outYear = parseInt(outDates[2])
              let outMonth = parseInt(outDates[1]) - 1
              let outDay = parseInt(outDates[0])
              let outcurrentDate = new Date(outYear, outMonth, outDay)
              outcurrentDate.setDate(outcurrentDate.getDate() - minGap)
              
              obj['End Date'] = obj['Lock MRP Date'] === false ? outcurrentDate.toLocaleDateString() : obj['MRP Date']
            })

            dispatch({
                type: SET_SCHEDULE,
                payload: filtered
            });
        }
    }

    //get baseline
    const getBaseline = async () => {
      setLoading()

      try {
        // const res = await axios.get('http://localhost:8080/getBaseline')
        dispatch({
          type: SET_BASELINE,
          payload: []
        })
      } catch (err) {
        
      }
    }

    //update baseline
    const updateBaseline = base => {
      base.forEach(obj => {
        obj.hasOwnProperty('emptyToDelete') && delete obj['emptyToDelete'];
        
        checkDatesValue(obj, ['MRP Date','Created On','Created Time','SAP Customer Req Date','Ship Recog Date','Slot Request Date','Int. Ops Ship Readiness Date','MFG Commit Date','Div Commit Date','Changed On','Last Changed Time'])
      })
      dispatch({
        type: UPDATE_BASELINE,
        payload: base
      })
    }

    //filter then insert/dispatch to newbaseline
    const filterAndInsertToBaseline = (filtered, base) => {
      let tempBase = [];
      
      filtered.forEach(obj => {
        base.forEach(val => {
          if(val['Slot ID/UTID'] === obj['Slot ID/UTID']){
            tempBase.push(obj)
          }
        })
      })

      if(tempBase.length !== 0){
        tempBase.forEach(val => {
          filtered.forEach(obj => {
            if(val === obj){
              obj['emptyToDelete'] = ''
            }
          })
        })

        dispatch({
          type: UPDATE_BASELINE,
          payload: tempBase
        })

        return filtered.filter(obj => !(obj.hasOwnProperty('emptyToDelete')))
      } else {
        return filtered
      }
    }

    //check date value if undefined
    const checkDatesValue = (obj, arr) => {
      arr.forEach(val => obj[val] = obj[val] === undefined ? '' : obj[val].toLocaleDateString('en-GB'))
    }

    //update masterops
    const updateSchedule = (objs) => {
        //convert cycle time days to integer
        objs.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })
        dispatch({ type: UPDATE_SCHEDULE, payload: objs })
    }

    //set step counter
    const setStepCount = (num) => dispatch({ type: SET_STEPS, payload: num })

    //update save
    const updateSave = (res) => dispatch({ type: UPDATE_SAVE, payload: res })

    //set bays
    const setBays = (bayNum) => dispatch({ type: SET_BAYS, payload: bayNum })

    //set min gap
    const setMinGap = (num) => dispatch({ type: SET_MIN_GAP, payload: num });

    //set max gap
    const setMaxGap = (num) => dispatch({ type: SET_MAX_GAP, payload: num })

    //clear preresult
    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    //clear upload errors
    const uploadClearError = () => dispatch({ type: UPLOAD_CLEAR_ERROR })

    //set baseline
    const setBaseline = async (file) => {
        setLoading();

        const data = await convertExcelToJSON(file);

        let baselineChecker = false;
        data.forEach(obj => {
          if(Object.keys(obj).length !== 1 || !(obj.hasOwnProperty('Slot ID/UTID'))){
            baselineChecker = true;
          }
        })

        if(baselineChecker){
          dispatch({
            type: UPLOAD_ERROR,
            payload: 'Please upload a proper bay requirement file'
          })
        } else {
          dispatch({
            type: SET_BASELINE,
            payload: data
          })
        }
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

    //back to default - for logout purpose
    const clearZero = () => dispatch({ type: CLEAR_ZERO })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            newBaseline: state.newBaseline,
            schedule: state.schedule,
            minGap: state.minGap,
            maxGap: state.maxGap,
            bays: state.bays,
            loading: state.loading,
            scheduleDone: state.scheduleDone,
            postResult: state.postResult,
            error: state.error,
            postResultDone: state.postResultDone,
            stepcount : state.stepcount,
            steps: state.steps,
            currentQuarter: state.currentQuarter,
            postResultErrors: state.postResultErrors,
            reschedule: state.reschedule,
            saveHistory: state.saveHistory,
            tabUpdate: state.tabUpdate,
            setBaseline,
            setSchedule,
            setBays,
            updateSchedule,
            createResult,
            createExportSchedule,
            clearPreresult,
            setLoading,
            createExport,
            saveResult,
            clearAll,
            uploadClearError,
            clearZero,
            setStepCount,
            updatePostResultEmpties,
            updatePostResult,
            updateCurrentQuarter,
            updateCurrentData,
            updateSave,
            getBaseline,
            updateBaseline,
            setPostResult,
            endDateCheck,
            handlePostResultError,
            reschedulePostResult,
            updateReschedule,
            tabChecker,
            validateDate,
            validateNum,
            setMinGap,
            setMaxGap
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
