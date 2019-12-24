import { SET_BASELINE, SET_STATUS_BASELINE } from '../types';

export default (state, action) => {
    switch(action.type) {
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