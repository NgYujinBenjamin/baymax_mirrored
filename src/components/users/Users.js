import React, { useContext, useEffect, Fragment } from 'react'
import { Typography } from '@material-ui/core';
import AdminContext from '../../context/admin/adminContext'
import UserItem from './UserItem'
import Spinner from '../layout/Spinner'

const Users = () => {
    const adminContext = useContext(AdminContext)
    
    const { users, getUsers, loading } = adminContext

    useEffect(() => {
        getUsers();
        //eslint-disable-next-line
    }, [])

    if(users !== null && users.length === 0 && !loading){
        return (
            <Typography component='p' variant='body2'>
                Please add a User
            </Typography>
        )
    }
    return (
        users !== null && !loading ? (
            <Fragment>
                {users.map(user => (
                    <UserItem user={user} key={user.staff_id} />
                ))}
            </Fragment>
        ) : (
            <Spinner />
        )

        // /*----ELECTRON CODE----*/
        // <Fragment>
        //     {users.map(user => (
        //         <UserItem user={user} key={user.id} />
        //     ))}
        // </Fragment>
    )
}

export default Users
