import React from 'react';
import Header from './components/Header';
import Content from './components/Content';
import Footer from './components/Footer';
import './App.scss';

import { library } from '@fortawesome/fontawesome-svg-core'
import { faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog } from '@fortawesome/free-solid-svg-icons'

library.add(faHome, faSignInAlt, faSignOutAlt, faUserCog, faCog);

function App() {
  return (
    <React.Fragment>
      <Header />
      <div className="main-container">
        <Content />
      </div>
      <Footer />
    </React.Fragment>
  );
}

export default App;
