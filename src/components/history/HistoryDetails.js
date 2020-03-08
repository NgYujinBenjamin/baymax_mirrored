import React, { Fragment, useContext, useEffect } from 'react';
import { Card, CardContent, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';
import UploadContext from '../../context/upload/uploadContext';
import AuthContext from '../../context/auth/authContext';
import Postresult from '../schedule/Postresult';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

const HistoryDetails = () => {
    
    const uploadContext = useContext(UploadContext);
    const authContext = useContext(AuthContext);

    const { loadUser, updateNavItem } = authContext;
    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [])

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <Link to={'/history'} style={{ textDecoration:'none' }}>
                        <Button size="small" color="primary" style={{ padding:0, marginBottom: 15 }} startIcon={<ArrowBackIcon />}> Back to History </Button>
                    </Link>
                    {/* Returns nth now since the endpoints are not working */}
                    <Postresult></Postresult> 
                </CardContent>
            </Card>
        </Fragment>
    )
}

export default HistoryDetails