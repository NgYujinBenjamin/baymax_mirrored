import React, { Fragment, useContext, useState, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Button, Box, Grid, Typography } from '@material-ui/core'
import { AppBar, Tabs, Tab } from '@material-ui/core';
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import GetAppIcon from '@material-ui/icons/GetApp';
import Postresultqtr from './Postresultqtr';

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);
    const [value, setValue] = useState(0); // this is for tab panel to display quarters

    const { loadUser, updateNavItem } = authContext
    const { postResult, createExport, createExportSchedule, saved, updateSave, saveFile, clearAll, scheduletest, updatePostResultEmpties, setPostResult } = uploadContext;

    useEffect(() => {
        updateNavItem(0)
        //eslint-disable-next-line
        updateNavItem(0);
        updatePostResultEmpties(scheduletest);
        setPostResult(scheduletest);
    }, [])

    const qtrs = new Array();
    Object.keys(scheduletest.bayOccupancy).map(quarterName => 
        qtrs.push(quarterName)
    )

    const a11yProps = (index) => {
        return {
          id: `scrollable-force-tab-${index}`,
          'aria-controls': `scrollable-force-tabpanel-${index}`,
        }
    }

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const handleClearAll = () => {
        clearAll();
    }

    const handleSave = () => {
        updateSave(!saved);
        // if(currentData !== null){
        //     console.log(currentData)
        // }
        // saveFile(postResult);
        // console.log(arr)
    }

    const handleExport = (event) => {
        event.preventDefault();
        createExport(scheduletest);
        createExportSchedule(scheduletest);
    }

    return (
        <Fragment>
            <div>
                <AppBar position="static" color="default">
                    <Tabs
                        value={value}
                        onChange={handleChange}
                        variant="scrollable"
                        scrollButtons="on"
                        indicatorColor="primary"
                        textColor="primary"
                        aria-label="scrollable force tabs example"
                    >
                        {/* This will create the tab headers */}
                        { qtrs.map((key, i) => 
                            <Tab label={key} {...a11yProps({i})} key={i} />
                        )}
                    </Tabs>
                </AppBar>

                {qtrs.map((qtr, index) =>
                    <Postresultqtr schedule={scheduletest.bayOccupancy} baseline={scheduletest.baseLineOccupancy} value={value} num={index} quarter={qtr} key={index}/>
                )}
            </div>
            
            <Box className={classes.marginTop}>
                <Grid container spacing={1}>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleClearAll} startIcon={<DeleteIcon />} color='secondary'>Clear All</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleSave} startIcon={<SaveIcon />} color='primary'>Save</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained' onClick={handleExport} startIcon={<GetAppIcon />}>Export</Button>
                    </Grid>
                </Grid>
            </Box>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    marginTop: { marginTop: 12 }
}));

export default Postresult
