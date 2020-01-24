import { SET_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, CLEAR_ALL, SAVE_RESULT } from '../types';

export default (state, action) => {
    switch(action.type) {
        case SAVE_RESULT:
        case EXPORT_RESULT:
            return {
                ...state,
                loading: false
            }
        case CREATE_RESULT:
            return {
                ...state,
                postResult: action.payload,
                loading: false,
                scheduleDone: true
            }
        case SET_BAYS:
            return {
                ...state,
                bays: action.payload,
                loading: false
            }
        case SET_SCHEDULE:
        case UPDATE_SCHEDULE:
            return {
                ...state,
                schedule: action.payload,
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
        case CLEAR_ALL:
            return {
                ...state,
                schedule: null,
                bays: '',
                loading: false,
                postResult: null,
                scheduleDone: false
            }
        case CLEAR_PRERESULT:
            return {
                ...state,
                schedule: null,
                bays: '',
                loading: false
            }
        default:
            return state;
    }
}