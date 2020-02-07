import React, { Fragment, useContext, useEffect, useState } from 'react';
import { TableContainer, Paper, Typography, TableRow, TableCell, TableHead, TableBody, Grid, Box, Button} from '@material-ui/core';
import AuthContext from '../../context/auth/authContext';
import HistoryDetailsItems from "./HistoryDetailsItem";
import HistContext from '../../context/history/histContext';
import SaveIcon from '@material-ui/icons/Save';
import ReplayIcon from '@material-ui/icons/Replay';
import '../../App.css';

const HistoryDetails = () => {
    const histContext = useContext(HistContext);
    const authContext = useContext(AuthContext);

    const { historyItems } = histContext;

    // Setting objs = historyItems and use setItems to update objs value
    const [ objs, setItems ] = useState(historyItems);

    useEffect(() => {
        authContext.loadUser();
    },)

    // retrieve msuID from href
    const id = parseInt(window.location.pathname.split("/")[2]);

    const handleChange = (item) => {
        return (event) => {
            const name = event.target.name;
            const value = event.target.value;
            console.log(item);

            setItems(prevObjs => (prevObjs.map((o) => {
                if (o === item) { // check if the row is the same before updating
                    return {...item, [name]: value} // ... copy the current object and update the key value pair 
                }
                return o;
            })))
        }
    }

    return (
        <Fragment>
            {/* Breadcrumbs */}
            <Typography>
                <a href='/profile' className="breadcrumb"> Profile</a> > 
                <a href='/history' className="breadcrumb"> History</a> > 
                <a className="breadcrumb"> History ID #{id}</a>
            </Typography>

            <Typography variant='h5' component='h3' gutterBottom> History Details </Typography>
            
            <TableContainer component={Paper}>
                <TableRow>
                    <TableHead>
                        <TableCell style={{minWidth: "300px"}}>Argo ID</TableCell>
                        <TableCell style={{minWidth: "170px"}}>UTID</TableCell>
                        <TableCell style={{minWidth: "170px"}}>Product Name</TableCell>
                        <TableCell style={{minWidth: "200px"}}>Cycle Time Days</TableCell>
                        <TableCell style={{minWidth: "200px"}}>MRP Date</TableCell>
                    </TableHead>
                    <TableBody>
                        {historyItems.map(item => (item.msuID === id) ?
                            <HistoryDetailsItems record={item} onChange={handleChange(item)} key={item.argoID}/> :
                            null
                        )}
                    </TableBody>
                </TableRow>
            </TableContainer>
            
            <Box style={{marginTop: 12}}>
                <Grid container spacing={1}>
                    <Grid item xs>
                        <Button variant='contained' startIcon={<SaveIcon />} style={{float: 'right', width: '40%'}} color='primary'>Save Changes</Button>
                    </Grid>
                    <Grid item xs>
                        <Button variant='contained' startIcon={<ReplayIcon />} style={{width: '40%'}}>Schedule Again</Button>
                    </Grid>
                </Grid>
            </Box>
        </Fragment>
    )
}

export default HistoryDetails