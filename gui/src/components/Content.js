import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Finder from './Finder';
import Builder from './Builder';
import Login from './Login';
import UserCommands from './UserCommands';
import './Content.scss';
import SignUp from './SignUp';

class Content extends Component {
    render() {
        return (
            <div className="main-content">
                <Switch>
                    <Route exact path="/" component={Finder} />
                    <Route path="/login" component={Login} />
                    <Route path="/signup" component={SignUp} />
                    <Route path="/command/build/:commandName" component={Builder} />
                    <Route path="/command/user-commands" component={UserCommands} />
                </Switch>
            </div>
        )
    }
}

export default Content;
