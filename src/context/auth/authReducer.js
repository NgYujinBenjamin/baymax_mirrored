import { REGISTER_SUCCESS, REGISTER_FAIL, LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT, USER_LOADED, CLEAR_ERRORS, AUTH_ERROR, NEW_PASSWORD, UPDATE_NAV } from '../types';

export default (state, action) => {
    switch(action.type){
        case USER_LOADED:
            return {
                ...state,
                isAuthenticated: true,
                loading: false,
                user: action.payload
            }
        case REGISTER_SUCCESS:
        case LOGIN_SUCCESS:
            localStorage.setItem('token', action.payload.token)
            return {
                ...state,
                ...action.payload,
                isAuthenticated: true,
                loading: false
            }
        case LOGOUT:
        case AUTH_ERROR:
            localStorage.removeItem('token')
            return {
                ...state,
                token: null,
                isAuthenticated: false,
                user: null,
                loading: false,
                error: null
            }
        case REGISTER_FAIL:
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
        case CLEAR_ERRORS:
            return {
                ...state, 
                error: null
            }
        case UPDATE_NAV:
            return {
                ...state,
                currentNavItem: action.payload
            }
        case NEW_PASSWORD:
        default:
            return state
    }
}