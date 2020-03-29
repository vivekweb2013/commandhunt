import React, { Component } from 'react';
import firebase from 'firebase';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import './Login.scss';

class Login extends Component {
    // Configure FirebaseUI.
    uiConfig = {
        // Popup sign-in flow rather than redirect flow.
        signInFlow: 'popup',
        signInSuccessUrl: '/',
        // We will display Google and Facebook as auth providers.
        signInOptions: [
            firebase.auth.GoogleAuthProvider.PROVIDER_ID,
            // facebook auth requires https enabled server
            firebase.auth.FacebookAuthProvider.PROVIDER_ID,
            firebase.auth.TwitterAuthProvider.PROVIDER_ID,
            firebase.auth.GithubAuthProvider.PROVIDER_ID
        ]
    };


    render() {
        return (
            <StyledFirebaseAuth uiConfig={this.uiConfig} firebaseAuth={firebase.auth()} />
            // <div className="login">
            //     <span className="icon-social-login"></span>
            //     <span className="social-login-text">Login with Social Media</span>

            //     <div className="social-buttons">
            //         <button> <span className="icon-google"></span>Login with Google</button>
            //         <button> <span className="icon-facebook"></span>Login with Facebook</button>
            //         <button> <span className="icon-github" ></span>Login with GitHub</button>
            //         <button> <span className="icon-twitter"></span>Login with Twitter</button>
            //     </div>
            // </div>
        )
    }
}

export default Login;