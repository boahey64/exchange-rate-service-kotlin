import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Provider } from 'react-redux';
import { reducer } from "./reducer";
import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import ToDo from "./ToDo";

// Redux DevTools setup: https://github.com/zalmoxisus/redux-devtools-extension
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const state = createStore(
    reducer,
    composeEnhancers(
        applyMiddleware(thunk)
    )
);

function App() {
  return (
    <Provider store={state}>
        <div className="App">
            <section>
                <img src={logo} className="App-logo" alt="logo" />
                <ToDo/>
            </section>
         </div>
    </Provider>
  );
}

export default App;
