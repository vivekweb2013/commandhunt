import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";

import { createStore, applyMiddleware, compose } from "redux";
import { Provider } from "react-redux";
import thunk from "redux-thunk";
import reducer from "./reducers/index";
import App from "./App";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(reducer, composeEnhancers(applyMiddleware(thunk)));

it("renders without crashing", () => {
  const div = document.createElement("div");
  ReactDOM.render(<Provider store={store}><BrowserRouter><App /></BrowserRouter></Provider>, div);
  ReactDOM.unmountComponentAtNode(div);
});
