import React, { Fragment, useEffect, useContext, useState } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'

const Postresultbody = ({ result, baseline, quarter }) => {
    const [ objs, setObjects ] = useState(result);
    
    const uploadContext = useContext(UploadContext);
    const { currentQuarter, updateCurrentQuarter, updatePostResult, postResult, scheduletest, saved, updateSave } = uploadContext;

    useEffect(() => {
        if(currentQuarter === null || currentQuarter !== quarter){
            updateCurrentQuarter(quarter);
        }

        if(saved){
            // console.log(scheduletest);
            updatePostResult(scheduletest, localStorage.getItem('postResultEdit'), quarter);
            localStorage.setItem('postResultEdit', null);
            updateSave(!saved); // go back to false
            console.log(scheduletest);
        }

        localStorage.setItem('postResultEdit', JSON.stringify(objs));
        //eslint-disable-next-line
    }, [currentQuarter, objs, saved])
    
    // console.log(objs);

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            // console.log(obj);
            // console.log(event.target)
            setObjects(prevObjs => (prevObjs.map((o) => {
                // console.log(o)
                if (o === obj) {
                    if (name === 'MRPDate'){
                        return [ {...obj[0], 'MRPDate': value}, ...obj.slice(1) ]
                    }
                    if (name === 'cycleTimeDays'){
                        return [ {...obj[0], 'cycleTimeDays': parseInt(value)}, ...obj.slice(1) ]
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
