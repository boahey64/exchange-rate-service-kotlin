import {
    FETCH_TODOS_BEGIN,
    FETCH_TODOS_SUCCESS,
    FETCH_TODOS_FAILURE,
    FETCH_EXCHANGE_RATE_BEGIN,
    FETCH_EXCHANGE_RATE_SUCCESS,
    FETCH_EXCHANGE_RATE_FAILURE
} from './action';

const initialState = {
    count: 0,
    items: [],
    item: {},
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
        case FETCH_TODOS_BEGIN:
            return {
                ...state,
                loading: true,
                error: null
            };

        case FETCH_TODOS_SUCCESS:
            return {
                ...state,
                loading: false,
                items: action.payload.items
            };

        case FETCH_TODOS_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload.error,
                items: []
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
        default:
            return state;
    }
}