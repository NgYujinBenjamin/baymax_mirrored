import React, { Fragment, useContext, useState, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@material-ui/core'
import PreresultItem from './PreresultItem'
import AuthContext from '../../context/auth/authContext'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'

const Preresult = ({ fileName, minGap, maxGap }) => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { updateNavItem } = authContext;
    const { setAlert } = alertContext;
    const { schedule, updateSchedule, createResult, bays, baseline, stepcount, setStepCount } = uploadContext;

    const [objs, setObjects] = useState(schedule);

    useEffect(() => {
        updateNavItem(0)
        //eslint-disable-next-line
    }, [])

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
            const name = event.target.name;
            // console.log(event.target)
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    if(name === 'MRP Date'){
                        if(obj['Lock MRP Date'] === false){
                            let dates = value.split('/')
                            let year = parseInt(dates[2])
                            let month = parseInt(dates[1]) - 1
                            let day = parseInt(dates[0])
                            let currentDate = new Date(year, month, day)
                            currentDate.setDate(currentDate.getDate() - minGap)
                            return {
                                ...obj,
                                [name]: value,
                                'End Date': currentDate.toLocaleDateString()
                            }
                        } else {
                            return {
                                ...obj,
                                [name]: value,
                                'End Date': value
                            }
                        }
                    }

                    if(name === 'Lock MRP Date'){
                        if(value === false){
                            let dates = obj['MRP Date'].split('/')
                            let year = parseInt(dates[2])
                            let month = parseInt(dates[1]) - 1
                            let day = parseInt(dates[0])
                            let currentDate = new Date(year, month, day)
                            currentDate.setDate(currentDate.getDate() - minGap)
                            return {
                                ...obj, 
                                'End Date': currentDate.toLocaleDateString(), 
                                [name]: value
                            }
                        } else {
                            return {
                                ...obj, 
                                'End Date': obj['MRP Date'], 
                                [name]: value
                            }
                        }
                    }
                    return {...obj, [name]: value}
                }
                return o;
            })))
        }
    }

    const handleSchedule = (event) => {
        event.preventDefault();
        //test regx
        const regxDate = /^\d\d\/\d\d\/\d\d\d\d$/;
        const regxNum = /^[0-9]+$/;
        let preCounter = false;

        //check if Cycle Time Days and MRP Date are NOT in the right format
        objs.forEach(obj => {
            if(!regxNum.test(obj['Cycle Time Days']) || !regxDate.test(obj['MRP Date'])){
                setAlert(`ArgoID: ${obj['Argo ID']}. Please enter a valid number in Cycle Time Days and valid date format (DD/MM/YYYY) in MRP Date`)
                preCounter = true;
            }
        })

        if(!preCounter) {
            // console.log(objs)
            updateSchedule(objs);
            createResult(objs, bays, baseline);
            setStepCount(stepcount + 2); // add 2 since it will be the end of the step
        } else {
            window.scrollTo(0,0);
        }
    }
    
    return (
        <Fragment>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>{fileName}</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Argo ID</TableCell>
                            <TableCell>Slot/UTID</TableCell>
                            <TableCell>Product Name</TableCell>
                            <TableCell>MRP Date</TableCell>
                            <TableCell>MFG Commit</TableCell>
                            <TableCell>Internal Readiness Date</TableCell>
                            <TableCell>End Date</TableCell>
                            <TableCell>Cycle Time</TableCell>
                            <TableCell>Lock MRP Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {objs.map((obj, index) => (
                            <PreresultItem obj={obj} onChange={handleChange(obj)} key={index} minGap={minGap} maxGap={maxGap} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Button 
                className={classes.marginTop} 
                fullWidth color='primary' 
                variant='contained' 
                onClick={handleSchedule}
            >
                Begin Schedule Creation
            </Button>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    marginTop: { marginTop: 8 }
}));

export default Preresult
