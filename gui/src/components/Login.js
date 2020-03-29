import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import FirebaseAuth from './auth/FirebaseAuth';
import Spinner from './common/Spinner';
import './Login.scss';

class Login extends Component {
    state = {
        loginInProgress: false
    }
    componentDidMount() {
        FirebaseAuth.handlePostSignInRedirect();
    }
    handleLogin(e, provider) {
        e.preventDefault();
        this.setState({ loginInProgress: true });
        FirebaseAuth.signInWithProvider(provider);
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
                    <span className="social-login-text">Login with Social Media</span>

                    <div className="social-buttons">
                        <button onClick={(e) => this.handleLogin(e, 'google.com')}> <span className="icon-google"></span>Login with Google</button>
                        <button onClick={(e) => this.handleLogin(e, 'facebook.com')}> <span className="icon-facebook"></span>Login with Facebook</button>
                        <button onClick={(e) => this.handleLogin(e, 'github.com')}> <span className="icon-github" ></span>Login with GitHub</button>
                        <button onClick={(e) => this.handleLogin(e, 'twitter.com')}> <span className="icon-twitter"></span>Login with Twitter</button>
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

export default withRouter(connect(mapStateToProps, null)(Login));
