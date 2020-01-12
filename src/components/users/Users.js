import React, { useContext, Fragment } from 'react'
import AdminContext from '../../context/admin/adminContext'
import UserItem from './UserItem'

const Users = () => {
    const adminContext = useContext(AdminContext)
    
    const { users } = adminContext

    return (
        <Fragment>
            {users.map(user => (
                <UserItem user={user} key={user.id} />
            ))}
        </Fragment>
    )
}

export default Users
