import axios from "axios";

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

/*------------------- fetch exchange rate ----------------------*/

export function fetchExchangeRate() {
    // https://javascript.info/async-await
    // https://medium.com/javascript-in-plain-english/async-await-javascript-5038668ec6eb
    return async dispatch => {
        dispatch(fetchExchangeRateBegin());

        const response = await axios("http://localhost:8080/api/exchange-rate/2019-12-31/CAD/BRL");
        console.log("response: " + response.data.currentRate);
        dispatch(fetchExchangeRateSuccess(response.data));
        return response.data;
    };
}

export const FETCH_EXCHANGE_RATE_BEGIN   = 'FETCH_EXCHANGE_RATE_BEGIN';
export const FETCH_EXCHANGE_RATE_SUCCESS = 'FETCH_EXCHANGE_RATE_SUCCESS';
export const FETCH_EXCHANGE_RATE_FAILURE = 'FETCH_EXCHANGE_RATE_FAILURE';

export const fetchExchangeRateBegin = () => ({
    type: FETCH_EXCHANGE_RATE_BEGIN
});

export const fetchExchangeRateSuccess = item => ({
    type: FETCH_EXCHANGE_RATE_SUCCESS,
    payload: { item }
});

export const fetchExchangeRateFailure = error => ({
    type: FETCH_EXCHANGE_RATE_FAILURE,
    payload: { error }
});
