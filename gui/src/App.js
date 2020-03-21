import React, { Component } from 'react';
import Header from './components/Header';
import Content from './components/Content';
import Footer from './components/Footer';
import './App.scss';
import firebase from 'firebase';
import { connect } from "react-redux";
import { withRouter } from "react-router";
import { userLogin, userLogout } from "./actions";

import { library } from '@fortawesome/fontawesome-svg-core'
import { faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog } from '@fortawesome/free-solid-svg-icons'

// Configure Firebase.
const firebaseConfig = {
  apiKey: process.env.API_KEY,
  authDomain: process.env.AUTH_DOMAIN,
  databaseURL: process.env.DATABASE_URL,
  projectId: process.env.PROJECT_ID,
  storageBucket: process.env.STORAGE_BUCKET,
  messagingSenderId: process.env.MESSAGING_SENDER_ID,
  appId: process.env.APP_ID,
  measurementId: process.env.MEASUREMENT_ID
};

firebase.initializeApp(firebaseConfig);

library.add(faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog);

class App extends Component {
  state = {
    loading: true
  }

  // Listen to the Firebase Auth state and set the local state.
  componentDidMount() {
    this.unregisterAuthObserver = firebase.auth().onAuthStateChanged((user) => {
      user ? this.props.userLogin(user) : this.props.userLogout(user)
      this.setState({ loading: false });
    }, (error) => {
      this.setState({ loading: false });
      console.error(error);
    });
  }

  // Make sure we un-register Firebase observers when the component unmounts.
  componentWillUnmount() {
    this.unregisterAuthObserver();
  }

  render() {


    return (
      this.state.loading ? 'Loading ...' : // TODO: STYLE THIS LOADING ELEMENT
        <React.Fragment>
          <Header />
          <div className="main-container">
            <Content />
          </div>
          <Footer />
        </React.Fragment>
    );
  }
}

const mapStateToProps = (state, props) => {
  return {
    user: state.authReducer.user
  }
}

const mapDispatchToProps = dispatch => {
  return {
    userLogin: (user) => {
      const userDetails = {
        uid: user.uid, displayName: user.displayName, photoURL: user.photoURL,
        email: user.email, emailVerified: user.emailVerified, phoneNumber: user.phoneNumber,
      };
      dispatch(userLogin(userDetails));
    },
    userLogout: () => {
      dispatch(userLogout());
    },
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));