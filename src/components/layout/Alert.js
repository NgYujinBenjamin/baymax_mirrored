import React, { useContext, useState } from 'react';
import { Button, Snackbar } from '@material-ui/core';
import MuiAlert from '@material-ui/lab/Alert';
import { Card, CardActions, Typography } from '@material-ui/core';
import CancelRoundedIcon from '@material-ui/icons/CancelRounded';
import AlertContext from '../../context/alert/alertContext';

const AlertAlt = (props) => {
    return <MuiAlert elevation={6} variant='filled' {...props} />
}

const Alert = () => {
    const alertContext = useContext(AlertContext);
    const [open, setOpen] = useState(true);

    const handleClose =() => {
        setOpen(false)
    }

    return (
        alertContext.alert.length > 0 && alertContext.alert.map(al => (
            <div key={al.id} style={{ width: '100%' }}>
                <Snackbar open={open} autoHideDuration={4500} onClose={handleClose}>
                    <AlertAlt onClose={handleClose} severity={al.type}>
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
