import { SET_BASELINE, SET_STATUS_BASELINE, SET_LINEARIZE, SET_BAYS, CLEAR_PRERESULT } from '../types';

export default (state, action) => {
    switch(action.type) {
        case CLEAR_PRERESULT:
            return {
                ...state,
                linearize: [],
                bays: ''
            }
        case SET_BAYS:
            return {
                ...state,
                bays: action.payload
            }
        case SET_LINEARIZE:
            return {
                ...state,
                linearize: action.payload
            }
        case SET_STATUS_BASELINE:
            return {
                ...state,
                hasBaseline: true
            }
        case SET_BASELINE:
            return {
                ...state,
                baseline: action.payload
            }
        default:
            return state;
    }
}