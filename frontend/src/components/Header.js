import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { userLogout } from '../actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import userSVG from '../styles/icons/user.svg'
import './Header.scss';

class Header extends Component {
    handleRootNavigation(e) {
        e.preventDefault();
        this.props.history.push('/');
    }

    handleLogin(e) {
        e.preventDefault();
        this.props.history.push('/login');
    }

    handleLogout(e) {
        e.preventDefault();
        this.props.userLogout();
    }

    handleUserCommands(e) {
        e.preventDefault();
        this.props.history.push('/command/user-commands');
    }

    render() {
        const { user } = this.props;
        const imageUrl = user && user.imageUrl ? user.imageUrl : userSVG;

        return (
            <header className="site-header no-print">
                <div className="logo">
                    <a href="/" onClick={(e) => this.handleRootNavigation(e)}>
                        <FontAwesomeIcon icon="home" />&nbsp;CommandHunt</a>
                </div>

                <div className="dropdown">
                    <button disabled={!!user} onClick={(e) => this.handleLogin(e)} className="dropdown-btn" type="button">
                        {user ?
                            <><span alt="avatar" style={{ backgroundImage: `url(${imageUrl})` }} className="avatar" /><span>&nbsp;{user.name}</span></>
                            : <>&nbsp;<FontAwesomeIcon icon="sign-in-alt" />&nbsp;&nbsp;Login</>
                        }
                    </button>
                    {user && <div className="dropdown-content">
                        <button key="Logout" onClick={e => this.handleLogout(e)} type="button"><FontAwesomeIcon icon="sign-out-alt" />&nbsp;Logout</button>
                        <Link to="/" key="Profile"><FontAwesomeIcon icon="user-cog" />&nbsp;Profile</Link>
                        <Link to="/" key="Settings"><FontAwesomeIcon icon="cog" />&nbsp;Settings</Link>
                    </div>}
                </div>
                <button disabled={!user} {...(!user ? { 'data-tooltip': 'Login to See Saved Commands' } : {})}
                    onClick={(e) => this.handleUserCommands(e)}
                    className="nav-icon-btn tooltip-l" type="button" title="My Saved Commands">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" style={{ height: '100%' }} viewBox="0 0 200 200"><path d="M183 155l-3 16a12 12 0 01-13 10H25c-4-1-7-2-9-5s-3-6-3-10l14-66c1-5 2-10 9-10h153a6 6 0 016 7l-12 58zm3-81c0-9-6-14-12-14h-7v22h19zM17 98c2-9 9-16 18-18V55H18c-7 0-12 5-12 12v87l11-56zm141-77v61H44V21zm-19 47c8 0 8-9 0-9H63c-9 0-9 9 0 9zm0-24c8 0 8-9 0-9H64c-10 0-10 8-1 8" /></svg>
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

const mapDispatchToProps = dispatch => {
    return {
        userLogout: () => {
            // This will remove token from localStorage & also remove user from store
            API.userLogout().then(() => dispatch(userLogout()));
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Header));
