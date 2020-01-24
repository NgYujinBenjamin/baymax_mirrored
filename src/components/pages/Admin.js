import React, { Fragment, useEffect, useContext } from 'react'
import { Grid } from '@material-ui/core'
import Users from '../users/Users'
import UserForm from '../users/UserForm'
import AuthContext from '../../context/auth/authContext'
import AdminContext from '../../context/admin/adminContext'
import AlertContext from '../../context/alert/alertContext'

const Admin = () => {
    const authContext = useContext(AuthContext);
    const adminContext = useContext(AdminContext);
    const alertContext = useContext(AlertContext);

    const { setAlert } = alertContext;
    const { error, adminClearError } = adminContext;

    useEffect(() => {
        authContext.loadUser();

        if(error !== null){
            setAlert(error)
            adminClearError();
        }

        //eslint-disable-next-line
    }, [error])

    return (
        <Fragment>
            <Grid container spacing={2}>
                <Grid item xs>
                    <UserForm />
                </Grid>
                <Grid item xs>
                    <Users />
                </Grid>
            </Grid>
        </Fragment>
    )
}

export default Admin
