import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Provider } from 'react-redux';
import { reducer } from "./reducer";
import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import ToDo from "./ToDo";
import ExchangeRateQueryForm from "./components/exchangeratequery/ExchangeRateQueryForm";
import ExchangeRateResult from "./components/exchangeratequery/ExchangeRateResult";


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
        <header class="i-header">
            <h1 class="headline">Logo</h1>
            <nav>
                <ul>
                    <li>Home</li>
                    <li>About Us</li>
                    <li>Contact Us</li>
                </ul>
            </nav>
        </header>
        <main class="main">
            <div>
                <h2>Query Parameter</h2>
                <p>
                    <ExchangeRateQueryForm/>
                </p>
            </div>
            <div class="sub-content">
                <h3>Result</h3>
                <ExchangeRateResult/>
            </div>
        </main>
        <aside>
            <div>
                <h3>Result</h3>
                <ExchangeRateResult/>
            </div>
            <div>
                <h3>History</h3>
                <ExchangeRateResult/>
            </div>
            <div>
                <h3>Sidebar Content2</h3>
                <ToDo/>
            </div>
        </aside>
        <footer>
            <div class="footer-content">
                <nav>
                    <ul>
                        <li>Home</li>
                        <li>About Us</li>
                        <li>Contact Us</li>
                    </ul>
                </nav>
            </div>
        </footer>
    </Provider>
  );
}

export default App;
