import React, { Fragment, useEffect, useContext, useState } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'

const Postresultbody = ({ result, baseline, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const { currentQuarter, updateCurrentQuarter, updatePostResult, postResult, scheduletest, saved, updateSave, endDateCheck, postResultErrors, handlePostResultError } = uploadContext;

    useEffect(() => {
        if(currentQuarter === null){
            updateCurrentQuarter(quarter);
        } 

        // if user change tab, it will do an auto save on the previous tab
        if( (currentQuarter !== null && currentQuarter !== quarter) || saved){
            saveChanges(saved, scheduletest, currentQuarter);
            updateCurrentQuarter(quarter);
        }

        localStorage.setItem('postResultEdit', JSON.stringify(objs));
        //eslint-disable-next-line
    }, [currentQuarter, objs, saved])
    
    const saveChanges = (saved, scheduletest, currentQuarter) => {
        updatePostResult(scheduletest, localStorage.getItem('postResultEdit'), currentQuarter);
        localStorage.setItem('postResultEdit', null);
        updateSave(!saved); // go back to false
    }

    const validateFields = (postResultErrors, value, argoID, type) => {
        let uniqueID = argoID + "_" + type;
        let errorMsg = validateDate(value) ? null : 'Invalid date format';

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
