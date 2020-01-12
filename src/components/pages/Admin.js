import React, { Fragment, useEffect, useContext } from 'react'
import { Grid } from '@material-ui/core'
import Users from '../users/Users'
import AuthContext from '../../context/auth/authContext'

const Admin = () => {
    const authContext = useContext(AuthContext);

    useEffect(() => {
        authContext.loadUser();
        //eslint-disable-next-line
    }, [])

    return (
        <Fragment>
            <Grid container spacing={1}>
                <Grid item xs>
                    {/* User form */}
                </Grid>
                <Grid item xs>
                    <Users />
                </Grid>
            </Grid>
        </Fragment>
    )
}

export default Admin
