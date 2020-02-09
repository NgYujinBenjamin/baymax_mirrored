import { SET_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO } from '../types';

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
        case UPLOAD_ERROR:
            return {
                ...state,
                error: action.payload,
                loading: false
            }
        case UPLOAD_CLEAR_ERROR:
            return {
                ...state,
                error: null,
                loading: false
            }
        case CLEAR_ALL:
            return {
                ...state,
                schedule: null,
                bays: '',
                loading: false,
                postResult: null,
                scheduleDone: false,
                error: null
            }
        case CLEAR_PRERESULT:
            return {
                ...state,
                schedule: null,
                bays: '',
                loading: false,
                error: null
            }
        case CLEAR_ZERO:
            return {
                ...state,
                baseline: null,
                schedule: null,
                bays: '',
                loading: false,
                postResult: null,
                scheduleDone: false,
                error: null
            }
        default:
            return state;
    }
}