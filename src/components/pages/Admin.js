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
    const { error, adminClearError } = adminContext;
    const { loadUser } = authContext;

    useEffect(() => {
        loadUser();

        if(error !== null){
            setAlert(error)
            adminClearError();
        }

        //eslint-disable-next-line
    }, [error])

    return (
        <Fragment>
            <Typography variant='h4' gutterBottom style={{ fontWeight: 'bold' }}>User List</Typography>
            <Users />
        </Fragment>
    )
}

export default Admin
