import React, { useReducer } from 'react';
import HistContext from './histContext';
import HistReducer from './histReducer';
import { HISTORY_LOADED } from '../types';
import axios from 'axios';

const HistState = (props) => {
    const initialState = {
        historyData: [
            {
                msuID: 1,
                username: 'joe',
                dateGenerated: '12 August 2020'
            },
            {
                msuID: 2,
                username: 'admin',
                dateGenerated: '13 August 2020'
            },
            {
                msuID: 3,
                username: 'joe',
                dateGenerated: '14 August 2020'
            }
        ],
        historyItems: [
            {
                msuID: 1,
                argoID: 16073,
                utID: 'CY19Q4TOOL19',
                productName: 'AUDI A4',
                cycleTime: '92',
                mrpDate: '12/12/20'
            },
            {
                msuID: 1,
                argoID: 16042,
                utID: 'CY19Q4TOOL19',
                productName: 'MERZ B4',
                cycleTime: '62',
                mrpDate: '04/07/21'
            },
            {
                msuID: 3,
                argoID: 11111,
                utID: 'CY19Q4TOOL19',
                productName: 'XXX B4',
                cycleTime: '92',
                mrpDate: '12/12/20'
            },
        ]
        // historyData: null,
        // historyItems: null
    };

    const [state, dispatch] = useReducer(HistReducer, initialState);

    //all methods here

    const loadHistory = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/gethistory`);
            // console.log(res);
            dispatch({
                type: HISTORY_LOADED
            })
        } catch (err) {
            // console.log(err.response)
            // dispatch({
            //     type: AUTH_ERROR
            // })
        }
    }

    return <HistContext.Provider
        value={{
            historyData: state.historyData,
            historyItems: state.historyItems,
            loadHistory
        }}>
        {props.children}
    </HistContext.Provider>
}

export default HistState