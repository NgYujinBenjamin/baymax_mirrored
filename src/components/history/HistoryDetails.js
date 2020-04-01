import React, { Fragment, useContext, useEffect } from 'react';
import { Card, CardContent, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';
import AuthContext from '../../context/auth/authContext';
import UploadContext from '../../context/upload/uploadContext';
import Postresult from '../schedule/Postresult';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

const HistoryDetails = (props) => {
    
    const authContext = useContext(AuthContext);
    const uploadContext = useContext(UploadContext);

    const { loadUser, updateNavItem } = authContext;
    const { getHistory } = uploadContext;

    useEffect(() => {
        loadUser();
        updateNavItem(1);
        
        const path = props.location.pathname.split('/');
        getHistory(path[2],path[3]);
        //eslint-disable-next-line
    }, [])

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <Link to={'/history'} style={{ textDecoration:'none' }}>
                        <Button size="small" color="primary" style={{ padding:0, marginBottom: 15 }} startIcon={<ArrowBackIcon />}> Back to History </Button>
                    </Link>
                    <Postresult></Postresult> 
                </CardContent>
            </Card>
        </Fragment>
    )
}

export default HistoryDetails