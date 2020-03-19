import {
    fetchExchangeRate,
    FETCH_EXCHANGE_RATE_SUCCESS,
    FETCH_EXCHANGE_RATE_BEGIN
} from "./action";

import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import moxios from 'moxios';
import expect from 'expect';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

describe("actions", () => {
    beforeEach(function () {
        moxios.install();
    });

    afterEach(function () {
        moxios.uninstall();
    });

    // https://medium.com/@netxm/test-async-redux-actions-jest-e703add2cf91
    it('triggers exchage rate fetch', () => {
        const exchangeRate =
            {
                "currentRate": 1.345,
                "avarageRate": 1.321,
                "trend": "undefined"
            };

        moxios.wait(() => {
            const request = moxios.requests.mostRecent();

            request.respondWith({
                status: 200,
                response: exchangeRate
            });
        });

        const expectedActions = [
            { type: FETCH_EXCHANGE_RATE_BEGIN },
            { type: FETCH_EXCHANGE_RATE_SUCCESS, payload: { item: exchangeRate } },
        ];

        // if there is no current state > initial state
        const store = mockStore();
        const baseCurrency = 'EUR';
        const targetCurrency = 'CAD';
        const date = '2020-01-19';
        return store.dispatch(fetchExchangeRate(date, baseCurrency, targetCurrency)).then(() => {
            // return of async actions
            expect(store.getActions()).toEqual(expectedActions);
        });
    });
});
