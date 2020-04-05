import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import FirebaseAuth from './auth/FirebaseAuth';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Header.scss';

class Header extends Component {
    handleLogin(event) {
        event.preventDefault();
        this.props.history.push('/login');
    }

    handleLogout(event) {
        event.preventDefault();
        FirebaseAuth.signOut();
    }

    handleUserCommands(event) {
        event.preventDefault();
        this.props.history.push('/command/user-commands');
    }

    render() {
        const { user } = this.props;

        return (
            <header className="site-header">
                <div className="logo">
                    <a href="/"><FontAwesomeIcon icon="home" />&nbsp;Command Builder</a>
                </div>

                <div className="dropdown">
                    <button disabled={!!user} onClick={(e) => this.handleLogin(e)} className="dropdown-btn">
                        {user ?
                            <><span alt="avatar" style={{ backgroundImage: `url(${user.photoUrl})` }} className="avatar" /><span>&nbsp;{user.displayName}</span></>
                            : <>&nbsp;<FontAwesomeIcon icon="sign-in-alt" />&nbsp;&nbsp;Login</>
                        }
                    </button>
                    {user && <div className="dropdown-content">
                        <button key="Logout" onClick={this.handleLogout}><FontAwesomeIcon icon="sign-out-alt" />&nbsp;Logout</button>
                        <Link to="/" key="Profile"><FontAwesomeIcon icon="user-cog" />&nbsp;Profile</Link>
                        <Link to="/" key="Settings"><FontAwesomeIcon icon="cog" />&nbsp;Settings</Link>
                    </div>}
                </div>
                <button disabled={!user} {...(!user ? { 'data-tooltip': 'Login to See Saved Commands' } : {})}
                    onClick={(e) => this.handleUserCommands(e)}
                    className="nav-icon-btn tooltip-l"
                    title="My Commands">
                    <FontAwesomeIcon icon="file-alt" size="2x" />
                </button>

            </header>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        user: state.authReducer.user
    }
}

export default withRouter(connect(mapStateToProps, null)(Header));
