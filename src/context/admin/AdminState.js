import React, { useReducer } from 'react';
import AdminReducer from './adminReducer';
import AdminContext from './adminContext';
import axios from 'axios';
import { GET_USERS, DELETE_USER, USER_ERROR, RESET_PASSWORD, ADMIN_CLEAR_ERROR, CONVERT_ADMIN } from '../types';

const AdminState = (props) => {
    const initialState = {
        users: null,
        error: null
    }

    const [state, dispatch] = useReducer(AdminReducer, initialState);

    //all actions here

    //get all users
    const getUsers = async () => {
        try {
            const res = await axios.get('http://localhost:8080/getusers');
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

    //convert user to admin
    const convertAdmin = async (id) => {
        try {
            const res = await axios.get(`http://localhost:8080/convertadmin/${id}`)
            dispatch({
                type: CONVERT_ADMIN,
                payload: res.data
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
            deleteUser,
            resetPassword,
            convertAdmin,
            adminClearError
        }}>
        { props.children }        
    </AdminContext.Provider>
    
}

export default AdminState
