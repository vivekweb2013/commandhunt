import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import FirebaseAuth from './auth/FirebaseAuth';
import Spinner from './common/Spinner';
import { userLogin } from '../actions';
import * as API from '../api/API';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Login.scss';

class Login extends Component {
    state = {
        loginInProgress: false,
        loginRequest: {}
    }
    componentDidMount() {
        FirebaseAuth.handlePostSignInRedirect();
    }
    handleInputChange(event) {
        const { name, value } = event.target;
        const { loginRequest } = this.state;

        this.setState({ loginRequest: { ...loginRequest, [name]: value } });
    }
    handleOAuthLogin(e, provider) {
        e.preventDefault();
        this.setState({ loginInProgress: true });
        FirebaseAuth.signInWithProvider(provider);
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
        const isRedirected = window.location.href.match(/[&?]code=/);
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
                            <button className="google" onClick={(e) => this.handleOAuthLogin(e, 'google.com')}> <span className="icon-google"></span>Login with Google</button>
                            <button className="facebook" onClick={(e) => this.handleOAuthLogin(e, 'facebook.com')}> <span className="icon-facebook"></span>Login with Facebook</button>
                            <button className="github" onClick={(e) => this.handleOAuthLogin(e, 'github.com')}> <span className="icon-github" ></span>Login with GitHub</button>
                            <button className="twitter" onClick={(e) => this.handleOAuthLogin(e, 'twitter.com')}> <span className="icon-twitter"></span>Login with Twitter</button>
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
        userLogin: (loginRequest) => {
            return API.userLogin(loginRequest).then((user) => {
                dispatch(userLogin({
                    localId: user.id, displayName: user.properties.name, photoUrl: null,
                    email: user.properties.email, emailVerified: user.properties.emailVerified
                }));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));
