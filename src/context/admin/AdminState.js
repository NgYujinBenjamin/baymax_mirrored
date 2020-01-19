import React, { useReducer } from 'react';
import AdminReducer from './adminReducer';
import AdminContext from './adminContext';
import uuid from 'uuid'
import axios from 'axios';
import { ADD_USER, GET_USERS, DELETE_USER, USER_ERROR } from '../types';

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
    const getUsers = () => {
        // try {
        //     const res = await axios.get('<IP-ADDRESS>:<PORT>/<PATH>');
        //     console.log(res)
        //     dispatch({
        //         type: GET_USERS,
        //         payload: res.data
        //     }) 
        // } catch (err) {
        //     dispatch({
        //         type: USER_ERROR,
        //         payload: err.response.data
        //     })
        // }

        console.log();
    }

    //create user
    const addUser = (user) => {
        // const config = {
        //     headers: {
        //         'Content-Type': 'application/json'
        //     }
        // }

        // try {
        //     const res = await axios.post('<IP-ADDRESS>:<PORT>/<PATH>', user, config);
        //     console.log(res);
        //     dispatch({
        //         type: ADD_USER,
        //         payload: res.data
        //     });
        // } catch (err) {
        //     dispatch({
        //         type: USER_ERROR,
        //         payload: err.response.data
        //     })
        // }
        
        user.id = uuid.v4();
        console.log(user)
        dispatch({
            type: ADD_USER,
            payload: user
        })
    }

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
