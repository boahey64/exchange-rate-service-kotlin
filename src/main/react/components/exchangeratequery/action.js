import axios from "axios";

export function fetchExchangeRate(date, baseCurrency, targetCurrency) {
    // https://javascript.info/async-await
    // https://medium.com/javascript-in-plain-english/async-await-javascript-5038668ec6eb
    return async dispatch => {
        dispatch(fetchExchangeRateBegin());

        const currencyServiceUrl = "http://localhost:8080/api/exchange-rate/"+date+"/"+baseCurrency+"/"+targetCurrency;

        return await axios.get(currencyServiceUrl)
            .then(res => {
                dispatch(fetchExchangeRateSuccess(res.data));
            })
            .catch(err => {
                dispatch(fetchExchangeRateFailure(err));
            });
        // console.log("response: " + response.data);
        // dispatch(fetchExchangeRateSuccess(response.data));
        // return response.data;
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
