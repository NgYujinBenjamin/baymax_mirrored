import React, { Fragment, useState, useContext, useEffect } from 'react'
import { Button, Box, Card, CardContent, Input, InputLabel, Typography, Grid } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Preresult from './Preresult'
import Postresult from './Postresult'
import Spinner from '../layout/Spinner'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import ScheduleStep from '../layout/ScheduleStep.js';

const Schedule = () => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { setAlert } = alertContext;
    const { loadUser, updateNavItem } = authContext
    const { setSchedule, setBays, clearPreresult, schedule, bays, loading, scheduleDone, postResult, error, uploadClearError, stepcount, setStepCount } = uploadContext;

    useEffect(() => {
        loadUser();
        updateNavItem(0);

        if(error !== null){
            setAlert(error);
            uploadClearError();
        }

        console.log(scheduleDone)

        //eslint-disable-next-line
    }, [error])

    const [userInput, setUserInput] = useState({
        bayComponent: '',
        bayFile: null,
        fileName: '',
        bayFileValue: '',
        minGapTime: '',
        maxGapTime: ''
    })

    const handleChange = (event) => {
        const { name, value } = event.target;
        setUserInput({
            ...userInput,
            [name]: value
        });
    };

    const handleFileChange = (event) => {
        setUserInput({
            ...userInput,
            bayFile: event.target.files[0],
            fileName: event.target.files[0].name,
            bayFileValue: event.target.value
        });
    }
    
    const handleConfirm = (event) => {
        event.preventDefault();
        const regx = /^[0-9]+$/;
        const splitFilename = userInput.fileName.split('.')
        
        if(!regx.test(userInput.bayComponent)){
            setAlert('Please enter a number in the Available Bay field');
        } else if(!regx.test(userInput.minGapTime)) {
            setAlert('Please enter a number in the Minimum Gap field')
        } else if(!regx.test(userInput.maxGapTime)) {
            setAlert('Please enter a number in the Maximum Gap field')
        } else if(userInput.bayComponent === '' || userInput.bayFile === null) {
            setAlert('Please upload an excel file');
        } else if(splitFilename[splitFilename.length - 1] !== 'xlsx' && splitFilename[splitFilename.length - 1] !== 'xlsm') {
            setAlert('Please upload a .xlsx or .xlsm excel file');
        } else {
            setStepCount(stepcount + 1);
            setBays(userInput.bayComponent);
            setUserInput({
                ...userInput,
                minGapTime: parseInt(userInput.minGapTime),
                maxGapTime: parseInt(userInput.maxGapTime)
            })
            setSchedule(userInput.bayFile, userInput.minGapTime);
        }
    }

    const handleClearPreresult = () => {
        clearPreresult();
        setUserInput({
            bayComponent: '',
            bayFile: null,
            fileName: '',
            bayFileValue: '',
            minGapTime: '',
            maxGapTime: ''
        })
    }

    return (
        <Fragment>
            <ScheduleStep/>
            <Card>
                <CardContent>
                    <Box className={classes.box}>
                        <Grid container spacing={3}>
                            <Grid item xs>
                                <InputLabel htmlFor='bays'>No of Available Bays</InputLabel>
                                <Input fullWidth type='text' id='bays' name='bayComponent' value={userInput.bayComponent} onChange={handleChange} required />
                            </Grid>
                            <Grid item xs>
                                <InputLabel htmlFor='minGap'>Minimum Gap Time</InputLabel>
                                <Input fullWidth type='text' id='minGap' name='minGapTime' value={userInput.minGapTime} onChange={handleChange} required />
                            </Grid>
                            <Grid item xs>
                                <InputLabel htmlFor='maxGap'>Maximum Gap Time</InputLabel>
                                <Input fullWidth type='text' id='maxGap' name='maxGapTime' value={userInput.maxGapTime} onChange={handleChange} required />
                            </Grid>
                        </Grid>
                    </Box>
                    {!scheduleDone && 
                        <Box className={classes.box}>
                            <InputLabel className={classes.marginBottom}>Upload excel file:</InputLabel>
                            
                            <Box className={classes.marginBottom}>
                                <input 
                                    type='file' 
                                    onChange={handleFileChange}
                                    accept=".xlsx, .xlsm" 
                                    id='schedule-file' 
                                    style={{ display: 'none' }} 
                                    name='bayFile'
                                    value={userInput.bayFileValue}
                                />
                                <label htmlFor='schedule-file'>
                                    <Button variant='contained' color='primary' component='span' disabled={schedule !== null} startIcon={<CloudUploadIcon />}>
                                        Upload
                                    </Button>
                                </label>
                                <Typography component='span' variant='body2' style={{ marginLeft: '12px'}}>
                                    {userInput.fileName !== '' && userInput.fileName}
                                </Typography>
                            </Box>
                            {(userInput.bayFile && userInput.fileName !== '' && schedule === null) && <Button color='primary' variant='contained' fullWidth onClick={handleConfirm}>Confirm</Button>}
                            {(schedule !== null && bays !== '' && !scheduleDone) && <Button fullWidth color='default' variant='contained' className={classes.marginTop} onClick={handleClearPreresult}>Clear</Button>}
                        </Box>
                    }  
                    <Box>
                        {loading && <Spinner />}
                        {(schedule !== null && bays !== '' && !scheduleDone && !loading) && <Preresult fileName={userInput.fileName} minGap={userInput.minGapTime} maxGap={userInput.maxGapTime} /> }
                        {(postResult !== null && scheduleDone && !loading) && <Postresult />}
                    </Box>
                </CardContent>
            </Card>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    box: {
        marginBottom: 24
    },
    marginBottom: {
        marginBottom: 12
    },
    marginTop: {
        marginTop: 8
    }
}));

export default Schedule
