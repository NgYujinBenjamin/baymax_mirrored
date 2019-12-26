import React, { useContext } from 'react';
import { Card, CardActions, Typography } from '@material-ui/core';
import CancelRoundedIcon from '@material-ui/icons/CancelRounded';
import AlertContext from '../../context/alert/alertContext';

const Alert = () => {
    const alertContext = useContext(AlertContext);

    const { alert } = alertContext;

    return (
        alert !== null && (
            <Card variant='outlined' style={{marginBottom: 12, backgroundColor: '#f4f4f8'}}>
                <CardActions disableSpacing style={{padding: 16}}>
                    <CancelRoundedIcon color='error' style={{marginRight:8}} />
                    <Typography component='span' variant='body2' color='error'>{alert}</Typography>
                </CardActions>
            </Card>
        )
    )
}

export default Alert
