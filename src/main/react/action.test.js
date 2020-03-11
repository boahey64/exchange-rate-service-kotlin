import {
    increment,
    decrement,
    reset,
    fetchToDos,
    FETCH_TODOS_SUCCESS,
    FETCH_TODOS_BEGIN
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

    it("triggers increment", () => {
        expect(increment()).toEqual({ type: "INCREMENT" });
    });

    it("triggers decrement", () => {
        expect(decrement()).toEqual({ type: "DECREMENT" });
    });

    it("triggers reset", () => {
        expect(reset()).toEqual({ type: "RESET" });
    });

    // https://medium.com/@netxm/test-async-redux-actions-jest-e703add2cf91
    it('triggers todo fetch', () => {
        const todos = [
            {
                "userId": 1,
                "id": 1,
                "title": "delectus aut autem",
                "completed": false
            },
            {
                "userId": 1,
                "id": 2,
                "title": "quis ut nam facilis et officia qui",
                "completed": false
            }];

        moxios.wait(() => {
            const request = moxios.requests.mostRecent();

            request.respondWith({
                status: 200,
                response: todos
            });
        });

        const expectedActions = [
            { type: FETCH_TODOS_BEGIN },
            { type: FETCH_TODOS_SUCCESS, payload: { items: todos } },
        ];

        // if there is no current state > initial state
        const store = mockStore();

        return store.dispatch(fetchToDos()).then(() => {
            // return of async actions
            expect(store.getActions()).toEqual(expectedActions);
        });
    });
});
