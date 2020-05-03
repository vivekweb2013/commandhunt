import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Spinner from './common/Spinner';
import { userLogin } from '../actions';
import * as API from '../api/API';
import { getQueryParamByName } from '../Utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Login.scss';

class Login extends Component {
    state = {
        loginInProgress: false,
        loginRequest: {}
    }
    componentDidMount() {
        const token = getQueryParamByName('token', window.location.search);
        const error = getQueryParamByName('error', window.location.search);
        if (token || error) {
            window.history.replaceState(null, null, window.location.origin + window.location.pathname); // URL Cleanup
            (token && this.props.getUserProfile(token)) || alert(error);
        }
    }
    handleInputChange(event) {
        const { name, value } = event.target;
        const { loginRequest } = this.state;

        this.setState({ loginRequest: { ...loginRequest, [name]: value } });
    }
    handleOAuthLogin(e, provider) {
        e.preventDefault();
        this.setState({ loginInProgress: true });
        window.location.href = `${API.API_URL}/oauth2/authorize/${provider}?redirect_uri=${window.location.href}`;
    }
    handleManualLogin(e) {
        e.preventDefault();
        const { loginRequest } = this.state;
        this.setState({ loginInProgress: true });
        this.props.userLogin(loginRequest).then((resp) => {
            this.setState({ loginInProgress: false });
        });
    }
    handleSignUp(e) {
        e.preventDefault();
        this.props.history.push('/signup');
    }
    render() {
        const { user } = this.props;
        if (user) this.props.history.goBack();

        const { loginInProgress } = this.state;
        const isRedirected = window.location.href.match(/[&?]token=/);
        return (
            loginInProgress || isRedirected ? <div className="app loading" ><Spinner size="50" /> <br />LOADING</div> :
                <div className="login">
                    <span className="icon-social-login"></span>
                    <span className="login-text">Login with Social Media or Manually</span>
                    <div className="box">

                        <form onSubmit={(e) => this.handleManualLogin(e)} className="manual-login">
                            <input type="email" name="email" onChange={e => this.handleInputChange(e)} placeholder="Email" required />
                            <input type="password" name="password" onChange={e => this.handleInputChange(e)} placeholder="Password" required />

                            <button type="submit">
                                {this.state.loginInProgress && <FontAwesomeIcon icon="circle-notch" spin />} Login
                            </button>
                            <button type="button" onClick={e => this.handleSignUp(e)}>Sign Up</button>
                        </form>

                        <div className="divide">
                            <span className="divide-text">or</span>
                        </div>

                        <div className="social-buttons">
                            <button className="google" onClick={(e) => this.handleOAuthLogin(e, 'google')}> <span className="icon-google"></span>Login with Google</button>
                            <button className="facebook" onClick={(e) => this.handleOAuthLogin(e, 'facebook')}> <span className="icon-facebook"></span>Login with Facebook</button>
                            <button className="github" onClick={(e) => this.handleOAuthLogin(e, 'github')}> <span className="icon-github" ></span>Login with GitHub</button>
                        </div>

                    </div>
                </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        user: state.authReducer.user
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getUserProfile: (token) => {
            return API.getUserProfile(token).then((user) => {
                dispatch(userLogin(user));
            });
        },
        userLogin: (loginRequest) => {
            return API.userLogin(loginRequest).then((user) => {
                dispatch(userLogin(user));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));
