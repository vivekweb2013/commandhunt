import React, { Component } from 'react';
import Header from './components/Header';
import Content from './components/Content';
import Footer from './components/Footer';
import Spinner from './components/common/Spinner';
import FirebaseAuth from './components/auth/FirebaseAuth';
import './App.scss';

import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { userLogin, userLogout } from './actions';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog } from '@fortawesome/free-solid-svg-icons';

library.add(faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog);

class App extends Component {
  state = {
    loading: true
  }

  // Listen to the Firebase Auth state and set the local state.
  componentDidMount() {
    this.unregisterAuthListener = FirebaseAuth.addListener((user) => {
      user ? this.props.userLogin(user) : this.props.userLogout(user);
      this.setState({ loading: false });
    });
  }

  // Make sure we un-register Firebase observers when the component unmounts.
  componentWillUnmount() {
    this.unregisterAuthListener();
  }

  render() {
    return (
      this.state.loading ? <div className="app loading" ><Spinner size="50" /> <br />LOADING</div> :
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
    userLogin: ({ localId, displayName, photoUrl, email, emailVerified }) => {
      dispatch(userLogin({ localId, displayName, photoUrl, email, emailVerified }));
    },
    userLogout: () => {
      dispatch(userLogout());
    },
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
