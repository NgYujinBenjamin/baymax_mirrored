import React, { Fragment, useContext, useEffect } from 'react';
import { Typography } from '@material-ui/core';
import HistoryItem from './HistoryItem';
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import '../../App.css';

const History = () => {
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);

    const { allHistory } = uploadContext;
    const { loadUser, updateNavItem } = authContext;
    
    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [])

    return (
        <Fragment>
            <Typography variant='h2' style={{ fontSize: '2rem', marginBottom: '16px' }}>
                Schedule Histories
            </Typography>

            {/* Load the top 10 histories retrieved from database */}
            { allHistory.map(history =>
                <HistoryItem record={history} key={history.msuID} />
            )}
        </Fragment>
    )
}

export default History