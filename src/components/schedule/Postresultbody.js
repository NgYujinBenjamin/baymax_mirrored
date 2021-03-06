import React, { Fragment, useEffect, useContext, useState } from 'react';
import { TableBody } from '@material-ui/core';
import PostresultItem from './PostresultItem';
import AuthContext from '../../context/auth/authContext';
import UploadContext from '../../context/upload/uploadContext';
import AlertContext from '../../context/alert/alertContext';

const Postresultbody = ({ result, base, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    
    const { setAlert } = alertContext;
    const { baseline, histID, newMinGap, bays, minGap, maxGap, validateDate, validateNum, currentQuarter, updateCurrentQuarter, reschedule, tabUpdate, tabChecker, reschedulePostResult, saveResult, updateReschedule, updatePostResult, postResultDone, postResult, saveHistory, updateSave, endDateCheck, postResultErrors, handlePostResultError } = uploadContext;
    const { user } = authContext;

    useEffect(() => {
        if(currentQuarter === null){
            updateCurrentQuarter(quarter);
        } 

        if( (currentQuarter !== null && currentQuarter !== quarter) || saveHistory || reschedule || tabUpdate ){
            appLevelSave(postResultDone, currentQuarter, bays, baseline.length, postResultErrors); // save local copy only
            
            if( Object.keys(postResultErrors).length !== 0 ){
                if( bays < baseline.length ){
                    setAlert("Number of Baseline Products exceed Number of Bays allocated.", 'error');
                } else if(reschedule){
                    setAlert("Please fix existing errors in this table quarter! Failed to send for reschedule.", 'error');
                } else if(saveHistory){
                    setAlert("Please fix existing errors in this table quarter! Failed to save.", 'error');
                } else{
                    setAlert("Please fix existing errors in the table before switching quarter!", 'error');
                }
                window.scrollTo(0,0);
                updateSave(false);
                updateReschedule(false);
                tabChecker(false);
            } else{
                // save success when user change tab
                updateCurrentQuarter(quarter);

                if(tabUpdate){
                    tabChecker(false);
                }

                // save to history
                if(saveHistory){
                    if(histID == null){
                        saveResult(postResult, bays, newMinGap, maxGap, user['staff_id'], histID, 'savetoDB');
                        setAlert("Scheduled result has been saved to history!", 'success');
                    } else{
                        saveResult(postResult, bays, newMinGap, maxGap, user['staff_id'], histID, 'updateDB');
                        setAlert("Scheduled result has been updated!", 'success');
                    }
                    updateSave(false);
                }
    
                // rescheduling
                if(reschedule){
                    reschedulePostResult(postResultDone, bays, newMinGap, maxGap);
                    setAlert("Schedule has been regenerated!", 'success');
                    updateReschedule(false);
                }
            }
        }

        localStorage.setItem('postResultEdit', JSON.stringify(objs));
        //eslint-disable-next-line
    }, [currentQuarter, objs, tabUpdate, saveHistory, reschedule])

    const appLevelSave = (postResultDone, currentQuarter, bays, baseline, postResultErrors) => {
        if( bays < baseline ){
            postResultErrors['bay'] = "";
        } else{
            delete postResultErrors['bay'];
        }

        updatePostResult(postResultDone, localStorage.getItem('postResultEdit'), currentQuarter);
        localStorage.setItem('postResultEdit', null);
    }

    const validateFields = (postResultErrors, value, argoID, type, index) => {
        let uniqueID = argoID + "_" + type;
        let errorMsg = validateDate(value);

        // overwrite since move to storage can be empty
        if( type == 'sendToStorageDate' && value == ''){
            errorMsg = null;
        }

        if (type == 'cycleTimeDays'){
            errorMsg = validateNum(value);
        }

        // Special checks
        let dateParts, fieldCheck, MRPoriginal;
        if( (type == 'MRPDate' || type == 'sendToStorageDate') && validateDate(value) == null){
            dateParts = value.split("/");
            fieldCheck = new Date(parseInt(dateParts[2]), parseInt((dateParts[1] - 1)), parseInt(dateParts[0]));
            MRPoriginal = new Date(postResult.bayOccupancy[quarter][index+1][0].MRPDate);
            
            // check pull in MRPDate 
            if(type == 'MRPDate' && fieldCheck !== MRPoriginal && fieldCheck < MRPoriginal){
                errorMsg = "Cannot pull earlier than " + MRPoriginal.toLocaleDateString('en-GB');
            }
            
            // sendToStorage cannot be earlier than MRP Date
            if(type == 'sendToStorageDate' && fieldCheck < MRPoriginal){
                errorMsg = "Storage date cannot be earlier than MRP date";
            }
        }
        
        handlePostResultError(postResultErrors, uniqueID, errorMsg);
    }

    const handleChange = (obj, postResultErrors, index) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            let checked = false;
            
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    if (name == 'cycleTimeDays'){
                        validateFields(postResultErrors, value, obj[0].argoID, name); // validation check
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    } 
                    if (name == 'sendToStorageDate'){
                        validateFields(postResultErrors, value, obj[0].argoID, name, index); // validation check
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    }
                    if (name == 'lockMRPDate'){
                        checked = event.target.checked;
                        if (checked){
                            obj[0].endDate = obj[0].MRPDate;
                        } else{
                            endDateCheck(obj[0], 'endDate', minGap, 'postResultCheck');
                        }
                        return [ {...obj[0], [name]: checked}, ...obj.slice(1) ];
                    }
                    if (name == 'MRPDate'){
                        validateFields(postResultErrors, value, obj[0].argoID, name, index); // validation check
                        if (obj[0].lockMRPDate){
                            obj = [ {...obj[0], 'endDate': value}, ...obj.slice(1) ];
                        }
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    }
                }
                return o;
            })))
        }
    }

    return (
        <Fragment>
            {!reschedule &&
                <TableBody style={{whiteSpace: "nowrap"}}>
                    { base !== null && base.map((obj, index) =>
                        <PostresultItem result={obj} id="baseline" key={index} />
                    )}
                    { result !== null && objs.map((obj, index) =>
                        <PostresultItem result={obj} id="predicted" onChange={handleChange(obj, postResultErrors, index)} key={index} />
                    )}
                </TableBody>
            }
        </Fragment>
    )
}

export default Postresultbody
