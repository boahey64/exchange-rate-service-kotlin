import { reducer } from "./reducer";

describe("reducer", () => {
    const currentState = {
        count: 0
    };

    it("set the initial counter", () => {
        const expectedState = {
            count: 0,
            error: null,
            items: [],
            validCurrencies: [],
            loading: false
        };
        expect(reducer(undefined, {})).toEqual(expectedState);
    });

    it("increment the counter", () => {
        expect(reducer(currentState, { type: "INCREMENT" })).toEqual({ count: 1 });
    });

    it("decrement the counter", () => {
        expect(reducer(currentState, { type: "DECREMENT" })).toEqual({ count: -1 });
    });

    it("reset the counter", () => {
        expect(reducer(currentState, { type: "RESET" })).toEqual({ count: 0 });
    });
});