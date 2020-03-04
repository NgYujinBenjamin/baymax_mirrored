import React, { Fragment, useEffect, useContext, useState } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'

const Postresultbody = ({ result, baseline, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);

    const { setAlert } = alertContext;
    const { currentQuarter, updateCurrentQuarter, reschedule, tabUpdate, tabChecker, reschedulePostResult, saveResult, updateReschedule, updatePostResult, postResultDone, saveHistory, updateSave, endDateCheck, postResultErrors, handlePostResultError } = uploadContext;

    useEffect(() => {
        if(currentQuarter === null){
            updateCurrentQuarter(quarter);
        } 

        if( (currentQuarter !== null && currentQuarter !== quarter) || saveHistory || reschedule || tabUpdate ){
            appLevelSave(postResultDone, currentQuarter); // save local copy only
            
            if( Object.keys(postResultErrors).length !== 0 ){
                setAlert("Please fix existing errors in this table quarter! Update failed.");
                window.scrollTo(0,0);
                updateSave(false);
                updateReschedule(false);
                tabChecker(false);
            } else{
                // save success when user change tab
                updateCurrentQuarter(quarter);

                if(tabUpdate){
                    console.log("Errors saved for Tab");
                    tabChecker(false);
                }

                // save to history
                if(saveHistory){
                    console.log("Saved");
                    // saveResult(postResult); // send to backend via endpoint
                    updateSave(false);
                }
    
                // reschuling
                if(reschedule){
                    console.log("Rescheduled");
                    // reschedulePostResult(postResultDone); // send to backend via endpoint
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
        let errorMsg = validateDate(value) ? null : 'Invalid Date (d/m/yyyy)';

        // overwrite since move to storage can be empty
        if( type == 'moveToStorage' && value == ''){
            errorMsg = null;
        }

        if (type == 'cycleTimeDays'){
            errorMsg = validateNum(value);
        }

        handlePostResultError(postResultErrors, uniqueID, errorMsg, type);
    }

    const validateDate = (value) => {
        const dateParts = value.split("/");
        const date = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]);
    
        if (date.getDate() == dateParts[0] && date.getMonth() == (dateParts[1] - 1) && (date.getFullYear() == dateParts[2]) && dateParts[2].length == 4) {
            return true;
        }
        return false;
    }

    const validateNum = (value) => {
        let errorMsg = isNaN(value) ? 'Invalid number' : null;

        return errorMsg;
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
                    if (name == 'moveToStorage'){
                        validateFields(postResultErrors, value, obj[0].argoID, name); // validation check
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    }
                    if (name == 'lockMRPDate'){
                        checked = event.target.checked;
                        if (checked){
                            obj[0].endDate = obj[0].MRPDate;
                        } else{
                            // hardcoded min gap for now to 3 days
                            endDateCheck(obj[0], 'endDate', (24*60*60*1000) * 3);
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
            <Fragment>
                <TableBody style={{whiteSpace: "nowrap"}}>
                    { baseline !== null && baseline.map((obj, index) =>
                        <PostresultItem result={obj} id="baseline" key={index} />
                    )}
                    { objs.map((obj, index) =>
                        <PostresultItem result={obj} id="predicted" onChange={handleChange(obj, postResultErrors)} key={index} />
                    )}
                </TableBody>
            </Fragment>
        </Fragment>
    )
}

export default Postresultbody
