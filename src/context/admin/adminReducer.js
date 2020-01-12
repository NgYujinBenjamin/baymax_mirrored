import { ADD_USER, GET_USERS, DELETE_USER } from '../types';

export default (state, action) => {
    switch(action.type){
        case ADD_USER:
            return {
                ...state,
                users: [action.payload, ...state.users]
            }
        default:
            return state
    }
}