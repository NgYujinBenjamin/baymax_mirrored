import React, { useContext } from 'react';
import { Card, CardActions, Typography } from '@material-ui/core';
import CancelRoundedIcon from '@material-ui/icons/CancelRounded';
import AlertContext from '../../context/alert/alertContext';

const Alert = () => {
    const alertContext = useContext(AlertContext);

    return (
        alertContext.alert.length > 0 && alertContext.alert.map(al => (
            <div key={al.id}>
                <Card variant='outlined' style={{marginBottom: 12, backgroundColor: 'red'}}>
                    <CardActions disableSpacing style={{padding: 16}}>
                        <CancelRoundedIcon style={{marginRight:8, color:'white'}} />
                        <Typography component='span' variant='body2' style={{color:'white'}}>{al.msg}</Typography>
                    </CardActions>
                </Card>
            </div>
        ))
    )
}

export default Alert
