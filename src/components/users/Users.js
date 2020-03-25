import React, { useContext, useEffect, Fragment } from 'react'
import { Typography, Card, CardActions } from '@material-ui/core';
import AdminContext from '../../context/admin/adminContext'
import AuthContext from '../../context/auth/authContext'
import UserItem from './UserItem'
import Spinner from '../layout/Spinner'
import PersonAddIcon from '@material-ui/icons/PersonAdd';

const Users = () => {
    const adminContext = useContext(AdminContext);
    const authContext = useContext(AuthContext);
    
    const { users, getUsers, loading } = adminContext;
    const { loadUser, user } = authContext;

    useEffect(() => {
        loadUser();
        getUsers();
        //eslint-disable-next-line
    }, [users])

    if(users !== null && users.length === 0 && !loading){
        return (
            <Card variant='outlined' style={{backgroundColor: '#5bc0de'}}>
                <CardActions disableSpacing style={{padding: 8}}>
                    <PersonAddIcon style={{ marginRight: '8px', color: 'white' }} />
                    <Typography component='span' variant='body1' style={{ color: 'white' }}>Currently there are no users</Typography>
                </CardActions>
            </Card>
        )
    }
    return (
        users !== null && user !== null && !loading ? (
            <Fragment>
                {users.map(u => (
                    user.staff_id !== u.staff_id && <UserItem user={u} key={u.staff_id} />
                ))}
            </Fragment>
        ) : (
            <Spinner />
        )
    )
}

export default Users
