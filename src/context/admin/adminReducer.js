import { ADD_USER, GET_USERS, DELETE_USER, USER_ERROR, RESET_PASSWORD, ADMIN_CLEAR_ERROR } from '../types';

export default (state, action) => {
    switch(action.type){
        case GET_USERS:
            return {
                ...state,
                users: action.payload,
                loading: false
            }
        case ADD_USER:
            return {
                ...state,
                users: [action.payload, ...state.users],
                loading: false
            }
        case DELETE_USER:
            return {
                ...state,
                users: state.users.filter(user => user.staff_id !== action.payload),
                loading: false
            }
        case USER_ERROR:
            return {
                ...state,
                error: action.payload
            }
        case ADMIN_CLEAR_ERROR:
            return {
                ...state,
                error: null
            }
        case RESET_PASSWORD:
        default:
            return state
    }
}