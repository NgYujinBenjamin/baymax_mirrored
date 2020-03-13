import React, { Fragment, useState, useContext, useEffect } from 'react'
import { Typography, Button, Box, Collapse, Card, CardContent, CardActions } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import UploadContext from '../../context/upload/uploadContext'
import AuthContext from '../../context/auth/authContext'
import AlertContext from '../../context/alert/alertContext'
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import ScheduleStep from '../layout/ScheduleStep.js';

const Baseline = (props) => {
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);
    const alertContext = useContext(AlertContext);
    const classes = useStyles();

    const { setBaseline, stepcount, setStepCount, error, uploadClearError, baseline, getBaseline } = uploadContext;
    const { loadUser, updateNavItem } = authContext;
    const { setAlert } = alertContext;

    useEffect(() => {
        loadUser()
        updateNavItem(0)
        setStepCount(0)

        if(error !== null){
            setAlert(error, 'error')
            uploadClearError()
        }

        if(baseline !== null){
            setStepCount(stepcount + 1);
            props.history.push('/schedule');
        }
        //eslint-disable-next-line
    }, [error, props.history, baseline])

    const [expanded, setExpanded] = useState(false);
    const [file, setFile] = useState(null);
    const [name, setName] = useState(null);

    const handleExpandBtn = (event) => {
        setExpanded(!expanded);
    }

    const handleFileChange = (event) => {
        setName(event.target.files[0].name);
        setFile(event.target.files[0]);
    }

    const handleSubmit = (event) => {
        getBaseline();
        props.history.push('/schedule');
    }

    const handleCollapseSubmit = (event) => {
        const splitFilename = name.split('.')

        if(splitFilename[splitFilename.length - 1] !== 'xlsx' && splitFilename[splitFilename.length -1] !== 'xlsm'){
            setAlert('Please upload a .xlsx or .xlsm excel file', 'error')
        } else {
            setBaseline(file);
        }
    }

    return (
        <Fragment>
            <ScheduleStep/>
            <Card>
                <CardContent>
                    <CardActions disableSpacing>
                        <Typography component='span' variant='h5'>Import bay requirement excel file?</Typography>
                        <Box component='span' className={classes.box}>
                            <Button 
                                className={classes.button} 
                                variant='contained' 
                                color='default' 
                                onClick={handleExpandBtn}
                                aria-expanded={expanded}
                            >
                                Yes
                            </Button>
                            <Button 
                                className={classes.button} 
                                variant='contained' 
                                color='default'
                                onClick={handleSubmit}
                            >
                                No
                            </Button>
                        </Box>
                    </CardActions>
                    <Collapse in={expanded}>
                        <Box className={classes.collapseBox}>
                            <Typography variant='h5' style={{ marginBottom: '8px' }}>
                                Upload Excel File
                            </Typography>
                            <input 
                                type='file' 
                                onChange={handleFileChange} 
                                accept=".xlsx, .xlsm" 
                                id='baseline-file' 
                                style={{ display: 'none' }} 
                            />
                            <label htmlFor='baseline-file'>
                                <Button variant='contained' color='primary' component='span' startIcon={<CloudUploadIcon />}>
                                    Upload
                                </Button>
                            </label>
                            <Typography component='span' variant='body2' style={{ marginLeft: '12px'}}>
                                {name !== null && name}
                            </Typography>
                            {(file && name) && <Button className={classes.collapseButton} fullWidth variant='contained' color='primary' onClick={handleCollapseSubmit}>Submit</Button>}
                        </Box>
                    </Collapse>
                </CardContent>
            </Card>
        </Fragment>
    )
}


const useStyles = makeStyles(theme => ({
    box: {
        marginLeft: 'auto'
    },
    button: {
        marginRight: 8
    },
    collapseBox: {
        marginBottom: 16,
        marginTop: 8,
        marginLeft: 8
    },
    collapseButton: {
        marginTop: '12px'
    }
}));

export default Baseline
