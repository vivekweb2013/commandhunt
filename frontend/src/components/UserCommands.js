import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getUserCommands } from '../actions';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { formatDate, formatTime } from '../Utils';
import Pagination from './common/Pagination';
import ItemsPerPage from './common/ItemsPerPage';
import SearchInput from './common/SearchInput';
import ToastMaker from 'toastmaker';
import "toastmaker/dist/toastmaker.css";
import { getQueryParamByName, getArrayQueryParamByName, getQueryParamsFromFilter } from '../Utils';
import './UserCommands.scss';

class UserCommands extends Component {
    state = {
        filter: {
            conditions: getArrayQueryParamByName('conditions'),

            pagination: {
                pageNumber: Number(getQueryParamByName('pagination.pageNumber')) || 1,
                pageSize: Number(getQueryParamByName('pagination.pageSize')) || 10,
                sort: {
                    by: getQueryParamByName('pagination.sort.by') || 'commandText',
                    order: getQueryParamByName('pagination.sort.order') || 'ASC'
                }
            }
        }
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        this.getUserCommands(this.state.filter);
    }

    getUserCommands() {
        const { history } = this.props;

        this.props.getUserCommands(this.state.filter).then(() => {
            const { userCommands } = this.props;
            const { filter } = this.state;

            if (userCommands.pageNumber !== 0 && (filter.pagination.pageNumber > userCommands.totalPages)) {
                this.setState({ filter: { ...filter, pagination: { ...filter.pagination, pageNumber: userCommands.totalPages } } }, () => {
                    const { filter } = this.state;
                    history.push(getQueryParamsFromFilter(filter))
                });
            }
        });
    }

    handleQueryUpdate = (value) => {
        const { history } = this.props;

        this.setState({
            filter: {
                ...this.state.filter,
                conditions: value ? [{ key: 'commandText', value, operator: 'CONTAINS' }] : []
            }
        }, () => history.push(getQueryParamsFromFilter(this.state.filter)));
    }

    handlePageChange(pageNumber) {
        const { history } = this.props;
        this.setState({ filter: { ...this.state.filter, pagination: { ...this.state.filter.pagination, pageNumber } } }, () => {
            history.push(getQueryParamsFromFilter(this.state.filter))
        });
    }

    handlePageSizeChange(e) {
        e.preventDefault();
        const { history } = this.props;
        const pageSize = Number(e.target.value);
        this.setState({ filter: { ...this.state.filter, pagination: { ...this.state.filter.pagination, pageSize } } }, () => {
            history.push(getQueryParamsFromFilter(this.state.filter))
        });
    }

    handleDelete(e, userCommand) {
        e.preventDefault();
        this.props.deleteUserCommand(userCommand.id).then(() => {
            ToastMaker('Deleted!');
            this.getUserCommands(this.state.filter);
        });
    }

    getSortIcon(columns) {
        const { filter } = this.state;
        return filter.pagination.sort.by === columns ? (filter.pagination.sort.order === 'DESC' ? 'sort-down' : 'sort-up') : '';
    }

    sort(columns) {
        const { history } = this.props;
        const { filter } = this.state;
        this.setState({
            filter: {
                ...filter, pagination: {
                    ...filter.pagination, sort: {
                        by: columns,
                        order: filter.pagination.sort.by === columns && filter.pagination.sort.order === 'ASC' ? 'DESC' : 'ASC'
                    }
                }
            }
        }, () => {
            const { filter } = this.state;
            history.push(getQueryParamsFromFilter(filter))
        });
    }

    render() {
        const { userCommands, history } = this.props;
        const { filter } = this.state;

        return (
            <div className="user-commands">
                <span className="heading">User Commands</span>
                <div className="toolbar">
                    <SearchInput defaultValue={filter.conditions[0] ? filter.conditions[0].value : ''}
                        onChange={this.handleQueryUpdate.bind(this)} />
                    <ItemsPerPage pageSize={filter.pagination.pageSize} handlePageSizeChange={this.handlePageSizeChange.bind(this)} />
                </div>
                <table>
                    <thead><tr>
                        <th className="command-column" onClick={(e) => this.sort('commandText')}>
                            COMMAND {this.getSortIcon('commandText') && <FontAwesomeIcon icon={this.getSortIcon('commandText')} />}
                        </th>
                        <th className="type-column" onClick={(e) => this.sort('commandName')}>
                            TYPE {this.getSortIcon('commandName') && <FontAwesomeIcon icon={this.getSortIcon('commandName')} />}
                        </th>
                        <th className="date-column" onClick={(e) => this.sort('operatedOn')}>
                            DATE {this.getSortIcon('operatedOn') && <FontAwesomeIcon icon={this.getSortIcon('operatedOn')} />}
                        </th>
                        <th className="actions-column">ACTIONS</th>
                    </tr></thead>

                    <tbody>
                        {userCommands && userCommands.totalSize > 0 ? userCommands.records.map(userCommand =>
                            <tr key={userCommand.id} >
                                <td className="command"><code>{userCommand.commandText}</code></td>
                                <td className="type">
                                    <code>{userCommand.commandName}</code>
                                </td>
                                <td className="date">
                                    {formatDate(new Date(userCommand.operatedOn))} <br />
                                    <small>{formatTime(new Date(userCommand.operatedOn))}</small>
                                </td>
                                <td className="actions">
                                    <CopyToClipboard text={userCommand.commandText}>
                                        <span className="copy-icon" title="copy" onClick={(e) => ToastMaker("Copied!")}>
                                            <FontAwesomeIcon icon="clipboard" color="slateblue" size="lg" />
                                        </span>
                                    </CopyToClipboard>
                                    <span title="view" onClick={(e) =>
                                        history.push(`/user/command/${userCommand.commandName}/${userCommand.id}`)
                                    } className="view-icon">
                                        <FontAwesomeIcon icon="eye" color="slateblue" size="lg" />
                                    </span>
                                    <span title="edit" onClick={() =>
                                        history.push(`/user/command/${userCommand.commandName}/${userCommand.id}?mode=build`)
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
                {userCommands && <Pagination pageNumber={filter.pagination.pageNumber} totalSize={userCommands.totalSize}
                    totalPages={userCommands.totalPages} maxPagesToShow={5} handlePageChange={this.handlePageChange.bind(this)} />}
            </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        user: state.authReducer.user,
        userCommands: state.userCommandReducer.userCommands
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getUserCommands: (filters) => API.getUserCommands(filters).then(userCommands => dispatch(getUserCommands(userCommands))),
        deleteUserCommand: (userCommandId) => API.deleteUserCommand(userCommandId)
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UserCommands));
