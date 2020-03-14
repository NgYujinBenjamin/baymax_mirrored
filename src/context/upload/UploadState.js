import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import axios from 'axios';
import { SET_BASELINE, UPDATE_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, EXPORT_SCHEDULE, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO, SET_STEPS, UPDATE_POST_RESULT, UPDATE_QUARTER, UPDATE_DATA, UPDATE_SAVE, UPDATE_POST_RESULT_FORMAT, UPDATE_RESCHEDULE, RESCHEDULE_POST_RESULT, UPDATE_TABCHECKER,CREATE_RESULT_ERROR, SET_MIN_GAP, SET_MAX_GAP, GET_HISTORY, LOAD_ALL_HISTORY, UPDATE_NEW_MIN_GAP } from '../types';

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        newBaseline: [],
        schedule: null,
        bays: '',
        minGap: '',
        maxGap: '',
        newMinGap: '',
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
        tabUpdate: false,
        histID: null,
        allHistory: []
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    // METHODS

    // @loc     PostResult.js
    // @desc    export of Bay_Requirement excel file
    // @param   (object)
    const createExportSchedule = file => {
        setLoading();

        let headerDates = new Set();
        let quarterIdArr = {};
        let baselineIdArr = {};

        //form of quarters in baselineIdArr
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            baselineIdArr[property] = {};
            file.baseLineOccupancy[property][0].forEach(val => {
              headerDates.add(val);
            })
          }
        }
        
        //form of quarters in quarterIdArr
        for(const property in file.bayOccupancy){
          quarterIdArr[property] = {};
          file.bayOccupancy[property][0].forEach(val => {
            headerDates.add(val);
          })
        }

        //form no duplicates, array of dates
        let headerDatesArr = Array.from(headerDates);

        //sorts the dates from smallest to biggest
        headerDatesArr.sort((a, b) => new Date(a) - new Date(b));

        //format the dates according to dd/mm/yyyy
        const dateoptions = { year: 'numeric', month: 'long', day: 'numeric' };
        const formatedHeaderDates = headerDatesArr.map(val => val = new Date(val).toLocaleDateString('en-GB', dateoptions))

        //form the headers for the excel
        const excelHeader = [ ['Slot/UTID', 'Fab Name', 'Build Product', 'Configuration', 'Tool Start', 'MRP Date', 'Mfg Commit Date', 'Gap','Cycle Time Day', 'Actual Cycle Time', ...formatedHeaderDates] ];

        //put dates -> 'E', 'O' inside baselineIdArr
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            let baselineIndex = 1;
  
            file.baseLineOccupancy[property].slice(1).forEach(arr => {
              let name = `quarter${baselineIndex}`;

              headerDatesArr.forEach(dte => arr[0][dte] = '')

              baselineIdArr[property][name] = {};

              file.baseLineOccupancy[property][0].forEach(val => {
                baselineIdArr[property][name][val] = '';
              })

              arr.slice(1).forEach((val,idx) => {
                let key = Object.keys(baselineIdArr[property][name])[idx];
                baselineIdArr[property][name][key] = val;
              })

              baselineIndex++;

            })
          }
        }
        
        //put dates -> 'E', 'O' inside quarterIdArr
        for(const property in file.bayOccupancy){
          let bayIndex = 1;

          file.bayOccupancy[property].slice(1).forEach(arr => {
            let name = `quarter${bayIndex}`;
            headerDatesArr.forEach(dte => arr[0][dte] = '')

            quarterIdArr[property][name] = {};

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

        //putting the key:value in baselineIdArr into baseLineOccupancy
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            let index = 1;
            
            file.baseLineOccupancy[property].slice(1).forEach(arr => {
              let name = `quarter${index}`;
  
              for(const inProp in baselineIdArr[property][name]){
                if(arr[0].hasOwnProperty(inProp)){
                  arr[0][inProp] = baselineIdArr[property][name][inProp];
                }
              }
  
              index++;
  
            })
          }
        }
        
        //putting the key:value in quarterIdArr into bayOccupancy
        for(const property in file.bayOccupancy){
          let tempindex = 1;
          
          file.bayOccupancy[property].slice(1).forEach(arr => {
            let name = `quarter${tempindex}`
            
            for(const inProp in quarterIdArr[property][name]){
              if(arr[0].hasOwnProperty(inProp)){
                arr[0][inProp] = quarterIdArr[property][name][inProp]
              }
            }

            tempindex++

          })
        }

        //putting 'E' under the key dates that are empty in baseLineOccupancy
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            file.baseLineOccupancy[property].slice(1).forEach(arr => {
              Object.entries(arr[0]).forEach(( [key, value] ) => {
                if(arr[0][key] === ''){
                  arr[0][key] = 'E'
                }
              })
            })
          }
        }

        //putting 'E' under the key dates that are empty in bayOccupancy
        for(const property in file.bayOccupancy){
          file.bayOccupancy[property].slice(1).forEach(arr => {
            Object.entries(arr[0]).forEach(( [key, value] ) => {
              if(arr[0][key] === ''){
                arr[0][key] = 'E'
              }
            })
          })
        }

        //form array of objects for excel file
        let excelFormat = [];

        //remove not needed keys inside baseLineOccupancy and pushing the keys needed into excelFormat
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            file.baseLineOccupancy[property].slice(1).forEach(arr => {

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
                'leaveBayDate',
                'secondaryCustomerName',
                'shipRisk_Upside',
                'shipRiskReason',
                'handOffDateToDE',
                'handOffDateBackToMFG',
                'installStartDate',
                'slotPlanNote',
                'commentFor$Change',
                'configurationNote',
                'dropShip',
                'MFGStatus',
                'coreNeedDate',
                'coreArrivalDate',
                'refurbStartDate',
                'refurbCompleteDate',
                'donorStatus',
                'coreUTID',
                'coreNotes',
                'flex01',
                'flex02',
                'flex03',
                'flex04',
                'lockMRPDate',
                'sendToStorageDate'
              ])

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
        }

        //remove not needed keys inside bayOccupancy and pushing the keys needed into excelFormat
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
              'leaveBayDate',
              'secondaryCustomerName',
              'shipRisk_Upside',
              'shipRiskReason',
              'handOffDateToDE',
              'handOffDateBackToMFG',
              'installStartDate',
              'slotPlanNote',
              'commentFor$Change',
              'configurationNote',
              'dropShip',
              'MFGStatus',
              'coreNeedDate',
              'coreArrivalDate',
              'refurbStartDate',
              'refurbCompleteDate',
              'donorStatus',
              'coreUTID',
              'coreNotes',
              'flex01',
              'flex02',
              'flex03',
              'flex04',
              'lockMRPDate',
              'sendToStorageDate'
            ])
            
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

        //format date inside excelFormat
        excelFormat.forEach(val => {
          changeDateFormat(val, ['toolStartDate','MRPDate','MFGCommitDate'])
        })

        //count the number of 'O' inside excelFormat
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

        //write as excel file format
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
    
    // @loc     PostResult.js
    // @desc    export of Mass_Slot_Upload excel file
    // @param   (object)
    const createExport = file => {
        setLoading();
        const output = []

        //get the actual data from baseLineOccupancy
        if(file.hasOwnProperty('baseLineOccupancy')){
          for(const property in file.baseLineOccupancy){
            file.baseLineOccupancy[property].slice(1).forEach(arr => {
              output.push({
                ...arr[0]
              })
            })
          }
        }

        //get the actual data from bayOccupancy
        for(const property in file.bayOccupancy){
          file.bayOccupancy[property].slice(1).forEach(arr => {
            output.push({
              ...arr[0]
            })
          })
        }

        //form array of object for excel file output
        const excelOutput = [];

        //check date format
        output.forEach(val => {
          changeDateFormat(
            val, 
            [
              'MRPDate',
              'intOpsShipReadinessDate',
              'MFGCommitDate',
              'shipRecogDate',
              'toolStartDate',
              'coreNeedDate',
              'coreArrivalDate',
              'refurbStartDate',
              'refurbCompleteDate',
              'handOffDateToDE',
              'handOffDateBackToMFG',
              'installStartDate'
            ]
          )

          //form it into the output
          excelOutput.push({
            'Argo ID': val.argoID,
            'Slot ID/UTID': val.slotID_UTID,
            'Slot Status': val.slotStatus,
            'Ship Rev Type': val.shipRevenueType,
            'Build Category': val.buildCategory,
            'Build Product': val.buildProduct,
            'Slot Plan Notes': val.slotPlanNote,
            'Plan Product Type': val.planProductType,
            'Ship Risk/Upside': val.shipRisk_Upside,
            'Ship Risk Reason': val.shipRiskReason,
            'Comment for $ Change': val.commentFor$Change,
            'Commited Ship $': val.committedShip$,
            'Secondary Customer Name': val.secondaryCustomerName,
            'Fab ID': val.fabID,
            'Sales Order #': val.salesOrder,
            'Forecast ID': val.forecastID,
            'Mfg Commit Date': val.MFGCommitDate,
            'Ship Recognition Date': val.shipRecogDate,
            'MRP Date': val.MRPDate,
            'Build Complete': val.buildComplete,
            'Internal Ops Ship Readiness Date': val.intOpsShipReadinessDate,
            'Plant': val.plant,
            'New/Used': val.new_Used,
            'Core Need Date': val.coreNeedDate,
            'Core Arrival Date': val.coreArrivalDate,
            'Refurb Start Date': val.refurbStartDate,
            'Refurb Complete Date': val.refurbCompleteDate,
            'Donor Status': val.donorStatus,
            'Core UTID': val.coreUTID,
            'Core Notes': val.coreNotes,
            'Mfg Status': val.MFGStatus,
            'Qty': val.quantity,
            'Configuration Note': val.configurationNote,
            'Drop Ship': val.dropShip,
            'RMA': val.RMATool,
            'Product PN': val.productPN,
            'Hand Off Date to DE': val.handOffDateToDE,
            'Hand Off Date back to Mfg': val.handOffDateBackToMFG,
            'Install Start Date': val.installStartDate,
            'Cycle Time Days': val.cycleTimeDays,
            'Flex01': val.flex01,
            'Flex02': val.flex02,
            'Flex03': val.flex03,
            'Flex04': val.flex04
          })
        })

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWS = XLSX.utils.json_to_sheet(excelOutput);

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

    // @loc     UploadState.js -> createExport, createExportSchedule
    // @desc    check if property exist and delete
    // @param   (object, array)
    const checkDeleteProperty = (obj, arr) => {
      arr.forEach(val => obj.hasOwnProperty(val) && delete obj[val])
    }

    // @loc     UploadState.js -> createExport, createExportSchedule
    // @desc    check if property exist and delete
    // @param   (object, array)
    const changeDateFormat = (obj, arr) => {
      arr.forEach(val => {
        obj[val] = obj[val] !== null ? new Date(obj[val]).toLocaleDateString('en-GB') : null
      })
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
    const reschedulePostResult = async (postResultDone, bays, newMinGap, maxgap) => {
      Object.keys(postResultDone).map(type => {
        Object.keys(postResultDone[type]).map(qtr => {
          for(let i = 1; i < postResultDone[type][qtr].length; i++){
            endDateCheck(postResultDone[type][qtr][i][0], 'endDate', newMinGap); // set the new endDate based on the newMinGap
            postResultDone[type][qtr][i][0].cycleTimeDays = parseInt(postResultDone[type][qtr][i][0].cycleTimeDays);
          }
        })
      })

      postResultDone.numBays = parseInt(bays);
      postResultDone.minGap = parseInt(newMinGap);
      postResultDone.maxGap = parseInt(maxgap);

      //update minGap to the newMinGap
      setMinGap(newMinGap);

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
      let BfirstQtrDates = [];
      // console.log(postResult.baseLineOccupancy)
      if (firstQtr in postResult.baseLineOccupancy){
        BfirstQtrDates = postResult.baseLineOccupancy[firstQtr][0];
      }

      let allDates = SfirstQtrDates.concat(BfirstQtrDates);
      allDates = Array.from(new Set(allDates));

      return allDates;
    }

    // update post result data "E"s
    const updatePostResultEmpties = (postResult, minGapDays) => {
      let postResultUpdate = JSON.parse(JSON.stringify(postResult));

      const qtrs = getQtrs(postResultUpdate);
      const firstQtrDates = getUniqueDates(postResultUpdate);
      let result = []

      // update baseline 
      if(qtrs in postResultUpdate.baseLineOccupancy){
        result = postResultUpdate.baseLineOccupancy[qtrs[0]];
        for( let i = 1; i < result.length; i++){
          let EOcount = result[i].slice(1).length;
          for (let j = EOcount; j < firstQtrDates.length; j++){
            result[i].push("E");
          }
        }
        result[0] = firstQtrDates;
        postResultUpdate.baseLineOccupancy[qtrs[0]] = result;
      }

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

      // update bays and gaps for history
      if("minGap" in postResultUpdate){
        setMinGap(postResultUpdate.minGap);
      }
      
      if("maxGap" in postResultUpdate){
        setMaxGap(postResultUpdate.maxGap);
      }
      
      if("numBays" in postResultUpdate){
        setBays(postResultUpdate.numBays);
      }

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
      
      if (qtrObj.lockMRPDate){ // if true, endDate == MRPDate
        qtrObj[key] = qtrObj.MRPDate;
      } else if (qtrObj.fabName == "OPEN"){ // if OPEN status, endDate == IRR - mingap
        qtrObj[key] = new Date(intRedDate.setTime(intRedDate.getTime() - minGap)).toLocaleDateString('en-GB')
      } else { // normal situation - endDate == MFG - mingap
        qtrObj[key] = new Date(MFGCommit.setTime(MFGCommit.getTime() - minGap)).toLocaleDateString('en-GB');
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
    
    const setNewMinGap = (res) => dispatch({ type: UPDATE_NEW_MIN_GAP, payload: res })
    const updateReschedule = (res) => dispatch({ type: UPDATE_RESCHEDULE, payload: res })
    const tabChecker = (res) => dispatch({ type: UPDATE_TABCHECKER, payload: res })

    // ##################################################################################################
    // ########################################## HISTORY START #########################################
    // ##################################################################################################

    //save to history
    const saveResult = async (postResult, bays, mingap, maxgap, staffID, histID) => {
      setLoading();

      postResult.numBays = parseInt(bays);
      postResult.minGap = parseInt(mingap);
      postResult.maxGap = parseInt(maxgap);
      postResult.staffID = parseInt(staffID);
      postResult.histID = (histID == null) ? null : parseInt(histID);

      // console.log(postResult);

      const config = {
          headers: {
              'Content-Type': 'application/json'
          }
      }

      try {
          let res = await axios.post('http://localhost:8080/savePreSchedule', postResult, config);

          dispatch({
              type: SAVE_RESULT,
              payload: res
          })
      } catch (err) {
          //prompt error

      }
    }

    // get a history
    const getHistory = async () => {
      try {
          const res = await axios.get(`http://localhost:8080/getHistory`);
          // console.log(res);

          dispatch({
              type: GET_HISTORY,
              payload: res
          })
      } catch (err) {
          
      }
    }

    // get ALL histories
    const loadHistories = async (staffID) => {
      try {
        const res = await axios.get(`http://localhost:8080/history/${staffID}`);
        // console.log(res);
        
        dispatch({
            type: LOAD_ALL_HISTORY,
            payload: res.data
        })
      } catch (err) {
        // console.log(err);
        dispatch({
          type: CREATE_RESULT_ERROR,
          payload: err.response.data.message
        })
      }
    }

    // ##################################################################################################
    // ########################################### HISTORY END ##########################################
    // ##################################################################################################

    // @loc     Preresult.js
    // @desc    to create the first post schedule output result
    // @param   (array, array, int, int, int)
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

    // @loc     Preresult.js
    // @desc    to generate schedule file 
    // @param   (array, int, array, int)
    const setSchedule = async (file, minGap, baseFile, numBay) => {
        setLoading();

        let data = await convertExcelToJSON(file);
        let scheduleCounter = false;

        data[data.length - 1]['Argo ID'] === undefined && data.pop();

        //check masterops have the necessary keys for the UI to be displayed
        data.forEach(val => {
          if((!(val.hasOwnProperty('Argo ID'))) || (!(val.hasOwnProperty('Slot ID/UTID'))) || (!(val.hasOwnProperty('Build Product'))) || (!(val.hasOwnProperty('Cycle Time Days'))) || (!(val.hasOwnProperty('MRP Date'))) || (!(val.hasOwnProperty('MFG Commit Date')))){
            scheduleCounter = true;
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
            let filtered_two = filterAndInsertToBaseline(filtered_one, baseFile, numBay)

            if(filtered_two.checker === false){
              dispatch({
                type: CREATE_RESULT_ERROR,
                payload: `Number of Baseline Products exceed number of Bays allocated! Current Number of Baseline Products = ${filtered_two.newBaseLength}`
              })
            } else {
              //check of previously shipped products (today's date & mfg commit date)
              let filtered = filtered_two['filteredOutput'].filter(obj => obj['MFG Commit Date'] >= new Date());

              //check date in right format & setup for end date
              filtered.forEach(obj => {
                checkDatesValue(obj, ['MRP Date','Created On','Created Time','SAP Customer Req Date','Ship Recog Date','Slot Request Date','Int. Ops Ship Readiness Date','MFG Commit Date','Div Commit Date','Changed On','Last Changed Time'])

                obj['Lock MRP Date'] = false

                let output = obj['Fab Name'] === 'OPEN' ? obj['Int. Ops Ship Readiness Date'] : obj['MFG Commit Date']
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
    }

    // @loc     Baseline.js
    // @desc    to get baseline from DB
    // @param   ()
    const getBaseline = async () => {
      setLoading()
      // const res = await axios.get('http://localhost:8080/getBaseline')

      dispatch({
        type: SET_BASELINE,
        payload: []
      })
    }

    // @loc     Preresult.js
    // @desc    update baseline, format the date for storing to DB
    // @param   (array)
    const updateBaseline = async base => {
      base.forEach(obj => {
        obj.hasOwnProperty('emptyToDelete') && delete obj['emptyToDelete'];
        
        checkDatesValue(obj, ['MRP Date','Created On','Created Time','SAP Customer Req Date','Ship Recog Date','Slot Request Date','Int. Ops Ship Readiness Date','MFG Commit Date','Div Commit Date','Changed On','Last Changed Time'])
      })

      const config = {
        headers: {
          'Content-Type': 'application/json'
        }
      }

      // const res = await axios.post('http://localhost:8080/setbaseline', base, config)

      dispatch({
        type: UPDATE_BASELINE,
        payload: base
      })
    }

    // @loc     UploadState.js -> setSchedule
    // @desc    compare and filter out the baseline with masterops
    // @param   (array, array, int)
    const filterAndInsertToBaseline = (filtered, base, numBay) => {
      let tempBase = [];

      if(base.length === 0) {
        return { filteredOutput: filtered }
      } else {
        filtered.forEach(obj => {
          base.forEach(val => {
            if(val['Slot ID/UTID'] === obj['Slot ID/UTID']){
              tempBase.push(obj)
            }
          })
        })

        if(tempBase.length === 0){
          return { filteredOutput: filtered }
        } else {
          tempBase.forEach(val => {
            filtered.forEach(obj => {
              if(val === obj){
                obj['emptyToDelete'] = ''
              }
            })
          })

          //return true/false to check newBaseline and number of bays
          let checker = checkNewBaselineAndBays(tempBase, numBay);
          
          dispatch({
            type: UPDATE_BASELINE,
            payload: tempBase
          })

          return {
            filteredOutput: filtered.filter(obj => !(obj.hasOwnProperty('emptyToDelete'))), 
            checker: checker, 
            newBaseLength: tempBase.length
          }
        }
      }
    }

    // @loc     UploadState.js -> filterAndInsertToBaseline
    // @desc    check if baseline products exceed number of bays
    // @param   (array, int)
    const checkNewBaselineAndBays = (newBase, numBay) => newBase.length <= numBay ? true : false

    // @loc     UploadState.js -> updateBaseline, setSchedule
    // @desc    check date value if undefined and change date format
    // @param   (object, array)
    const checkDatesValue = (obj, arr) => {
      arr.forEach(val => obj[val] = obj[val] === undefined ? '' : obj[val].toLocaleDateString('en-GB'))
    }

    // @loc     Preresult.js
    // @desc    update the masterops file
    // @param   (array)
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

    // @loc     Schedule.js
    // @desc    set number of bays
    // @param   (int)
    const setBays = (bayNum) => dispatch({ type: SET_BAYS, payload: bayNum })

    // @loc     Schedule.js
    // @desc    set number of min gap time
    // @param   (int)
    const setMinGap = (num) => dispatch({ type: SET_MIN_GAP, payload: num });

    // @loc     Schedule.js
    // @desc    set number of max gap time
    // @param   (int)
    const setMaxGap = (num) => dispatch({ type: SET_MAX_GAP, payload: num })

    //clear preresult
    // @loc     Schedule.js
    // @desc    clear the generated schedule
    // @param   ()
    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    // @loc     Schedule.js
    // @desc    clear errors in Schedule
    // @param   ()
    const uploadClearError = () => dispatch({ type: UPLOAD_CLEAR_ERROR })

    //set baseline
    // @loc     Baseline.js
    // @desc    set the baseline
    // @param   (array)
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
          data.forEach(obj => obj['Slot ID/UTID'] = obj['Slot ID/UTID'].toString())

          dispatch({
            type: SET_BASELINE,
            payload: data
          })
        }
    }

    // @loc     UploadState.js -> setBaseline, setSchedule
    // @desc    set number of bays
    // @param   (file-object)
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

    // @loc     UploadState.js
    // @desc    set loading
    // @param   ()
    const setLoading = () => dispatch({ type: SET_LOADING })

    // @loc     Navbar.js
    // @desc    back to default, for logout purpose
    // @param   ()
    const clearZero = () => dispatch({ type: CLEAR_ZERO })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            newBaseline: state.newBaseline,
            schedule: state.schedule,
            minGap: state.minGap,
            maxGap: state.maxGap,
            bays: state.bays,
            newMinGap: state.newMinGap,
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
            allHistory: state.allHistory,
            histID: state.histID,
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
            setMaxGap,
            getHistory,
            loadHistories,
            checkNewBaselineAndBays,
            setNewMinGap
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
