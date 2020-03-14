import React, { useContext, useState } from 'react';
import { Button, Snackbar, SnackbarContent } from '@material-ui/core';
import MuiAlert from '@material-ui/lab/Alert';
import { Card, CardActions, Typography } from '@material-ui/core';
import CancelRoundedIcon from '@material-ui/icons/CancelRounded';
import AlertContext from '../../context/alert/alertContext';
import { makeStyles } from '@material-ui/core/styles';
import { useSnackbar } from 'notistack';

const AlertAlt = (props) => {
    return <MuiAlert elevation={6} variant='filled' {...props} />
}

const useStyles = makeStyles(theme => ({
    root: {
      width: '100%',
      '& > * + *': {
        marginTop: theme.spacing(2),
      },
    }
}));

const Alert = () => {
    const alertContext = useContext(AlertContext);
    const classes = useStyles();

    return (
        alertContext.alert.length > 0 && alertContext.alert.map(al => (
            <div key={al.id} className={classes.root}>
                <Snackbar 
                    anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
                    open={true} 
                    autoHideDuration={4500}
                >
                    <AlertAlt severity={al.type}>
                        {al.msg}
                    </AlertAlt>
                </Snackbar>
            </div>
        ))
    )

    // return (
    //     alertContext.alert.length > 0 && alertContext.alert.map(al => (
    //         <div key={al.id}>
    //             <Card variant='outlined' style={{marginBottom: 12, backgroundColor: 'red'}}>
    //                 <CardActions disableSpacing style={{padding: 16}}>
    //                     <CancelRoundedIcon style={{marginRight:8, color:'white'}} />
    //                     <Typography component='span' variant='body2' style={{color:'white'}}>{al.msg}</Typography>
    //                 </CardActions>
    //             </Card>
    //         </div>
    //     ))
    // )
}

export default Alert
