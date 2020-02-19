import React, { Fragment, useEffect, useContext, useState, useMemo, useCallback } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import { TableContainer, Paper, Typography, Box, Table, Button } from '@material-ui/core'
import UploadContext from '../../context/upload/uploadContext'

const Postresultbody = ({ result, id, quarter, sendhere }) => {
    const [ objs, setObjects ] = useState(result);
    // const [ arr, setArr ] = useState([]);

    const uploadContext = useContext(UploadContext);
    const { currentQuarter, updateCurrentQuarter, currentData, updateCurrentData } = uploadContext;

    useEffect(() => {
        if(currentQuarter === null || currentQuarter !== quarter){
            updateCurrentQuarter(quarter)
        }
        //eslint-disable-next-line
    }, [currentQuarter])

    // if(currentQuarter === null || currentQuarter !== quarter){
    //     updateCurrentQuarter(quarter)
    // }
    // if(currentData === null){
    //     updateCurrentData(objs)
    // }

    // if(currentData !== null){
    //     // console.log(currentData)
    // }

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            // console.log(obj);
            console.log(event.target)
            setObjects(prevObjs => (prevObjs.map((o) => {
                // console.log(o)
                if (o === obj) {

                    // if(currentData !== null){
                    //     const tempArr = currentData.map(valObj => {
                    //         if(valObj === o && valObj === obj){
                    //             return [{...valObj[0], 'cycleTimeDays': value}, ...valObj.slice(1)]
                    //         }
                    //         return valObj
                    //     })
                    // }
                    // sendData([ {...obj[0], [name]: value}, ...obj.slice(1) ])
                    
                    return [ {...obj[0], [name]: value}, ...obj.slice(1) ]
                }
                return o;
            })))
        }
    }

    // const computeChange = useMemo(() => handleChange(objs), [objs, handleChange])

    const handletest = () => {
        console.log(objs)
    }

    // return (
    //     <Fragment>
    //         <TableContainer component={Paper}>
    //             <Table>
    //                 <TableBody style={{ whiteSpace: 'nowrap' }}>
    //                     { objs.map((obj, index) =>
    //                         // <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
    //                         <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
    //                     )}
    //                 </TableBody>
    //             </Table>
    //         </TableContainer>
    //         <Button variant='contained' onClick={handletest} color='primary'>test</Button>
    //     </Fragment>
    // )

    return (
        <Fragment>
            <Fragment>
                <TableBody style={{whiteSpace: "nowrap"}}>
                    { objs.map((obj, index) =>
                        // <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
                        <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
                    )}
                </TableBody>
            </Fragment>
            <Fragment>
                <Button variant='contained' onClick={handletest} color='primary'>test</Button>
            </Fragment>
        </Fragment>
    )
}

export default Postresultbody
