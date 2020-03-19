
import {
    FETCH_EXCHANGE_RATE_BEGIN,
    FETCH_EXCHANGE_RATE_SUCCESS,
    FETCH_EXCHANGE_RATE_FAILURE,
} from './components/exchangeratequery/action';

import {
    FETCH_EXCHANGE_RATE_HISTORY_BEGIN,
    FETCH_EXCHANGE_RATE_HISTORY_SUCCESS,
    FETCH_EXCHANGE_RATE_HISTORY_FAILURE,
} from './components/history/action';

const initialState = {
    count: 0,
    items: [],
    historyQuery: {
        date: 'monthly/2020/01',
    },
    item: {},
    query: {
        baseCurrency: 'EUR',
        targetCurrency: 'USD',
        date: '2020-01-24',
    },
    loading: false,
    error: null
};

export function reducer(state = initialState, action) {
    console.log('reducer', state, action);

    switch(action.type) {
        case "INCREMENT":
            return {
                ...state,
                count: state.count + 1
            };
        case "DECREMENT":
            return {
                ...state,
                count: state.count - 1
            };
        case "RESET":
            return {
                ...state,
                count: 0
            };
        case FETCH_EXCHANGE_RATE_BEGIN:
            return {
                ...state,
                loading: true,
                error: null
            };

        case FETCH_EXCHANGE_RATE_SUCCESS:
            return {
                ...state,
                loading: false,
                item: action.payload.item
            };

        case FETCH_EXCHANGE_RATE_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload.error,
                item: {}
            };
        case FETCH_EXCHANGE_RATE_HISTORY_BEGIN:
            return {
                ...state,
                loading: true,
                error: null
            };

        case FETCH_EXCHANGE_RATE_HISTORY_SUCCESS:
            return {
                ...state,
                loading: false,
                items: action.payload.items
            };

        case FETCH_EXCHANGE_RATE_HISTORY_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload.error,
                items: []
            };
        default:
            return state;
    }
}