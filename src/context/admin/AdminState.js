import React, { useReducer } from 'react';
import AdminReducer from './adminReducer';
import AdminContext from './adminContext';
import { ADD_USER, GET_USERS, DELETE_USER } from '../types';

const AdminState = (props) => {
    const initialState = {
        users: [
            {
                id: 1,
                username: 'johndoe-user',
                firstname: 'John',
                lastname: 'Doe',
                department: 'Marketing',
                role: 'admin'
            },
            {
                id: 2,
                username: 'marysmith-user',
                firstname: 'Mary',
                lastname: 'Smith',
                department: 'Supply',
                role: 'user'
            },
            {
                id: 3,
                username: 'barrywhite-user',
                firstname: 'Barry',
                lastname: 'White',
                department: 'IT',
                role: 'user'
            }
        ],
        error: null
    }

    const [state, dispatch] = useReducer(AdminReducer, initialState);

    //all actions here

    //get all users
    const getUsers = () => console.log();

    //create user
    const addUser = () => console.log();

    //delete user
    const deleteUser = () => console.log();


    return <AdminContext.Provider 
        value={{
            users: state.users,
            error: state.error,
            getUsers,
            addUser,
            deleteUser
        }}>
        { props.children }        
    </AdminContext.Provider>
    
}

export default AdminState
