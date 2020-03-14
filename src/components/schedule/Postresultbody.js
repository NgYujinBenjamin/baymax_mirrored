import React, { Fragment, useEffect, useContext, useState } from 'react';
import { TableBody } from '@material-ui/core';
import PostresultItem from './PostresultItem';
import AuthContext from '../../context/auth/authContext';
import UploadContext from '../../context/upload/uploadContext';
import AlertContext from '../../context/alert/alertContext';

const Postresultbody = ({ result, baseline, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    
    const { setAlert } = alertContext;
    const { histID, newMinGap, bays, minGap, maxGap, validateDate, validateNum, currentQuarter, updateCurrentQuarter, reschedule, tabUpdate, tabChecker, reschedulePostResult, saveResult, updateReschedule, updatePostResult, postResultDone, postResult, saveHistory, updateSave, endDateCheck, postResultErrors, handlePostResultError } = uploadContext;
    const { user } = authContext;

    useEffect(() => {
        if(currentQuarter === null){
            updateCurrentQuarter(quarter);
        } 

        if( (currentQuarter !== null && currentQuarter !== quarter) || saveHistory || reschedule || tabUpdate ){
            appLevelSave(postResultDone, currentQuarter); // save local copy only
            
            if( Object.keys(postResultErrors).length !== 0 ){
                if(reschedule){
                    setAlert("Please fix existing errors in this table quarter! Failed to send for reschedule.");
                } else if(saveHistory){
                    setAlert("Please fix existing errors in this table quarter! Failed to save.");
                } else{
                    setAlert("Please fix existing errors in the table before switching quarter!");
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
                    saveResult(postResult, bays, minGap, maxGap, user['staff_id'], histID);
                    updateSave(false);
                }
    
                // rescheduling
                if(reschedule){
                    reschedulePostResult(postResultDone, bays, newMinGap, maxGap);
                    updateReschedule(false);
                }
            }
        }

        localStorage.setItem('postResultEdit', JSON.stringify(objs));
        //eslint-disable-next-line
    }, [currentQuarter, objs, tabUpdate, saveHistory, reschedule])

    const appLevelSave = (postResultDone, currentQuarter) => {
        updatePostResult(postResultDone, localStorage.getItem('postResultEdit'), currentQuarter);
        localStorage.setItem('postResultEdit', null);
    }

    const validateFields = (postResultErrors, value, argoID, type) => {
        let uniqueID = argoID + "_" + type;
        let errorMsg = validateDate(value);

        // overwrite since move to storage can be empty
        if( type == 'sendToStorageDate' && value == ''){
            errorMsg = null;
        }

        if (type == 'cycleTimeDays'){
            errorMsg = validateNum(value);
        }

        handlePostResultError(postResultErrors, uniqueID, errorMsg, type);
    }

    const handleChange = (obj, postResultErrors) => {
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
                        validateFields(postResultErrors, value, obj[0].argoID, name); // validation check
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    }
                    if (name == 'lockMRPDate'){
                        checked = event.target.checked;
                        if (checked){
                            obj[0].endDate = obj[0].MRPDate;
                        } else{
                            endDateCheck(obj[0], 'endDate', (24*60*60*1000) * minGap);
                        }
                        return [ {...obj[0], [name]: checked}, ...obj.slice(1) ];
                    }
                    if (name == 'MRPDate'){
                        validateFields(postResultErrors, value, obj[0].argoID, name); // validation check
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
                    { baseline !== null && baseline.map((obj, index) =>
                        <PostresultItem result={obj} id="baseline" key={index} />
                    )}
                    { objs.map((obj, index) =>
                        <PostresultItem result={obj} id="predicted" onChange={handleChange(obj, postResultErrors)} key={index} />
                    )}
                </TableBody>
            }
        </Fragment>
    )
}

export default Postresultbody
