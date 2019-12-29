import { LOGIN_SUCCESS, LOGIN_FAIL, AUTH_ERROR, LOGOUT, USER_LOADED, CLEAR_ERRORS, SET_AUTH_LOADING } from '../types';

export default (state, action) => {
    switch(action.type){
        case USER_LOADED:
            return {
                ...state,
                isAuthenticated: true,
                loading: false,
                user: action.payload
            }
        case LOGIN_SUCCESS:
            localStorage.setItem('token', action.payload.token)
            return {
                ...state,
                ...action.payload,
                isAuthenticated: true,
                loading: false
            }
        case LOGOUT:
        case LOGIN_FAIL:
            localStorage.removeItem('token')
            return {
                ...state,
                token: null,
                isAuthenticated: false,
                user: null,
                loading: false,
                error: action.payload
            }
        case SET_AUTH_LOADING:
            return {
                ...state,
                loading: true
            }
        default:
            return state
    }
}