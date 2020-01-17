import { SET_BASELINE, SET_LINEARIZE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_LINEARIZE, CREATE_RESULT } from '../types';

export default (state, action) => {
    switch(action.type) {
        case CREATE_RESULT:
            return {
                ...state,
                postResult: action.payload,
                loading: false,
                linearizeDone: true
            }
        case SET_BAYS:
            return {
                ...state,
                bays: action.payload,
                loading: false
            }
        case SET_LINEARIZE:
        case UPDATE_LINEARIZE:
            return {
                ...state,
                linearize: action.payload,
                loading: false
            }
        case SET_BASELINE:
            return {
                ...state,
                baseline: action.payload
            }
        case SET_LOADING:
            return {
                ...state,
                loading: true
            }
        case CLEAR_PRERESULT:
            return {
                ...state,
                linearize: [],
                bays: ''
            }
        default:
            return state;
    }
}