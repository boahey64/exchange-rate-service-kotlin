import axios from "axios";

export const INCREMENT = "INCREMENT";
export const DECREMENT = "DECREMENT";
export const RESET = "RESET";


export function increment() {
    return { type: INCREMENT };
}

export const decrement = () => ({ type: DECREMENT });

export const reset = () => ( {type: RESET });

export function fetchToDos() {
    // https://javascript.info/async-await
    // https://medium.com/javascript-in-plain-english/async-await-javascript-5038668ec6eb
    return async dispatch => {
        dispatch(fetchToDosBegin());

        const response = await axios("http://localhost:18081/todos");

        dispatch(fetchToDosSuccess(response.data));
        return response.data;
    };
}

export const FETCH_TODOS_BEGIN   = 'FETCH_TODOS_BEGIN';
export const FETCH_TODOS_SUCCESS = 'FETCH_TODOS_SUCCESS';
export const FETCH_TODOS_FAILURE = 'FETCH_TODOS_FAILURE';

export const fetchToDosBegin = () => ({
    type: FETCH_TODOS_BEGIN
});

export const fetchToDosSuccess = items => ({
    type: FETCH_TODOS_SUCCESS,
    payload: { items }
});

export const fetchToDosFailure = error => ({
    type: FETCH_TODOS_FAILURE,
    payload: { error }
});
