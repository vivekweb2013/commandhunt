import React, { Component } from 'react';
import Header from './components/Header';
import Content from './components/Content';
import Footer from './components/Footer';
import Spinner from './components/common/Spinner';
import * as API from './api/API';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { userLogin, userLogout } from './actions';
import './App.scss';

import { library } from '@fortawesome/fontawesome-svg-core';
import { faHome, faSignInAlt, faSignOutAlt, faUserCog, faEye, faCog, faCopy, faEdit, faTrashAlt, faSortUp, faSortDown, faCircleNotch, faBug, faHeart, faCodeBranch, faQuestion } from '@fortawesome/free-solid-svg-icons';

library.add(faHome, faSignInAlt, faSignOutAlt, faUserCog, faEye, faCog, faCopy, faEdit, faTrashAlt, faSortUp, faSortDown, faCircleNotch, faBug, faHeart, faCodeBranch, faQuestion);

class App extends Component {
  state = {
    loading: true
  }

  componentDidMount() {
    this.props.getUserProfile().then(() => this.setState({ loading: false }));
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
    getUserProfile: () => {
      return API.getUserProfile().then((user) => {
        dispatch(userLogin(user));
      });
    },
    userLogout: () => {
      dispatch(userLogout());
    },
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
