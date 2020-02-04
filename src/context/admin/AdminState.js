import React, { useReducer } from 'react';
import AdminReducer from './adminReducer';
import AdminContext from './adminContext';
import uuid from 'uuid'
import axios from 'axios';
import { ADD_USER, GET_USERS, DELETE_USER, USER_ERROR, RESET_PASSWORD, ADMIN_CLEAR_ERROR } from '../types';

const AdminState = (props) => {
    const initialState = {
        // /*----ELECTRON CODE----*/
        // users: [
        //     {
        //         id: 1,
        //         username: 'johndoe-user',
        //         firstname: 'John',
        //         lastname: 'Doe',
        //         department: 'Marketing',
        //         role: 'admin'
        //     },
        //     {
        //         id: 2,
        //         username: 'marysmith-user',
        //         firstname: 'Mary',
        //         lastname: 'Smith',
        //         department: 'Supply',
        //         role: 'user'
        //     },
        //     {
        //         id: 3,
        //         username: 'barrywhite-user',
        //         firstname: 'Barry',
        //         lastname: 'White',
        //         department: 'IT',
        //         role: 'user'
        //     }
        // ],
        users: null,
        error: null
    }

    const [state, dispatch] = useReducer(AdminReducer, initialState);

    //all actions here

    //get all users
    const getUsers = async () => {
        try {
            const res = await axios.get('http://localhost:8080/getusers');
            console.log(res)
            dispatch({
                type: GET_USERS,
                payload: res.data
            }) 
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response
            })
        }
    }

    //create user
    const addUser = async (user) => {
        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/register', user, config);
            console.log(res);
            dispatch({
                type: ADD_USER,
                payload: res.data
            });
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response
            })
        }
        
        // user.id = uuid.v4();
        // console.log(user)
        // dispatch({
        //     type: ADD_USER,
        //     payload: user
        // })
    }

    //delete user
    const deleteUser = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/deleteuser/${id}`)
            dispatch({
                type: DELETE_USER,
                payload: id
            })
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response
            })
        }
    }

    //reset user password
    const resetPassword = async (id) => {
        try {
            await axios.get(`http://localhost:8080/resetpassword/${id}`)
            dispatch({
                type: RESET_PASSWORD
            })
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response
            })
        }
    }

    //clear error
    const adminClearError = () => dispatch({ type: ADMIN_CLEAR_ERROR })

    return <AdminContext.Provider 
        value={{
            users: state.users,
            error: state.error,
            getUsers,
            addUser,
            deleteUser,
            resetPassword,
            adminClearError
        }}>
        { props.children }        
    </AdminContext.Provider>
    
}

export default AdminState
