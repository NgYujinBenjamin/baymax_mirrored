import React, { Fragment, useContext, useState, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Button, Box, Grid, Typography } from '@material-ui/core'
import { AppBar, Tabs, Tab } from '@material-ui/core';
import UploadContext from '../../context/upload/uploadContext'
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import GetAppIcon from '@material-ui/icons/GetApp';
import Postresultqtr from './Postresultqtr';

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);
    const [value, setValue] = useState(0); // this is for tab panel to display quarters

    const { postResult, createExport, createExportSchedule, saveFile, clearAll, currentQuarter, currentData, scheduletest } = uploadContext;

    // if(currentQuarter !== null){
    //     console.log(currentQuarter)
    // }
    
    const quarters = new Set(); // remove duplicates from baseline and predicted
    // Object.keys(scheduletest.baseline).map(quarterName => 
    //     quarters.add(quarterName)
    // )
    Object.keys(postResult.bayOccupancy).map(quarterName => 
        quarters.add(quarterName)
    )
    let qtrs = Array.from(quarters);

    const a11yProps = (index) => {
        return {
          id: `scrollable-force-tab-${index}`,
          'aria-controls': `scrollable-force-tabpanel-${index}`,
        }
    }

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const [arr, setArr] = useState([])
    const getData = (output) => {
        // if(arr.length === 0){
        // setArr([...arr, output])
        // }
        // setArr(prevState => prevState.map(val => val.argoID === output.argoID ? output : val))
    }

    //-----HERE------
    const handleClearAll = () => {
        clearAll();
    }

    const handleSave = () => {
        // if(currentData !== null){
        //     console.log(currentData)
        // }
        // saveFile(postResult);
        console.log(arr)
    }

    const handleExport = (event) => {
        event.preventDefault();
        createExport(scheduletest);
        createExportSchedule(scheduletest);
    }

    // const [ objs, setObjects ] = useState(postResult);
    // if(currentQuarter !== null){
    //     console.log(objs.bayOccupancy[currentQuarter])
    // }
    

    // const hChange = (obj) => {
    //     return (event) => {
    //         const value = event.target.value;
    //         const name = event.target.name;
    //         // console.log(obj);
    //         // console.log(objs)
    //         if(currentQuarter !== null){
    //             setObjects(prevObjs => (prevObjs.bayOccupancy[currentQuarter].slice(1).map((o) => {
    //                 // console.log(o)
    //                 if(o === obj){
    //                     // console.log(obj)
    //                     let temp = {...obj[0], [name]: value}
    //                     obj.unshift()
    //                     return [ temp, ...obj ]
    //                 }
    //             })))
    //         }
    //         // setObjects(prevObjs => (prevObjs.map((o) => {
    //         //     console.log(o)
    //         //     if (o === obj) {
    //         //         // let temp = {...obj[0], [name]: value}
    //         //         // obj.unshift()
    //         //         // return [ temp, ...obj ]
    //         //     }
    //         //     return o;
    //         // })))
    //     }
    // }

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
                            <Tab label={key} {...a11yProps({i})} key={i}/>
                        )}
                    </Tabs>
                </AppBar>

                {qtrs.map((qtr, index) =>
                    <Postresultqtr sendData={getData} schedule={postResult.bayOccupancy} value={value} num={index} quarter={qtr} key={index}/>
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
