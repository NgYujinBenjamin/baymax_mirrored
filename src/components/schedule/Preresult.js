import React, { Fragment, useContext, useState } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@material-ui/core'
import PreresultItem from './PreresultItem'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'

const Preresult = ({ fileName }) => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const classes = useStyles();

    const { setAlert } = alertContext;
    const { schedule, updateSchedule, createResult, bays, baseline } = uploadContext;

    const [objs, setObjects] = useState(schedule);

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            console.log(event.target);
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    return {...obj, [name]: value}
                    // if (name === 'Cycle Time Days'){
                    //     return {...obj, 'Cycle Time Days': value }
                    // }
                    // if (name === 'MRP Date'){
                    //     return {...obj, 'MRP Date': value}
                    // }
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
            updateSchedule(objs);
            createResult(objs, bays, baseline);
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
                        </TableRow>
                        <TableRow>
                            <TableCell>Argo ID</TableCell>
                            <TableCell>UTID</TableCell>
                            <TableCell>Product Name</TableCell>
                            <TableCell>Cycle Time Days</TableCell>
                            <TableCell>MRP Date</TableCell>
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
