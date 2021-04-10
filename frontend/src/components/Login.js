import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Spinner from './common/Spinner';
import { userLogin, isManualAuthAllowed } from '../actions';
import * as API from '../api/API';
import { getQueryParamByName } from '../Utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Login.scss';
import Modal from './common/Modal';

class Login extends Component {
    state = {
        loginInProgress: false,
        loginRequest: {},
        loginError: '',
        postLoginRedirectUrl: '/'
    }
    componentDidMount() {
        const token = getQueryParamByName('token', window.location.search);
        const error = getQueryParamByName('error', window.location.search);
        const referer = getQueryParamByName('referer', window.location.search);
        if (token || error) {
            window.history.replaceState(null, null, window.location.origin + window.location.pathname); // URL Cleanup
            if (token) {
                this.props.getUserProfile(token);
                referer && this.setState({ postLoginRedirectUrl: referer });
            } else {
                this.setState({ loginError: error });
            }
        }
        this.props.isManualAuthAllowed();
    }
    handleInputChange(event) {
        const { name, value } = event.target;
        const { loginRequest } = this.state;

        this.setState({ loginRequest: { ...loginRequest, [name]: value } });
    }
    handleOAuthLogin(e, provider) {
        e.preventDefault();
        this.setState({ loginInProgress: true });
        window.location.href = `${API.API_URL}/oauth2/authorize/${provider}` + this.getRedirectQueryParam();
    }
    handleSignUp(e) {
        e.preventDefault();
        this.props.history.push('/signup');
    }
    getRedirectQueryParam() {
        const { location } = this.props;
        const referer = location.state && location.state.referer ? `?referer=${location.state.referer}` : '';
        return `?redirect_uri=${window.location.href}${referer}`;
    }
    onCloseModal() {
        this.setState({ loginError: '' });
    }

    render() {
        const { user, history, manualAuthAllowed } = this.props;
        if (user) history.replace(this.state.postLoginRedirectUrl);

        const { loginInProgress, loginError } = this.state;
        const isRedirected = window.location.href.match(/[&?]token=/);
        return (
            loginInProgress || isRedirected ? <div className="app loading" ><Spinner size="50" /> <br />LOADING</div> :
                <div className="login">
                    <span className="icon-s-nw-login"></span>
                    <div className="box">

                        {manualAuthAllowed && <>
                            <form action={`${API.API_URL}/auth/login` + this.getRedirectQueryParam()} method="post" className="manual-login">
                                <input type="email" name="email" onChange={e => this.handleInputChange(e)} placeholder="Email" required />
                                <input type="password" name="password" onChange={e => this.handleInputChange(e)} placeholder="Password" required />

                                <button type="submit">
                                    {loginInProgress && <FontAwesomeIcon icon="circle-notch" spin />} Login
                            </button>
                                <button type="button" onClick={e => this.handleSignUp(e)}>Sign Up</button>
                            </form>

                            <div className="divide">
                                <span className="divide-text">or</span>
                            </div> </>}
                        <div className="s-nw-buttons">
                            <span className="s-nw-login-text">Login with Social Networks</span>
                            <button type="button" className="ggl" onClick={(e) => this.handleOAuthLogin(e, 'google')}> <span className="icon-ggl"></span>Login with Google</button>
                            <button type="button" className="fbk" onClick={(e) => this.handleOAuthLogin(e, 'facebook')}> <span className="icon-fbk"></span>Login with Facebook</button>
                            <button type="button" className="gh" onClick={(e) => this.handleOAuthLogin(e, 'github')}> <span className="icon-gh" ></span>Login with GitHub</button>
                        </div>

                    </div>
                    {loginError && <Modal onClose={this.onCloseModal.bind(this)} title="Something Went Wrong..." type="error">
                        <span>{loginError}</span></Modal>}
                </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        user: state.authReducer.user,
        manualAuthAllowed: state.authReducer.manualAuthAllowed
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getUserProfile: (token) => {
            return API.getUserProfile(token).then((user) => {
                dispatch(userLogin(user));
            });
        },
        isManualAuthAllowed: () => {
            return API.isManualAuthAllowed().then((manualAuthAllowed) => {
                dispatch(isManualAuthAllowed(manualAuthAllowed === "true"));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));
