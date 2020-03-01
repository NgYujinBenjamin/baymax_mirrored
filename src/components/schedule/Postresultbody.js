import React, { Fragment, useEffect, useContext, useState } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'

const Postresultbody = ({ result, baseline, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const { currentQuarter, updateCurrentQuarter, updatePostResult, postResult, scheduletest, saved, updateSave, endDateCheck } = uploadContext;

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

    // console.log(objs);

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            let checked = false;
            // console.log(obj);
            // console.log(event.target)
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    if (name == 'cycleTimeDays'){
                        return [ {...obj[0], [name]: parseInt(value)}, ...obj.slice(1) ];
                    } 
                    if (name == 'moveToStorage'){
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
                        if (obj[0].lockMRPDate){
                            // obj[0].endDate = value;
                            obj = [ {...obj[0], 'endDate': value}, ...obj.slice(1) ];
                        }
                        return [ {...obj[0], [name]: value}, ...obj.slice(1) ];
                    }
                    
                    // return [ {...obj[0], [name]: parseInt(value)}, ...obj.slice(1) ]
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
                        // <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
                        <PostresultItem result={obj} id="baseline" key={index} />
                    )}
                    { objs.map((obj, index) =>
                        // <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
                        <PostresultItem result={obj} id="predicted" onChange={handleChange(obj)} key={index} />
                    )}
                </TableBody>
            </Fragment>
        </Fragment>
    )
}

export default Postresultbody
