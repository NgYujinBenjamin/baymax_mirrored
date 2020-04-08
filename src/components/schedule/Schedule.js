import React, { Fragment, useState, useContext, useEffect } from 'react'
import { Button, Box, Card, CardContent, Input, InputLabel, Typography, Grid, TextField } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Preresult from './Preresult'
import Postresult from './Postresult'
import Spinner from '../layout/Spinner'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import ScheduleStep from '../layout/ScheduleStep.js';

const Schedule = (props) => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { setAlert } = alertContext;
    const { loadUser, updateNavItem } = authContext
    const { setSchedule, setBays, postResultDone, clearPreresult, schedule, loading, scheduleDone, postResult, error, uploadClearError, stepcount, setStepCount, baseline, setMinGap, setMaxGap, success } = uploadContext;

    useEffect(() => {
        loadUser();
        updateNavItem(0);

        if(error !== null){
            window.scrollTo(0,0);
            setAlert(error, 'error');
            uploadClearError();

            setUserInput({
                bayComponent: '',
                bayFile: null,
                fileName: '',
                bayFileValue: '',
                minGapTime: '',
                maxGapTime: ''
            })
        }

        if(success !== null){
            window.scrollTo(0,0);
            setAlert(success, 'success');
            uploadClearError();
        }

        if(baseline === null){
            props.history.push('/baseline');
        }

        //eslint-disable-next-line
    }, [error, baseline, props.history, success])

    const [userInput, setUserInput] = useState({
        bayComponent: '',
        bayFile: null,
        fileName: '',
        bayFileValue: '',
        minGapTime: '',
        maxGapTime: ''
    })

    const { bayComponent, bayFile, fileName, bayFileValue, minGapTime, maxGapTime } = userInput

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
        const splitFilename = fileName.split('.')
        
        if(!regx.test(bayComponent)){
            setAlert('Please enter a number in the Available Bay field', 'error');
        } else if(!regx.test(minGapTime)) {
            setAlert('Please enter a number in the Minimum Gap field', 'error')
        } else if(!regx.test(maxGapTime)) {
            setAlert('Please enter a number in the Maximum Gap field', 'error')
        } else if(bayComponent === '' || bayFile === null) {
            setAlert('Please upload an excel file', 'error');
        } else if(splitFilename[splitFilename.length - 1] !== 'xlsx' && splitFilename[splitFilename.length - 1] !== 'xlsm') {
            setAlert('Please upload a .xlsx or .xlsm excel file', 'error');
        } else {
            setBays(parseInt(bayComponent));
            setMinGap(parseInt(minGapTime));
            setMaxGap(parseInt(maxGapTime));

            setSchedule(bayFile, parseInt(minGapTime), baseline, parseInt(bayComponent));
            setUserInput({
                bayComponent: '',
                bayFile: null,
                fileName: '',
                bayFileValue: '',
                minGapTime: '',
                maxGapTime: ''
            })
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
                    {schedule === null && postResultDone == null && 
                        <Box className={classes.box}>
                            <Grid container spacing={3}>
                                <Grid item xs>
                                    <InputLabel htmlFor='bay'>No of Available Bays</InputLabel>
                                    <TextField
                                        error={isNaN(bayComponent)}
                                        helperText={isNaN(bayComponent) && 'Invalid number'}
                                        type='text'
                                        id='bay'
                                        name='bayComponent'
                                        value={bayComponent}
                                        onChange={handleChange} 
                                        required
                                        fullWidth
                                    />
                                </Grid>
                                <Grid item xs>
                                    <InputLabel htmlFor='minGap'>Minimum Gap Time</InputLabel>
                                    <TextField 
                                        error={isNaN(minGapTime)}
                                        helperText={isNaN(minGapTime) && 'Invalid number'}
                                        type='text'
                                        id='minGap'
                                        name='minGapTime'
                                        value={minGapTime}
                                        onChange={handleChange}
                                        required
                                        fullWidth
                                    />
                                </Grid>
                                <Grid item xs>
                                    <InputLabel htmlFor='maxGap'>Maximum Gap Time</InputLabel>
                                    <TextField 
                                        error={isNaN(maxGapTime)}
                                        helperText={isNaN(maxGapTime) && 'Invalid number'}
                                        type='text'
                                        id='maxGap'
                                        name='maxGapTime'
                                        value={maxGapTime}
                                        onChange={handleChange}
                                        required
                                        fullWidth
                                    />
                                </Grid>
                            </Grid>
                        </Box>
                    }
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
                                    value={bayFileValue}
                                />
                                <label htmlFor='schedule-file'>
                                    <Button variant='contained' id='upload' color='primary' component='span' disabled={schedule !== null} startIcon={<CloudUploadIcon />}>
                                        Upload
                                    </Button>
                                </label>
                                <Typography component='span' variant='body2' style={{ marginLeft: '12px'}}>
                                    {fileName !== '' && fileName}
                                </Typography>
                            </Box>
                            {(bayFile && fileName !== '' && schedule === null) && <Button color='primary' id='confirm' variant='contained' fullWidth onClick={handleConfirm}>Confirm</Button>}
                            {(schedule !== null && !scheduleDone) && <Button fullWidth color='default' id='clear' variant='contained' className={classes.marginTop} onClick={handleClearPreresult}>Clear</Button>}
                        </Box>
                    }  
                    <Box>
                        {loading && <Spinner />}
                        {(schedule !== null && !scheduleDone && !loading) && <Preresult /> }
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
