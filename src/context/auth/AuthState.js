import React, { useReducer } from 'react';
import AuthContext from './authContext';
import AuthReducer from './authReducer';
import { LOGIN_SUCCESS, LOGIN_FAIL, AUTH_ERROR, LOGOUT, USER_LOADED, CLEAR_ERRORS } from '../types';

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
    const loadUser = () => console.log();

    //login user
    const login = () => console.log();

    //logout user
    const logout = () => console.log();

    //clear errors
    const clearErrors = () => console.log();

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
            clearErrors
        }}>
        {props.children}
    </AuthContext.Provider>
}

export default AuthState
