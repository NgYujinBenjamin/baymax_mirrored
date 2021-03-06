import React, { Fragment, useEffect, useContext } from 'react'
import { Typography } from '@material-ui/core'
import Users from '../users/Users'
import AuthContext from '../../context/auth/authContext'
import AdminContext from '../../context/admin/adminContext'
import AlertContext from '../../context/alert/alertContext'

const Admin = () => {
    const authContext = useContext(AuthContext);
    const adminContext = useContext(AdminContext);
    const alertContext = useContext(AlertContext);

    const { setAlert } = alertContext;
    const { error, success, adminClearError } = adminContext;
    const { loadUser, updateNavItem } = authContext;

    useEffect(() => {
        loadUser();
        updateNavItem(1);

        if(error !== null){
            setAlert(error, 'error')
            adminClearError();
        }

        if(success !== null){
            setAlert(success, 'success')
            adminClearError();
        }

        //eslint-disable-next-line
    }, [error, success])

    return (
        <Fragment>
            <Typography variant='h4' gutterBottom style={{ fontWeight: 'bold' }}>User List</Typography>
            <Users />
        </Fragment>
    )
}

export default Admin
