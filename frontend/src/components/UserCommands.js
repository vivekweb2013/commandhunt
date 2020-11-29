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

            pageable: {
                pageNumber: Number(getQueryParamByName('pageable.pageNumber')) || 1,
                pageSize: Number(getQueryParamByName('pageable.pageSize')) || 10,
                sort: {
                    sortBy: getQueryParamByName('pageable.sort.sortBy') || 'command_text',
                    sortOrder: getQueryParamByName('pageable.sort.sortOrder') || 'ASC'
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

            if (filter.pageable.pageNumber > userCommands.totalPages) {
                this.setState({ filter: { ...filter, pageable: { ...filter.pageable, pageNumber: userCommands.totalPages } } }, () => {
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
                conditions: value ? [{ key: 'name', value, operator: 'CONTAINS' }] : []
            }
        }, () => history.push(getQueryParamsFromFilter(this.state.filter)));
    }

    handlePageChange(pageNumber) {
        const { history } = this.props;
        this.setState({ filter: { ...this.state.filter, pageable: { ...this.state.filter.pageable, pageNumber } } }, () => {
            history.push(getQueryParamsFromFilter(this.state.filter))
        });
    }

    handlePageSizeChange(e) {
        e.preventDefault();
        const { history } = this.props;
        const pageSize = Number(e.target.value);
        this.setState({ filter: { ...this.state.filter, pageable: { ...this.state.filter.pageable, pageSize } } }, () => {
            history.push(getQueryParamsFromFilter(this.state.filter))
        });
    }

    handleDelete(e, userCommand) {
        e.preventDefault();
        this.props.deleteUserCommand(userCommand).then(() => {
            ToastMaker('Deleted!');
            this.getUserCommands(this.state.filter);
        });
    }

    getSortIcon(column) {
        const { filter } = this.state;
        return filter.pageable.sort.sortBy === column ? (filter.pageable.sort.sortOrder === 'DESC' ? 'sort-down' : 'sort-up') : '';
    }

    sort(column) {
        const { history } = this.props;
        const { filter } = this.state;
        this.setState({
            filter: {
                ...filter, pageable: {
                    ...filter.pageable, sort: {
                        sortBy: column,
                        sortOrder: filter.pageable.sort.sortBy === column && filter.pageable.sort.sortOrder === 'ASC' ? 'DESC' : 'ASC'
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
                    <ItemsPerPage pageSize={filter.pageable.pageSize} handlePageSizeChange={this.handlePageSizeChange.bind(this)} />
                </div>
                <table>
                    <thead><tr>
                        <th className="command-column" onClick={(e) => this.sort('command_text')}>
                            COMMAND {this.getSortIcon('command_text') && <FontAwesomeIcon icon={this.getSortIcon('command_text')} />}
                        </th>
                        <th className="type-column" onClick={(e) => this.sort('command_name')}>
                            TYPE {this.getSortIcon('command_name') && <FontAwesomeIcon icon={this.getSortIcon('command_name')} />}
                        </th>
                        <th className="date-column" onClick={(e) => this.sort('timestamp')}>
                            DATE {this.getSortIcon('timestamp') && <FontAwesomeIcon icon={this.getSortIcon('timestamp')} />}
                        </th>
                        <th className="actions-column">ACTIONS</th>
                    </tr></thead>

                    <tbody>
                        {userCommands && userCommands.totalSize > 0 ? userCommands.records.map(userCommand =>
                            <tr key={userCommand.id} >
                                <td className="command"><code>{userCommand.properties.command_text}</code></td>
                                <td className="type">
                                    <code>{userCommand.properties.command_name}</code>
                                </td>
                                <td className="date">
                                    {formatDate(new Date(userCommand.properties.timestamp))} <br />
                                    <small>{formatTime(new Date(userCommand.properties.timestamp))}</small>
                                </td>
                                <td className="actions">
                                    <CopyToClipboard text={userCommand.properties.command_text}>
                                        <span className="copy-icon" title="copy" onClick={(e) => ToastMaker("Copied!")}>
                                            <FontAwesomeIcon icon="copy" color="slateblue" size="lg" />
                                        </span>
                                    </CopyToClipboard>
                                    <span title="edit" onClick={() =>
                                        history.push(`/command/build/${userCommand.properties.command_name}?userCommandId=${userCommand.id}`)
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
                {userCommands && <Pagination pageNumber={filter.pageable.pageNumber} totalSize={userCommands.totalSize}
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
        deleteUserCommand: (userCommand) => API.deleteUserCommand(userCommand)
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UserCommands));
