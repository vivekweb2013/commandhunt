import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { setFilters, getUserCommands, deleteUserCommand } from '../actions';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { formatDate, formatTime } from '../Utils';
import Pagination from './common/Pagination';
import { deepCompare } from '../Utils';
import './UserCommands.scss';

class UserCommands extends Component {

    componentDidMount() {
        window.scrollTo(0, 0);
        this.getUserCommands();
    }

    componentDidUpdate(prevProps) {
        if (!deepCompare(prevProps.filters, this.props.filters)) {
            this.getUserCommands();
        }
    }

    getUserCommands() {
        const { sortBy, sortOrder } = this.props.filters || {};
        this.props.getUserCommands({
            select: ['text', 'name', 'timestamp'],
            orderBy: { field: sortBy || 'text', direction: sortOrder ? 'desc' : 'asc' }
        });
    }

    getSortIcon(column) {
        const { filters } = this.props;
        return filters && filters.sortBy === column ? (this.props.filters.sortOrder ? 'sort-down' : 'sort-up') : '';
    }

    sort(column) {
        const { sortOrder } = this.props.filters || {};
        this.props.setFilters({ sortBy: column, sortOrder: sortOrder ? 0 : 1 });
    }

    handleDelete(e, userCommand) {
        e.preventDefault();
        this.props.deleteUserCommand(userCommand);
    }

    render() {
        const { userCommands, filteredUserCommands, history } = this.props;

        return (
            <div className="user-commands">
                <div className="toolbar">
                    <span className="heading">User Commands</span>
                </div>
                <table>
                    <thead><tr>
                        <th className="command-column" onClick={(e) => this.sort('text')}>
                            COMMAND {this.getSortIcon('text') && <FontAwesomeIcon icon={this.getSortIcon('text')} />}
                        </th>
                        <th className="type-column" onClick={(e) => this.sort('name')}>
                            TYPE {this.getSortIcon('name') && <FontAwesomeIcon icon={this.getSortIcon('name')} />}
                        </th>
                        <th className="date-column" onClick={(e) => this.sort('timestamp')}>
                            DATE {this.getSortIcon('timestamp') && <FontAwesomeIcon icon={this.getSortIcon('timestamp')} />}
                        </th>
                        <th className="actions-column">ACTIONS</th>
                    </tr></thead>

                    <tbody>
                        {filteredUserCommands && filteredUserCommands.length > 0 ? filteredUserCommands.map(userCommand =>
                            <tr key={userCommand.__meta__.id} >
                                <td className="command"><code>{userCommand.text}</code></td>
                                <td className="type">
                                    <code>{userCommand.name}</code>
                                </td>
                                <td className="date">
                                    {formatDate(new Date(userCommand.timestamp))} <br />
                                    <small>{formatTime(new Date(userCommand.timestamp))}</small>
                                </td>
                                <td className="actions">
                                    <CopyToClipboard text={userCommand.text}>
                                        <span className="copy-icon" title="copy">
                                            <FontAwesomeIcon icon="clipboard" color="slateblue" size="lg" />
                                        </span>
                                    </CopyToClipboard>
                                    <span title="edit" onClick={() =>
                                        history.push(`/command/build/${userCommand.name}?userCommandId=${userCommand.__meta__.id}`)
                                    } className="edit-icon">
                                        <FontAwesomeIcon icon="edit" color="slateblue" size="lg" />
                                    </span>
                                    <span title="delete" onClick={(e) => this.handleDelete(e, userCommand)} className="delete-icon">
                                        <FontAwesomeIcon icon="trash-alt" color="tomato" size="lg" />
                                    </span>
                                </td>
                            </tr>) : <tr><td colSpan="4"><div className="no-data-msg">No Commands Found!</div></td></tr>}
                    </tbody>
                </table>
                <Pagination
                    totalItems={userCommands ? userCommands.length : 0} itemsPerPage={10}
                    maxPagesToShow={5} />
            </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    const { userCommands, filters, pagination } = state.userCommandReducer;
    const { user } = state.authReducer;
    let filteredUserCommands = [];
    if (pagination && userCommands) {
        const { itemsPerPage } = pagination;
        const currentPage = pagination.currentPage || 1;
        const startPos = (currentPage - 1) * itemsPerPage;
        filteredUserCommands = userCommands.slice(startPos, startPos + itemsPerPage)
    }

    return {
        user,
        userCommands,
        filters,
        filteredUserCommands
    }
}

const mapDispatchToProps = dispatch => {
    return {
        setFilters: (filters) => {
            dispatch(setFilters(filters));
        },
        getUserCommands: (filters) => {
            API.getUserCommands(filters).then(userCommands => dispatch(getUserCommands(userCommands)));
        },
        deleteUserCommand: (userCommand) => {
            API.deleteUserCommand(userCommand).then(() => dispatch(deleteUserCommand(userCommand)));
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UserCommands));
