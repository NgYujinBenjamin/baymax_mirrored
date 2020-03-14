import React, { useContext } from 'react';
import { Snackbar } from '@material-ui/core';
import MuiAlert from '@material-ui/lab/Alert';
import AlertContext from '../../context/alert/alertContext';
import { makeStyles } from '@material-ui/core/styles';

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
}

export default Alert
