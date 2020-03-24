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
    };
}

export function fetchValidCurrencies() {
    // https://javascript.info/async-await
    // https://medium.com/javascript-in-plain-english/async-await-javascript-5038668ec6eb
    return async dispatch => {
        dispatch(fetchValidCurrenciesBegin());

        const currencyServiceUrl = "http://localhost:8080/api/exchange-rate/currencies/valid";

        return await axios.get(currencyServiceUrl)
            .then(res => {
                dispatch(fetchValidCurrenciesSuccess(res.data));
            })
            .catch(err => {
                dispatch(fetchValidCurrenciesFailure(err));
            });
    };
}

export const FETCH_EXCHANGE_RATE_BEGIN   = 'FETCH_EXCHANGE_RATE_BEGIN';
export const FETCH_EXCHANGE_RATE_SUCCESS = 'FETCH_EXCHANGE_RATE_SUCCESS';
export const FETCH_EXCHANGE_RATE_FAILURE = 'FETCH_EXCHANGE_RATE_FAILURE';
export const FETCH_VALID_CURRENCIES_BEGIN   = 'FETCH_VALID_CURRENCIES_BEGIN';
export const FETCH_VALID_CURRENCIES_SUCCESS = 'FETCH_VALID_CURRENCIES_SUCCESS';
export const FETCH_VALID_CURRENCIES_FAILURE = 'FETCH_VALID_CURRENCIES_FAILURE';

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

export const fetchValidCurrenciesBegin = () => ({
    type: FETCH_VALID_CURRENCIES_BEGIN
});

export const fetchValidCurrenciesSuccess = validCurrencies => ({
    type: FETCH_VALID_CURRENCIES_SUCCESS,
    payload: { validCurrencies }
});

export const fetchValidCurrenciesFailure = error => ({
    type: FETCH_VALID_CURRENCIES_FAILURE,
    payload: { error }
});
