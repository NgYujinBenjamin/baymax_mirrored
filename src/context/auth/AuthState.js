import React, { useReducer } from 'react';
import AuthContext from './authContext';
import AuthReducer from './authReducer';
import { LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT, USER_LOADED, CLEAR_ERRORS, AUTH_ERROR, NEW_PASSWORD } from '../types';
import axios from 'axios';
import setAuthToken from '../../utils/setAuthToken';
// const { ipcRenderer } = window.require("electron");

const AuthState = (props) => {
    const initialState = {
        token: localStorage.getItem('token'),
        isAuthenticated: null,
        user: null,
        error: null,
        loading: true
    };

    const [state, dispatch] = useReducer(AuthReducer, initialState);

    //all methods here
    
    //load user
    const loadUser = async () => {
        if(localStorage.token){
            setAuthToken(localStorage.token);
        }

        try {
            const res = await axios.get(`http://localhost:8080/verify?token=${localStorage.token}`);
            console.log(res.data);
            dispatch({
                type: USER_LOADED,
                payload: res.data
            })
        } catch (err) {
            dispatch({
                type: AUTH_ERROR
            })
        }

        // ipcRenderer.send('loadUser:send', JSON.stringify({ token: state.token }));
        // ipcRenderer.once('loadUser:received', (event, res) => {
        //     const response = JSON.parse(res);
            
        //     if(response.type === 'SUCCESS'){
        //         dispatch({
        //             type: USER_LOADED,
        //             payload: response.user
        //         })
        //     } else if(response.type === 'ERROR'){
        //         dispatch({
        //             type: AUTH_ERROR
        //         })
        //     }
        // })
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
            console.log(res);
            dispatch({
                type: LOGIN_SUCCESS,
                payload: res.data.token
            });
            loadUser();
        } catch (err) {
            console.log(err);
            dispatch({
                type: LOGIN_FAIL,
                payload: err
            });
        }

        // ipcRenderer.send('login:send', JSON.stringify(formData));
        // ipcRenderer.once('login:received', (event, res) => {
        //     const response = JSON.parse(res);
            
        //     if(response.type === 'ERROR'){
        //         dispatch({
        //             type: LOGIN_FAIL,
        //             payload: response.data.msg
        //         })
        //     } else if(response.type === 'SUCCESS') {
        //         dispatch({
        //             type: LOGIN_SUCCESS,
        //             payload: response.data
        //         })
        //         loadUser();
        //     }
        // })
    } 

    //logout user
    const logout = () => dispatch({ type: LOGOUT })

    //clear errors
    const clearErrors = () => dispatch({ type: CLEAR_ERRORS })

    // update password 
    const updatePwd = (newpwd) => {
        console.log(newpwd);
        // const config = {
        //     headers: {
        //         'Content-Type': 'application/json'
        //     }
        // }

        // try {
        //     await axios.post('localhost:8080', newpwd, config);
        //     console.log(newpwd)
        //     dispatch({
        //         type: NEW_PASSWORD,
        //     })
        // } catch (err) {
            
        // }
    }

    return <AuthContext.Provider
        value={{
            token: state.token,
            isAuthenticated: state.isAuthenticated,
            user: state.user,
            error: state.error,
            loading: state.loading,
            loadUser,
            login,
            logout,
            clearErrors,
            updatePwd
        }}>
        {props.children}
    </AuthContext.Provider>
}

export default AuthState
