import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Provider } from 'react-redux';
import { reducer } from "./reducer";
import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import ToDo from "./ToDo";
import ExchangeRateQuery from "./ExchangeRateQuery";


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
                <h2>Page Title</h2>
                <p>
                    Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
                </p>
            </div>
            <div class="sub-content">
                <h3>Subtitle</h3>
                <ExchangeRateQuery/>
            </div>
        </main>
        <aside>
            <div>
                <h3>Sidebar Content0</h3>
                <ToDo/>
            </div>
            <div>
                <h3>Sidebar Content1</h3>
                <ul>
                    <li>sequi totam</li>
                    <li>praesentium perferendis</li>
                    <li>nihil aliquid</li>
                    <li>praesentium odio illo</li>
                </ul>
            </div>
            <div>
                <h3>Sidebar Content2</h3>
                <ul>
                    <li>sequi totam</li>
                    <li>praesentium perferendis</li>
                    <li>nihil aliquid</li>
                    <li>praesentium odio illo</li>
                </ul>
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
