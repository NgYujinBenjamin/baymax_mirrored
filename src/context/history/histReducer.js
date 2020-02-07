import { HISTORY_LOADED } from '../types';

export default (state, action) => {
    switch(action.type){
        case HISTORY_LOADED:
            return {
                ...state
            }
        default:
            return state
    }
}