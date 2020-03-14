import { SET_BASELINE, UPDATE_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, EXPORT_SCHEDULE, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO, SET_STEPS, UPDATE_POST_RESULT, UPDATE_QUARTER, UPDATE_DATA, UPDATE_SAVE, UPDATE_POST_RESULT_FORMAT, UPDATE_RESCHEDULE, RESCHEDULE_POST_RESULT, UPDATE_TABCHECKER, CREATE_RESULT_ERROR, SET_MIN_GAP, SET_MAX_GAP, LOAD_ALL_HISTORY, GET_HISTORY, UPDATE_NEW_MIN_GAP } from '../types';

export default (state, action) => {
    switch(action.type) {
        case EXPORT_RESULT:
        case EXPORT_SCHEDULE:
            return {
                ...state,
                loading: false
            }
        case SAVE_RESULT:
            return {
                ...state,
                histID: action.payload
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
        case UPDATE_POST_RESULT_FORMAT:
        case UPDATE_POST_RESULT:
            return {
                ...state,
                postResultDone: action.payload
            }
        case UPDATE_RESCHEDULE:
            return {
                ...state,
                reschedule: action.payload
            }
        case UPDATE_TABCHECKER:
            return {
                ...state,
                tabUpdate: action.payload
            }
        case UPDATE_SAVE:
            return {
                ...state,
                saveHistory: action.payload
            }
        case RESCHEDULE_POST_RESULT:
            return {
                ...state,
                postResultDone: null,
                postResult: action.payload
            }
        case CREATE_RESULT:
            return {
                ...state,
                postResult: action.payload,
                loading: false,
                scheduleDone: true
            }
        case CREATE_RESULT_ERROR:
            return {
                ...state,
                schedule: null,
                newBaseline: [],
                bays: '',
                minGap: '',
                maxGap: '',
                loading: false,
                error: action.payload,
                stepcount: 1
            }
        case SET_BAYS:
            return {
                ...state,
                bays: action.payload,
                loading: false
            }
        case SET_MIN_GAP:
            return {
                ...state,
                minGap: action.payload,
                loading: false
            }
        case SET_MAX_GAP:
            return {
                ...state,
                maxGap: action.payload,
                loading: false
            }
        case UPDATE_NEW_MIN_GAP:
            return {
                ...state,
                newMinGap: action.payload
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
                newBaseline: action.payload
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
                scheduleDone: false,
                newBaseline: [],
                bays: '',
                minGap: '',
                maxGap: '',
                error: null,
                loading: false,
                postResult: null,
                postResultDone: null,
                currentQuarter: null,
                stepcount: 1
            }
        case CLEAR_PRERESULT:
            return {
                ...state,
                schedule: null,
                newBaseline: [],
                bays: '',
                minGap: '',
                maxGap: '',
                loading: false,
                error: null,
                stepcount: 1
            }
        case CLEAR_ZERO:
            return {
                ...state,
                baseline: null,
                newBaseline: [],
                schedule: null,
                bays: '',
                minGap: '',
                maxGap: '',
                loading: false,
                postResult: null,
                postResultDone: null,
                scheduleDone: false,
                error: null,
                stepcount: 0
            }
        case LOAD_ALL_HISTORY:
            return {
                ...state,
                allHistory: action.payload
            }
        case GET_HISTORY:
            return {
                ...state,
                postResult: action.payload,
                postResultDone: null,
                scheduleDone: false,
                error: null,
                currentQuarter: null
            }
        default:
            return state;
    }
}