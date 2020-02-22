import React, { Fragment, useContext, useEffect } from 'react';
import { Typography } from '@material-ui/core';
import HistContext from '../../context/history/histContext';
import HistoryItem from './HistoryItem';
import AuthContext from '../../context/auth/authContext';
import '../../App.css';

const History = () => {
    const histContext = useContext(HistContext);
    const authContext = useContext(AuthContext);

    const { historyData } = histContext;
    const { user, loadUser, updateNavItem } = authContext;
    
    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [])

    // console.log(user);

    return (
        <Fragment>
            {/* Breadcrumbs */}
            <Typography>
                <a href='/profile' className="breadcrumb"> Profile</a> > 
                <a className="breadcrumb"> History</a> 
            </Typography>

            <Typography variant='h5' component='h3' gutterBottom> History </Typography>
            
            {historyData.map(history => (user && user.username === history.username) ?
                <HistoryItem record={history} key={history.msuID} /> :
                null
            )}
        </Fragment>
    )
}

export default History