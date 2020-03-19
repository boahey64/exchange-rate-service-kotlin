import axios from "axios";

export function fetchExchangeRateHistory(date) {
    // https://javascript.info/async-await
    // https://medium.com/javascript-in-plain-english/async-await-javascript-5038668ec6eb
    return async dispatch => {
        dispatch(fetchExchangeRateHistoryBegin());

        // const convertedDate = date.replace('-', '/');
        // console.log("convertedDate: " + convertedDate);
        const currencyServiceUrl = "http://localhost:8080/api/exchange-rate/history/local/" + date;

        const response = await axios(currencyServiceUrl);
        console.log("history response: " + response.data);
        dispatch(fetchExchangeRateHistorySuccess(response.data));
        return response.data;
    };
}

export const FETCH_EXCHANGE_RATE_HISTORY_BEGIN   = 'FETCH_EXCHANGE_RATE_HISTORY_BEGIN';
export const FETCH_EXCHANGE_RATE_HISTORY_SUCCESS = 'FETCH_EXCHANGE_RATE_HISTORY_SUCCESS';
export const FETCH_EXCHANGE_RATE_HISTORY_FAILURE = 'FETCH_EXCHANGE_RATE_HISTORY_FAILURE';

export const fetchExchangeRateHistoryBegin = () => ({
    type: FETCH_EXCHANGE_RATE_HISTORY_BEGIN
});

export const fetchExchangeRateHistorySuccess = items => ({
    type: FETCH_EXCHANGE_RATE_HISTORY_SUCCESS,
    payload: { items }
});

export const fetchExchangeRateHistoryFailure = error => ({
    type: FETCH_EXCHANGE_RATE_HISTORY_FAILURE,
    payload: { error }
});
