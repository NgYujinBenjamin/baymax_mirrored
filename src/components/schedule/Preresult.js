import React, { Fragment, useContext, useState, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@material-ui/core'
import PreresultItem from './PreresultItem'
import AuthContext from '../../context/auth/authContext'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'

const Preresult = () => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { updateNavItem, user } = authContext;
    const { setAlert } = alertContext;
    const { schedule, updateSchedule, createResult, bays, stepcount, setStepCount, updateBaseline, minGap, maxGap, newBaseline } = uploadContext;

    const [objs, setObjects] = useState(schedule);

    useEffect(() => {
        setStepCount(stepcount + 1);
        updateNavItem(0)
        //eslint-disable-next-line
    }, [])

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
            const name = event.target.name;
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    if(name === 'MRP Date'){
                        if(obj['Lock MRP Date'] === true){
                            return {
                                ...obj,
                                [name]: value,
                                'End Date': value
                            }
                        } else {
                            let output = obj['Fab Name'] === 'OPEN' ? obj['Int. Ops Ship Readiness Date'] : obj['MFG Commit Date']
                            let currentDate = dateConvert(output)
                            currentDate.setDate(currentDate.getDate() - minGap)
                            return {
                                ...obj,
                                [name]: value,
                                'End Date': currentDate.toLocaleDateString()
                            }
                        }
                    }

                    if(name === 'Lock MRP Date'){
                        if(value === true){
                            return {
                                ...obj,
                                'End Date': obj['MRP Date'],
                                [name]: value
                            }
                        } else {
                            let output = obj['Fab Name'] === 'OPEN' ? obj['Int. Ops Ship Readiness Date'] : obj['MFG Commit Date']
                            let currentDate = dateConvert(output)
                            currentDate.setDate(currentDate.getDate() - minGap)
                            return {
                                ...obj,
                                'End Date': currentDate.toLocaleDateString(),
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

    const dateConvert = (date) => {
        let dates = date.split('/')
        let year = parseInt(dates[2])
        let month = parseInt(dates[1]) - 1
        let day = parseInt(dates[0])
        let currentDate = new Date(year, month, day)
        return currentDate
    }

    const handleSchedule = (event) => {
        event.preventDefault();
        const regxDate = /^\d\d\/\d\d\/\d\d\d\d$/;
        const regxNum = /^[0-9]+$/;
        let preCounter = false;

        objs.forEach(obj => {
            if(!regxDate.test(obj['MRP Date']) || !regxNum.test(obj['Cycle Time Days'])){
                preCounter = true;
            }
        })

        if(preCounter){
            setAlert('Please fix existing errors in this table! Begin Schedule Creation failed.', 'error');
            window.scrollTo(0,0);
        } else {
            updateSchedule(objs);

            if(newBaseline.length > 0){
                updateBaseline(newBaseline, user.staff_id);
            }
    
            createResult(newBaseline, objs, bays, minGap, maxGap);
            setStepCount(stepcount + 2);
        }
    }
    
    return (
        <Fragment>
            <TableContainer component={Paper} style={{ maxHeight: 500 }}>
                <Table stickyHeader>
                    <TableHead>
                        <TableRow style={{ whiteSpace: 'nowrap' }}>
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
                            <PreresultItem obj={obj} onChange={handleChange(obj)} key={index} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Button 
                className={classes.marginTop} 
                id='beginSchedule'
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
