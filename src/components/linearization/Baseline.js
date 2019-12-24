import React, { Fragment, useState, useContext } from 'react'
import { Paper, Typography, Button, Box, Collapse } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Linearize from './Linearize';
import UploadContext from '../../context/upload/uploadContext'

const Baseline = () => {
    const uploadContext = useContext(UploadContext);

    const classes = useStyles();
    const [expanded, setExpanded] = useState(false);

    const handleExpandBtn = (event) => {
        setExpanded(!expanded);
    }

    const handleFileChange = (event) => {
        uploadContext.setBaseline(event.target.files[0]);
    }

    const handleSubmit = (event) => {
        console.log(uploadContext.baseline);
        uploadContext.setStatusBaseline();
    }

    if(uploadContext.hasBaseline) { return <Linearize /> }
    return (
        <Fragment>
            <Paper className={classes.paper} >
                <Typography variant='h6' component='span'>
                    Import baseline excel file?  
                </Typography>
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
                <Collapse in={expanded}>
                    <Box className={classes.collapseBox}>
                        <input type='file' onChange={handleFileChange} />
                    </Box>
                    <Button variant='contained' color='primary' onClick={handleSubmit}>Submit</Button>
                </Collapse>
            </Paper>
        </Fragment>
    )
}


const useStyles = makeStyles(theme => ({
    paper: {
        padding: 16
    },
    box: {
        float: 'right'
    },
    button: {
        marginRight: 8
    },
    collapseBox: {
        marginBottom: 12,
        marginTop: 8
    }
}));

export default Baseline
