import {
    fetchExchangeRateHistory,
    FETCH_EXCHANGE_RATE_HISTORY_SUCCESS,
    FETCH_EXCHANGE_RATE_HISTORY_BEGIN
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
    it('triggers history fetch', () => {
        const history = [
            {
                "id": 1,
                "dateString": "2020-01-13",
                "baseCurrency": "EUR",
                "targetCurrency": "USD",
                "currentRate": 1.345,
                "avarageRate": 1.321
            },
            {
                "id": 2,
                "dateString": "2020-01-13",
                "baseCurrency": "CAD",
                "targetCurrency": "USD",
                "currentRate": 1.123,
                "avarageRate": 1.100
            }];

        moxios.wait(() => {
            const request = moxios.requests.mostRecent();

            request.respondWith({
                status: 200,
                response: history
            });
        });

        const expectedActions = [
            { type: FETCH_EXCHANGE_RATE_HISTORY_BEGIN },
            { type: FETCH_EXCHANGE_RATE_HISTORY_SUCCESS, payload: { items: history } },
        ];

        // if there is no current state > initial state
        const store = mockStore();
        const historyDate = 'daily/2020-01-19'

        return store.dispatch(fetchExchangeRateHistory(historyDate)).then(() => {
            // return of async actions
            expect(store.getActions()).toEqual(expectedActions);
        });
    });
});
