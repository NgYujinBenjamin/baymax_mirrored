import React, { Fragment, useContext } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Box, Grid, Typography, Card, CardActions } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import GetAppIcon from '@material-ui/icons/GetApp';
import DoneIcon from '@material-ui/icons/Done';

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);

    const { postResult, createExport, saveFile, clearAll } = uploadContext;

    const handleClearAll = () => {
        clearAll();
    }

    const handleSave = () => {
        saveFile(postResult);
    }

    const handleExport = () => {
        createExport(postResult);
    }

    return (
        <Fragment>
            {postResult.unfulfilled.length > 0 ? (
                <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Argo ID</TableCell>
                            <TableCell>UTID</TableCell>
                            <TableCell>Product Name</TableCell>
                            <TableCell>Derived Tool Completion</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {postResult.unfulfilled.map((result, index) => (
                            <PostresultItem result={result} key={index} />
                        ))}
                    </TableBody>
                </Table>
                </TableContainer>
            ) : (
                <Card variant='outlined' style={{backgroundColor: 'green'}}>
                    <CardActions disableSpacing style={{padding: 8}}>
                        <DoneIcon style={{marginRight:8, color:'white'}} />
                        <Typography component='span' variant='body2' style={{color:'white'}}>All Orders Fulfilled</Typography>
                    </CardActions>
                </Card>
            )}
            
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
