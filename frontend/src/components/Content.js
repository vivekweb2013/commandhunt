import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Finder from './Finder';
import Builder from './Builder';
import Login from './Login';
import UserCommands from './UserCommands';
import './Content.scss';
import { Redirect, withRouter } from 'react-router';
import SignUp from './SignUp';
import { connect } from 'react-redux';
import PageNotFound from './common/PageNotFound';

const ProtectedRoute = ({ component: Component, isLoggedIn, path, ...rest }) =>
    <Route path={path} {...rest} render={props => isLoggedIn ?
        <Component {...props} /> : <Redirect to={{ pathname: "/login", state: { referer: props.location.pathname + props.location.search } }} />} />

class Content extends Component {
    render() {
        return (
            <div className="main-content">
                <Switch>
                    <Route exact path="/" key={this.props.history.location.search} component={Finder} />
                    <Route path="/login" component={Login} />
                    <Route path="/signup" component={SignUp} />
                    <Route path="/command/build/:commandName" key={this.props.history.location.search} component={Builder} />
                    <Route path="/command/view/:commandName" key={this.props.history.location.search} component={Builder} />
                    <ProtectedRoute isLoggedIn={!!this.props.user} path="/command/user-commands" key={this.props.history.location.search} component={UserCommands} />
                    <Route component={PageNotFound} />
                </Switch>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.authReducer.user
    }
}

export default withRouter(connect(mapStateToProps)(Content));
