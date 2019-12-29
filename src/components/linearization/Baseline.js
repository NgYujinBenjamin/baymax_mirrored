import React, { Fragment, useState, useContext } from 'react'
import { Typography, Button, Box, Collapse, Card, CardContent, CardActions } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import UploadContext from '../../context/upload/uploadContext'

const Baseline = (props) => {
    const uploadContext = useContext(UploadContext);
    const classes = useStyles();

    const { setBaseline, baseline } = uploadContext;

    const [expanded, setExpanded] = useState(false);
    const [file, setFile] = useState(null);

    const handleExpandBtn = (event) => {
        setExpanded(!expanded);
    }

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    }

    const handleSubmit = (event) => {
        props.history.push('/linearize');
    }

    const handleCollapseSubmit = (event) => {
        setBaseline(file);
        props.history.push('/linearize');
    }

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
                            <input type='file' onChange={handleFileChange} accept=".xlsx, .xlsm" />
                        </Box>
                        <Button fullWidth variant='contained' color='primary' onClick={handleCollapseSubmit}>Submit</Button>
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
