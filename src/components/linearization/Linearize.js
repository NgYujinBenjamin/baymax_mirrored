import React, { Fragment, useState, useContext } from 'react'
import { Button, Box, Card, CardContent, Input, InputLabel } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Preresult from './Preresult'
import UploadContext from '../../context/upload/uploadContext'
import AlertContext from '../../context/alert/alertContext'

const Linearize = () => {
    const uploadContext = useContext(UploadContext);
    const alertContext = useContext(AlertContext);
    const classes = useStyles();

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
        // console.log(event.target.files[0].name)
        setUserInput({
            ...userInput,
            [event.target.name]: event.target.files[0],
            fileName: event.target.files[0].name
        });
    }
    
    const handleConfirm = (event) => {
        event.preventDefault();
        if(userInput.bayComponent === '' || userInput.bayFile === null){
            alertContext.setAlert('Please enter the number of available bays and upload an excel file');
        } else {
            uploadContext.setLinearize(userInput.bayFile);
            uploadContext.setBays(userInput.bayComponent);
        }
    }

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <form>
                        <Box className={classes.box}>
                            <InputLabel htmlFor='bays'>No of Available Bays:</InputLabel>
                            <Input fullWidth type='text' id='bays' name='bayComponent' value={userInput.bayComponent} onChange={handleChange} required />
                        </Box>
                        <Box className={classes.box}>
                            <InputLabel className={classes.marginBottom} htmlFor='file'>Import excel file:</InputLabel>
                            <input name='bayFile' className={classes.marginBottom} type='file' onChange={handleFileChange} accept=".xlsx, .xlsm" />
                            <Button color='primary' variant='contained' fullWidth onClick={handleConfirm}>Confirm</Button>
                            {(uploadContext.linearize.length > 0 && uploadContext.bays !== '') && <Button fullWidth color='default' variant='contained' className={classes.marginTop} onClick={uploadContext.clearPreresult}>Clear</Button>}
                        </Box>
                        <Box>
                            {(uploadContext.linearize.length > 0 && uploadContext.bays !== '') && <Preresult fileName={userInput.fileName} />}
                        </Box>
                    </form>
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
