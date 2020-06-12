import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Provider } from 'react-redux';
import { reducer } from "./reducer";
import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import ExchangeRateQueryForm from "./components/exchangeratequery/ExchangeRateQueryForm";
import ExchangeRateResult from "./components/exchangeratequery/ExchangeRateResult";
import HistoryResult from "./components/history/HistoryResult";
import HistoryQueryForm from "./components/history/HistoryQueryForm";
import activeFeatures from "./togglz";
import { FeatureToggles} from "@paralleldrive/react-feature-toggles";

const features = activeFeatures();

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
        <header className="i-header">
            <h1 className="headline">Logo</h1>
            <nav>
                <ul>
                    <li>Home</li>
                    <li>About Us</li>
                    <li>Contact Us</li>
                </ul>
            </nav>
        </header>
        <main className="main">
            <div>
                <h2>Query Parameter</h2>
                <ExchangeRateQueryForm/>
            </div>
            <div className="sub-content">
                <h3>Result</h3>
                <ExchangeRateResult/>
            </div>
        </main>
        <aside>
            <div>
                <h3>History Query</h3>
                <HistoryQueryForm/>
            </div>
            <div>
                <h3>History Result</h3>
                <HistoryResult/>
            </div>
        </aside>
        <FeatureToggles features={features}>

            <footer>
                <div className="footer-content">
                    <nav>
                        <ul>
                            <li>Home</li>
                            <li>About Us</li>
                            <li>Contact Us</li>
                        </ul>
                    </nav>
                </div>
            </footer>
        </FeatureToggles>
    </Provider>
  );
}

export default App;
