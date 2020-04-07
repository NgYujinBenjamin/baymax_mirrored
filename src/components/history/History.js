import React, { Fragment, useContext, useEffect } from 'react';
import { Typography } from '@material-ui/core';
import HistoryItem from './HistoryItem';
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import '../../App.css';

const History = () => {
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);

    const { allHistory, loadHistories } = uploadContext;
    const { user, loadUser, updateNavItem } = authContext;

    useEffect(() => {
        loadUser();
        updateNavItem(1);
        if(user !== null){
            loadHistories(user['staff_id']);
        }
        //eslint-disable-next-line
    }, [user])

    return (
        <Fragment>
            <Typography variant='h2' style={{ fontSize: '2rem', marginBottom: '16px' }}>
                Scheduled Histories
            </Typography>

            { allHistory.length > 0 ? allHistory.map(history =>
                <HistoryItem record={history} key={history.histID} />
            ) : 'No records'}
        </Fragment>
    )
}

export default History