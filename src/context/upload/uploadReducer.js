import { SET_BASELINE, UPDATE_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, EXPORT_SCHEDULE, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO, SET_STEPS, UPDATE_POST_RESULT, UPDATE_QUARTER, UPDATE_DATA, UPDATE_SAVE, UPDATE_POST_RESULT_1, UPDATE_POST_RESULT_2 } from '../types';

export default (state, action) => {
    switch(action.type) {
        case SAVE_RESULT:
        case EXPORT_RESULT:
        case EXPORT_SCHEDULE:
            return {
                ...state,
                loading: false
            }
        case UPDATE_DATA:
            return {
                ...state,
                currentData: [...state.currentData, action.payload]
            }
        case UPDATE_QUARTER:
            return {
                ...state,
                currentQuarter: action.payload
            }
        case UPDATE_POST_RESULT_1:
        case UPDATE_POST_RESULT_2:
            return {
                ...state,
                scheduletest: action.payload
            }
        case UPDATE_POST_RESULT:
            return {
                ...state,
                postResult: action.payload
            }
        case UPDATE_SAVE:
            return {
                ...state,
                saved: action.payload
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
        case SET_STEPS:
            return {
                ...state,
                stepcount: action.payload
            }
        case UPDATE_BASELINE:
            return {
                ...state,
                baseline: action.payload
            }
        case SET_BASELINE:
            return {
                ...state,
                baseline: action.payload,
                loading: false
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
                error: null,
                stepcount: 1
            }
        case CLEAR_PRERESULT:
            return {
                ...state,
                schedule: null,
                bays: '',
                loading: false,
                error: null,
                stepcount: 1
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
                error: null,
                stepcount: 0
            }
        default:
            return state;
    }
}