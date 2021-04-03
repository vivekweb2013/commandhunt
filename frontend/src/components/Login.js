import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Spinner from './common/Spinner';
import { userLogin } from '../actions';
import * as API from '../api/API';
import { getQueryParamByName } from '../Utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Login.scss';
import Modal from './common/Modal';

class Login extends Component {
    state = {
        loginInProgress: false,
        loginRequest: {},
        loginError: ''
    }
    componentDidMount() {
        const token = getQueryParamByName('token', window.location.search);
        const error = getQueryParamByName('error', window.location.search);
        if (token || error) {
            window.history.replaceState(null, null, window.location.origin + window.location.pathname); // URL Cleanup
            (token && this.props.getUserProfile(token)) || this.setState({ loginError: error });
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

    onCloseModal() {
        this.setState({ loginError: '' });
    }
    render() {
        const { user, history } = this.props;
        if (user) history.replace('/');

        const { loginInProgress, loginError } = this.state;
        const isRedirected = window.location.href.match(/[&?]token=/);
        return (
            loginInProgress || isRedirected ? <div className="app loading" ><Spinner size="50" /> <br />LOADING</div> :
                <div className="login">
                    <span className="icon-s-nw-login"></span>
                    <div className="box">

                        <form onSubmit={(e) => this.handleManualLogin(e)} className="manual-login">
                            <input type="email" name="email" onChange={e => this.handleInputChange(e)} placeholder="Email" required />
                            <input type="password" name="password" onChange={e => this.handleInputChange(e)} placeholder="Password" required />

                            <button type="submit">
                                {loginInProgress && <FontAwesomeIcon icon="circle-notch" spin />} Login
                            </button>
                            <button type="button" onClick={e => this.handleSignUp(e)}>Sign Up</button>
                        </form>

                        <div className="divide">
                            <span className="divide-text">or</span>
                        </div>

                        <div className="s-nw-buttons">
                            <span className="s-nw-login-text">Login with Social Networks</span>
                            <button className="ggl" onClick={(e) => this.handleOAuthLogin(e, 'google')}> <span className="icon-ggl"></span>Login with Google</button>
                            <button className="fbk" onClick={(e) => this.handleOAuthLogin(e, 'facebook')}> <span className="icon-fbk"></span>Login with Facebook</button>
                            <button className="gh" onClick={(e) => this.handleOAuthLogin(e, 'github')}> <span className="icon-gh" ></span>Login with GitHub</button>
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
