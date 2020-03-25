import React, { useReducer } from 'react';
import AdminReducer from './adminReducer';
import AdminContext from './adminContext';
import axios from 'axios';
import { GET_USERS, DELETE_USER, USER_ERROR, RESET_PASSWORD, ADMIN_CLEAR_ERROR, CONVERT_ADMIN } from '../types';

const AdminState = (props) => {
    const initialState = {
        users: null,
        error: null,
        success: null
    }

    const [state, dispatch] = useReducer(AdminReducer, initialState);

    // METHODS

    // @loc     Users.js
    // @desc    get all users
    // @param   ()
    const getUsers = async () => {
        try {
            const res = await axios.get('http://localhost:8080/getusers');
            const data = res.data.filter(user => user.role !== 'admin')
            dispatch({
                type: GET_USERS,
                payload: data
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
    // @param   (object)
    const resetPassword = async (resetdata) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                'adminToken': 'baymaxFTW'
            }
        }

        const data = {
            'staff_id': resetdata.staff_id
        }

        try {
            const res = await axios.post('http://localhost:8080/resetpassword', data, config);

            dispatch({
                type: RESET_PASSWORD,
                payload: `${resetdata.username} password successfully reset`
            })
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response.data.message
            })
        }
    }

    // @loc     UserItem.js
    // @desc    convert user to admin
    // @param   (object)
    const convertAdmin = async (convertdata) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                'adminToken': 'baymaxFTW'
            }
        }

        const data = {
            'staff_id': convertdata.staff_id
        }

        try {
            const res = await axios.post('http://localhost:8080/adminconvert', data, config);

            dispatch({
                type: CONVERT_ADMIN,
                payload: { data: res.data, msg: `${convertdata.username} account converted to Admin`}
            })
        } catch (err) {
            dispatch({
                type: USER_ERROR,
                payload: err.response.data.message
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
            success: state.success,
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
