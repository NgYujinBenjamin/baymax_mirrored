import React, { useReducer } from 'react';
import AuthContext from './authContext';
import AuthReducer from './authReducer';
import { REGISTER_SUCCESS, REGISTER_FAIL, LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT, USER_LOADED, CLEAR_ERRORS, AUTH_ERROR, NEW_PASSWORD, UPDATE_NAV, CHANGE_PWD_FAIL } from '../types';
import axios from 'axios';
import setAuthToken from '../../utils/setAuthToken';

const AuthState = (props) => {
    const initialState = {
        token: localStorage.getItem('token'),
        isAuthenticated: null,
        user: null,
        error: null,
        loading: true,
        currentNavItem: -1
    };

    const [state, dispatch] = useReducer(AuthReducer, initialState);

    //all methods here
    
    //load user
    const loadUser = async () => {
        if(localStorage.token){
            setAuthToken(localStorage.token);
        }

        try {
            const res = await axios.get(`http://localhost:8080/verify`);
            dispatch({
                type: USER_LOADED,
                payload: res.data.data
            })
        } catch (err) {
            dispatch({
                type: AUTH_ERROR
            })
        }
    }

    //login user
    const login = async (formData) => {
        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/login', formData, config);
            // console.log(res);
            dispatch({
                type: LOGIN_SUCCESS,
                payload: res.data
            });
            loadUser();
        } catch (err) {
            // console.log(err.response.data);
            dispatch({
                type: LOGIN_FAIL,
                payload: err.response.data.message
            });
        }
    }
    
    //register user
    const register = async (user) => {
        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        user.firstname = user.firstname.charAt(0).toUpperCase() + user.firstname.slice(1);
        user.lastname = user.lastname.charAt(0).toUpperCase() + user.lastname.slice(1);

        try {
            const res = await axios.post('http://localhost:8080/register', user, config);
            // console.log(res)
            dispatch({
                type: REGISTER_SUCCESS,
                payload: res.data
            });
            loadUser();
        } catch (err) {
            // console.log(err.response.data)
            dispatch({
                type: REGISTER_FAIL,
                payload: err.response.data.message
            })
        }
    }

    //logout user
    const logout = () => dispatch({ type: LOGOUT })

    //clear errors
    const clearErrors = () => dispatch({ type: CLEAR_ERRORS })

    // update password 
    const updatePwd = async (username, oldpwd, newpwd) => {
        const pwd = {'username': username, 'oldpassword': oldpwd, 'newpassword': newpwd};

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            await axios.post('http://localhost:8080/changepassword', pwd, config);
            dispatch({
                type: NEW_PASSWORD
            })
        } catch (err) {
            dispatch({
                type: CHANGE_PWD_FAIL,
                payload: err.response.data.message
            })
        }
    }

    //update nav item
    const updateNavItem = (index) => {
        dispatch({
            type: UPDATE_NAV,
            payload: index
        })
    }

    return <AuthContext.Provider
        value={{
            token: state.token,
            isAuthenticated: state.isAuthenticated,
            user: state.user,
            error: state.error,
            loading: state.loading,
            currentNavItem: state.currentNavItem,
            loadUser,
            login,
            logout,
            register,
            clearErrors,
            updatePwd,
            updateNavItem
        }}>
        {props.children}
    </AuthContext.Provider>
}

export default AuthState
