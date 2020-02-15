import React, { Fragment, useContext } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Button, Box, Grid, Typography } from '@material-ui/core'
import { AppBar, Tabs, Tab } from '@material-ui/core';
import UploadContext from '../../context/upload/uploadContext'
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import GetAppIcon from '@material-ui/icons/GetApp';
import DoneIcon from '@material-ui/icons/Done';
import Postresultqtr from './Postresultqtr';

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);
    const [value, setValue] = React.useState(0); // this is for tab panel to display quarters

    const { postResult, createExport, createExportSchedule, saveFile, clearAll, scheduletest } = uploadContext;

    const quarters = new Set(); // remove duplicates from baseline and predicted
    Object.keys(scheduletest.baseline).map(quarterName => 
        quarters.add(quarterName)
    )
    Object.keys(scheduletest.predicted).map(quarterName => 
        quarters.add(quarterName)
    )
    let qtrs = Array.from(quarters);

    const handleClearAll = () => {
        clearAll();
    }

    const handleSave = () => {
        saveFile(postResult);
    }

    const handleExport = () => {
        createExport(postResult);
        createExportSchedule(scheduletest);
    }

    const a11yProps = (index) => {
        return {
          id: `scrollable-force-tab-${index}`,
          'aria-controls': `scrollable-force-tabpanel-${index}`,
        }
    }

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

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
                            <Tab label={key} {...a11yProps({i})} />
                        )}
                    </Tabs>
                </AppBar>

                {qtrs.map((qtr, index) =>
                    <Postresultqtr schedule={scheduletest} value={value} num={index} quarter={qtr} />
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
