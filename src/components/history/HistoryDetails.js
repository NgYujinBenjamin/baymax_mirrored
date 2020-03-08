import React, { Fragment, useContext, useEffect, useState } from 'react';
import { Box, Grid, Card, CardContent, InputLabel, Input, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles'
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import Postresult from '../schedule/Postresult';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

const HistoryDetails = () => {
    
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);

    const { loadUser, updateNavItem } = authContext;
    const { bays, minGap, maxGap } = uploadContext;

    const [ bayNum, setBay ] = useState(bays);
    const [ minimumGap, setMin ] = useState(minGap);
    const [ maximumGap, setMax ] = useState(maxGap);

    const classes = useStyles();

    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [])

    const handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        
        if (name == 'bayComponent'){
            setBay(value);
        } else if (name == 'minGapTime'){
            setMin(value);
        } else{
            setMax(value);
        }
    }

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <Link to={'/history'} style={{ textDecoration:'none' }}>
                        <Button size="small" color="primary" style={{ padding:0 }} startIcon={<ArrowBackIcon />}> Back to History </Button>
                    </Link>
                    <Box className={classes.box}>
                        <Grid container spacing={3}>
                            <Grid item xs>
                                <InputLabel htmlFor='bays'>No of Available Bays</InputLabel>
                                <Input fullWidth type='text' id='bays' name='bayComponent' value={bayNum} onChange={handleChange} required />
                            </Grid>
                            <Grid item xs>
                                <InputLabel htmlFor='minGap'>Minimum Gap Time</InputLabel>
                                <Input fullWidth type='text' id='minGap' name='minGapTime' value={minimumGap} onChange={handleChange} required />
                            </Grid>
                            <Grid item xs>
                                <InputLabel htmlFor='maxGap'>Maximum Gap Time</InputLabel>
                                <Input fullWidth type='text' id='maxGap' name='maxGapTime' value={maximumGap} onChange={handleChange} required />
                            </Grid>
                        </Grid>
                    </Box>
                    {/* Returns nth now since the endpoints are not working */}
                    <Postresult></Postresult> 
                </CardContent>
            </Card>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    box: {
        marginBottom: 24,
        marginTop: 24
    }
}));

export default HistoryDetails