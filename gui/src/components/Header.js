import React, { Component } from 'react';
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Header.scss';

class Header extends Component {
    render() {
        return (
            <header className="header">
                <div className="logo">
                    <a href="/"><FontAwesomeIcon icon="home" />&nbsp;Command Builder</a>
                </div>

                <div className="dropdown">
                    <button className="dropdown-btn"><FontAwesomeIcon icon="sign-in-alt" />&nbsp;Sign In</button>
                    <div className="dropdown-content">
                        <Link to="/" key="Sign Out"><FontAwesomeIcon icon="sign-out-alt" />&nbsp;Sign Out</Link>
                        <Link to="/" key="Profile"><FontAwesomeIcon icon="user-cog" />&nbsp;Profile</Link>
                        <Link to="/" key="Settings"><FontAwesomeIcon icon="cog" />&nbsp;Settings</Link>
                    </div>
                </div>
            </header>
        )
    }
}

export default Header;