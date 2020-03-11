import {
    FETCH_TODOS_BEGIN,
    FETCH_TODOS_SUCCESS,
    FETCH_TODOS_FAILURE
} from './action';

const initialState = {
    count: 0,
    items: [],
    loading: false,
    error: null
};

export function reducer(state = initialState, action) {
    // console.log('reducer', state, action);

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
        default:
            return state;
    }
}