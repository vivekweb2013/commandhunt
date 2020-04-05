import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getUserCommands } from '../actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './UserCommands.scss';

class UserCommands extends Component {
    state = {
        sortBy: '',
        sortOrder: 0
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.props.getUserCommands();
    }

    getSortIcon(column) {
        return this.state.sortBy === column ? (this.state.sortOrder ? 'sort-up' : 'sort-down') : '';
    }

    sort(column) {
        this.setState({ sortBy: column, sortOrder: this.state.sortOrder ? 0 : 1 });
    }

    render() {
        const { userCommands, history } = this.props;

        return (
            <div className="user-commands">
                <div className="toolbar">
                    <span className="heading">User Commands</span>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th className="command-column" onClick={(e) => this.sort('command')}>
                                COMMAND {this.getSortIcon('command') && <FontAwesomeIcon icon={this.getSortIcon('command')} />}
                            </th>
                            <th className="type-column" onClick={(e) => this.sort('type')}>
                                TYPE {this.getSortIcon('type') && <FontAwesomeIcon icon={this.getSortIcon('type')} />}
                            </th>
                            <th className="date-column" onClick={(e) => this.sort('date')}>
                                DATE {this.getSortIcon('date') && <FontAwesomeIcon icon={this.getSortIcon('date')} />}
                            </th>
                            <th className="actions-column">ACTIONS</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userCommands && userCommands.map(userCommand => <tr key={userCommand.__meta__.id} onClick={e => { e.preventDefault(); history.push(`/command/build/${userCommand.name}`) }}>
                            <td className="command"><code>{userCommand.text}</code></td>
                            <td className="type">
                                <code>{userCommand.name}</code>
                            </td>
                            <td className="date">
                                {userCommand.__meta__.updateTime}
                            </td>
                            <td className="actions">
                                <span className="copy-icon"><FontAwesomeIcon icon="copy" color="slateblue" size="lg" /></span>
                                <span className="edit-icon"><FontAwesomeIcon icon="edit" color="slateblue" size="lg" /></span>
                                <span className="delete-icon"><FontAwesomeIcon icon="trash-alt" color="tomato" size="lg" /></span>
                            </td>
                        </tr>)}
                    </tbody>
                </table>
            </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        user: state.authReducer.user,
        userCommands: state.commandReducer.userCommands
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getUserCommands: () => {
            API.getUserCommands().then(userCommands => { dispatch(getUserCommands(userCommands)); });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UserCommands));
