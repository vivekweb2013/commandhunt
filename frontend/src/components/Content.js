import React, { Component } from "react";
import { Route, Switch } from "react-router-dom";
import Finder from "./Finder";
import Builder from "./Builder";
import Login from "./Login";
import UserCommands from "./UserCommands";
import "./Content.scss";
import { Redirect, withRouter } from "react-router";
import SignUp from "./SignUp";
import { connect } from "react-redux";
import PageNotFound from "./common/PageNotFound";

const ProtectedRoute = ({ component: Component, isLoggedIn, path, ...rest }) =>
    <Route path={path} {...rest} render={props => isLoggedIn ?
        <Component {...props} /> : <Redirect to={{ pathname: "/login", state: { referer: props.location.pathname + props.location.search } }} />} />

class Content extends Component {
    render() {
        const { user, location } = this.props;
        const relativeUrl = location.pathname + location.search;
        return <div className="main-content">
            <Switch>
                <Route exact path="/" key={relativeUrl} component={Finder} />
                <Route path="/login" component={Login} />
                <Route path="/signup" component={SignUp} />

                <Route exact path="/public/command/:commandName" key={relativeUrl} component={Builder} />
                <Route path="/public/command/:commandName/:commandId" key={relativeUrl} component={Builder} />
                <ProtectedRoute isLoggedIn={!!user} path="/user/command/:commandName/:commandId" key={relativeUrl} component={Builder} />
                <ProtectedRoute isLoggedIn={!!user} path="/user/commands" key={relativeUrl} component={UserCommands} />
                <Route component={PageNotFound} />
            </Switch>
        </div>
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.authReducer.user
    }
}

export default withRouter(connect(mapStateToProps)(Content));
