import React, { Fragment, useState, useContext, useEffect } from 'react'
import { Button, Box, Card, CardContent, Input, InputLabel, Typography } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Preresult from './Preresult'
import Postresult from './Postresult'
import Spinner from '../layout/Spinner'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'
import CloudUploadIcon from '@material-ui/icons/CloudUpload';

const Linearize = () => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { setLinearize, setBays, clearPreresult, linearize, bays, loading, linearizeDone, postResult } = uploadContext;

    useEffect(() => {
        authContext.loadUser();

        if(linearizeDone){
            setUserInput({
                bayComponent: '',
                bayFile: null,
                fileName: ''
            })
        }
        //eslint-disable-next-line
    }, [linearizeDone])

    const [userInput, setUserInput] = useState({
        bayComponent: '',
        bayFile: null,
        fileName: ''
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
            [event.target.name]: event.target.files[0],
            fileName: event.target.files[0].name
        });
    }
    
    const handleConfirm = (event) => {
        event.preventDefault();
        const regx = /^[0-9]+$/;
        
        if(!regx.test(userInput.bayComponent)){
            alertContext.setAlert('Please enter a number in the available bay field');
        } else if(userInput.bayComponent === '' || userInput.bayFile === null) {
            alertContext.setAlert('Please enter the number of available bays and upload an excel file');
        } else {
            setBays(userInput.bayComponent);
            setLinearize(userInput.bayFile);
        }
    }

    const handleClearPreresult = () => {
        clearPreresult();
        setUserInput({
            bayComponent: '',
            bayFile: null,
            fileName: ''
        })
    }

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <Box className={classes.box}>
                        <InputLabel htmlFor='bays'>No of Available Bays</InputLabel>
                        <Input fullWidth type='text' id='bays' name='bayComponent' value={userInput.bayComponent} onChange={handleChange} required />
                    </Box>
                    <Box className={classes.box}>
                        <InputLabel className={classes.marginBottom}>Upload excel file:</InputLabel>
                        <Box className={classes.marginBottom}>
                            <input 
                                type='file' 
                                onChange={handleFileChange}
                                accept=".xlsx, .xlsm" 
                                id='linearize-file' 
                                style={{ display: 'none' }} 
                                name='bayFile'
                            />
                            <label htmlFor='linearize-file'>
                                <Button variant='contained' color='primary' component='span' disabled={linearizeDone} startIcon={<CloudUploadIcon />}>
                                    Upload
                                </Button>
                            </label>
                            <Typography component='span' variant='body2' style={{ marginLeft: '12px'}}>
                                {userInput.fileName !== '' && userInput.fileName}
                            </Typography>
                        </Box>
                        {(userInput.bayFile && userInput.fileName !== '') && <Button color='primary' variant='contained' fullWidth onClick={handleConfirm}>Confirm</Button>}
                        {(linearize !== null && bays !== '' && !linearizeDone) && <Button fullWidth color='default' variant='contained' className={classes.marginTop} onClick={handleClearPreresult}>Clear</Button>}
                    </Box>
                    <Box>
                        {loading && <Spinner />}
                        {(linearize !== null && bays !== '' && !linearizeDone && !loading) && <Preresult fileName={userInput.fileName} /> }
                        {(postResult !== null && linearizeDone && !loading) && <Postresult />}
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

export default Linearize
