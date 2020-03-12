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

    // METHODS

    // @loc     Users.js
    // @desc    get all users
    // @param   ()
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

    // @loc     UserItem.js
    // @desc    delete the user
    // @param   (string)
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

    // @loc     UserItem.js
    // @desc    reset user password
    // @param   (string)
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

    // @loc     UserItem.js
    // @desc    convert user to admin
    // @param   (string)
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

    // @loc     Admin.js
    // @desc    clear errors in admin component
    // @param   ()
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
