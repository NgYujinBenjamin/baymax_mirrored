import { GET_USERS, DELETE_USER, USER_ERROR, RESET_PASSWORD, ADMIN_CLEAR_ERROR, CONVERT_ADMIN } from '../types';

export default (state, action) => {
    switch(action.type){
        case GET_USERS:
            return {
                ...state,
                users: action.payload,
                loading: false
            }
        case DELETE_USER:
            return {
                ...state,
                users: state.users.filter(user => user.staff_id !== action.payload.id),
                success: action.payload.msg,
                loading: false
            }
        case CONVERT_ADMIN:
            return {
                ...state,
                users: state.users.map(user => user.staff_id === action.payload.data.staff_id ? action.payload.data : user),
                success: action.payload.msg,
                loading: false
            }
        case USER_ERROR:
            return {
                ...state,
                error: action.payload,
                loading: false
            }
        case ADMIN_CLEAR_ERROR:
            return {
                ...state,
                error: null,
                success: null,
                loading: false
            }
        case RESET_PASSWORD:
            return {
                ...state,
                success: action.payload,
                loading: false
            }
        default:
            return state
    }
}