import React, { Fragment, useState, useContext, useEffect } from 'react'
import { Button, Box, Card, CardContent, Input, InputLabel } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Preresult from './Preresult'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'

const Linearize = () => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);
    const classes = useStyles();

    const { setLinearize, setBays, clearPreresult, linearize, bays } = uploadContext;

    useEffect(() => {
        authContext.loadUser();
        //eslint-disable-next-line
    }, [])

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
            setLinearize(userInput.bayFile);
            setBays(userInput.bayComponent);
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
                        <InputLabel htmlFor='bays'>No of Available Bays:</InputLabel>
                        <Input fullWidth type='text' id='bays' name='bayComponent' value={userInput.bayComponent} onChange={handleChange} required />
                    </Box>
                    <Box className={classes.box}>
                        <InputLabel className={classes.marginBottom} htmlFor='file'>Import excel file:</InputLabel>
                        <input name='bayFile' className={classes.marginBottom} type='file' onChange={handleFileChange} accept=".xlsx, .xlsm" />
                        <Button color='primary' variant='contained' fullWidth onClick={handleConfirm}>Confirm</Button>
                        {(linearize.length > 0 && bays !== '') && <Button fullWidth color='default' variant='contained' className={classes.marginTop} onClick={handleClearPreresult}>Clear</Button>}
                    </Box>
                    <Box>
                        {(linearize.length > 0 && bays !== '') && <Preresult fileName={userInput.fileName} />}
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
