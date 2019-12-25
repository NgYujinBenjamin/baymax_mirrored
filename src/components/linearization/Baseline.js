import React, { Fragment, useState, useContext } from 'react'
import { Typography, Button, Box, Collapse, Card, CardContent, CardActions } from '@material-ui/core'
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
        uploadContext.setStatusBaseline();
    }

    if(uploadContext.hasBaseline) { return <Linearize /> }
    return (
        <Fragment>
            <Card>
                <CardContent>
                    <CardActions disableSpacing>
                        <Typography component='span' variant='h6'>Import baseline excel file?</Typography>
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
                            <input type='file' onChange={handleFileChange} />
                        </Box>
                        <Button fullWidth variant='contained' color='primary' onClick={handleSubmit}>Submit</Button>
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
        marginTop: 8
    }
}));

export default Baseline
