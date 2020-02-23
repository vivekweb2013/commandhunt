import React, { Component } from 'react';
import { Route, Switch } from "react-router-dom";
import './Content.scss';
import Finder from './Finder';
import Builder from './Builder';

class Content extends Component {
    render() {
        return (
            <div className="main-content">
                <Switch>
                    <Route exact path="/" component={Finder} />
                    <Route path="/command/build/:commandName" component={Builder} />
                </Switch>
            </div>
        )
    }
}

export default Content;