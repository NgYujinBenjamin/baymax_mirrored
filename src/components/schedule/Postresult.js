import React, { Fragment, useContext, useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Button, Box, Grid, InputLabel, Input, AppBar, Tabs, Tab } from '@material-ui/core';
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import GetAppIcon from '@material-ui/icons/GetApp';
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import Postresultqtr from './Postresultqtr';

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);

    const [value, setValue] = useState(0); // this is for tab panel to display quarters

    const { loadUser, updateNavItem } = authContext
    const { bays, minGap, maxGap, setBays, setMinGap, setMaxGap, currentQuarter, postResult, postResultDone, postResultErrors, tabChecker, updateReschedule, createExport, createExportSchedule, updateSave, clearAll, updatePostResultEmpties } = uploadContext;

    useEffect(() => {
        updateNavItem(0);
        if(postResult !== null){
            updatePostResultEmpties(postResult, minGap);
        }
        //console.log(postResult)
        //eslint-disable-next-line
    }, [postResult])

    const qtrs = new Array();
    if(postResult !== null){
        Object.keys(postResult.bayOccupancy).map(quarterName => 
            qtrs.push(quarterName)
        )
    }

    const a11yProps = (index) => {
        return {
          id: `scrollable-force-tab-${index}`,
          'aria-controls': `scrollable-force-tabpanel-${index}`,
        }
    }

    const tabsChange = (event, newValue) => {
        // console.log(newValue);
        if (Object.keys(postResultErrors).length == 0){
            setValue(newValue);
        } else{
            setValue(qtrs.indexOf(currentQuarter)); //set back to the original quarter
            tabChecker(true);
        }
    };

    const handleClearAll = () => {
        clearAll();
    }

    const handleSave = () => {
        updateSave(true);
    }

    const handleExport = (event) => {
        event.preventDefault();
        createExport(postResult); // use postResult since export will be based on the first generated result data
        createExportSchedule(postResult);
    }

    const handleReschedule = () => {
        updateReschedule(true);
    }

    const handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        if(name == 'bayNum'){
            setBays(value);
        } else if (name == 'minGap'){
            setMinGap(value);
        } else{
            setMaxGap(value);
        }
    }

    return (
        <Fragment>
            <Box className={classes.box}>
                <Grid container spacing={3}>
                    <Grid item xs>
                        <InputLabel htmlFor='bays'>No of Available Bays</InputLabel>
                        <Input fullWidth type='text' id='bays' name='bayNum' value={bays} onChange={handleChange} required />
                    </Grid>
                    <Grid item xs>
                        <InputLabel htmlFor='minGap'>Minimum Gap Time</InputLabel>
                        <Input fullWidth type='text' id='minGap' name='minGap' value={minGap} onChange={handleChange} required />
                    </Grid>
                    <Grid item xs>
                        <InputLabel htmlFor='maxGap'>Maximum Gap Time</InputLabel>
                        <Input fullWidth type='text' id='maxGap' name='maxGapTime' value={maxGap} onChange={handleChange} required />
                    </Grid>
                </Grid>
            </Box>
            <div>
                <AppBar position="static" color="default">
                    <Tabs
                        value={value}
                        onChange={tabsChange}
                        variant="scrollable"
                        scrollButtons="on"
                        indicatorColor="primary"
                        textColor="primary"
                        aria-label="scrollable force tabs example"
                    >
                        {/* This will create the tab headers */}
                        { qtrs.map((key, i) => 
                            <Tab label={key} {...a11yProps({i})} key={i}/>
                        )}
                    </Tabs>
                </AppBar>
                            
                {postResultDone !== null && qtrs.map((qtr, index) =>
                    <Postresultqtr schedule={postResultDone.bayOccupancy} baseline={postResultDone.baseLineOccupancy} value={value} num={index} quarter={qtr} key={index}/>
                )}
            </div>
            
            <Box className={classes.marginTop}>
                <Grid container spacing={1}>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleClearAll} startIcon={<DeleteIcon />} color='secondary'>Clear All</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleSave} startIcon={<SaveIcon />}>Save Result</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleExport} startIcon={<GetAppIcon />}>Export to Excel File</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleReschedule} startIcon={<TrendingUpIcon />} color='primary'>Generate Schedule Again</Button>
                    </Grid>
                </Grid>
            </Box>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    box: { marginBottom: 24 },
    marginTop: { marginTop: 12 }
}));

export default Postresult
