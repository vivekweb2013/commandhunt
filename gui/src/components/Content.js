import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import './Content.scss';
import Finder from './Finder';
import Builder from './Builder';
import Login from './Login';

class Content extends Component {
    render() {
        return (
            <div className="main-content">
                <Switch>
                    <Route exact path="/" component={Finder} />
                    <Route path="/login" component={Login} />
                    <Route path="/command/build/:commandName" component={Builder} />
                </Switch>
            </div>
        )
    }
}

export default Content;
