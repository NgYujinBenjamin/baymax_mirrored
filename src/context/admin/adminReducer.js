import { ADD_USER, GET_USERS, DELETE_USER, USER_ERROR } from '../types';

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
        default:
            return state
    }
}