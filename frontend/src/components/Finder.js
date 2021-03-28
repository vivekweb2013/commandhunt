import React, { Component } from 'react';
import './Finder.scss';
import SearchInput from './common/SearchInput';
import Pagination from './common/Pagination';
import ItemsPerPage from './common/ItemsPerPage';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getQueryParamByName, getArrayQueryParamByName, getQueryParamsFromFilter } from '../Utils';
import { getCommands } from '../actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

class Finder extends Component {
    state = {
        filter: {
            conditions: getArrayQueryParamByName('conditions'),

            pagination: {
                pageNumber: Number(getQueryParamByName('pagination.pageNumber')) || 1,
                pageSize: Number(getQueryParamByName('pagination.pageSize')) || 10,
                sort: {
                    by: getQueryParamByName('pagination.sort.by') || 'name',
                    order: getQueryParamByName('pagination.sort.order') || 'ASC'
                }
            }
        }
    }

    componentDidMount() {
        this.getCommands(this.state.filter);
    }

    getCommands() {
        const { history } = this.props;

        this.props.getCommands(this.state.filter).then(() => {
            const { commands } = this.props;
            const { filter } = this.state;

            if (filter.pagination.pageNumber > commands.totalPages) {
                this.setState({ filter: { ...filter, pagination: { ...filter.pagination, pageNumber: commands.totalPages } } }, () => {
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

    getSortIcon(column) {
        const { filter } = this.state;
        return filter.pagination.sort.by === column ? (filter.pagination.sort.order === 'DESC' ? 'sort-down' : 'sort-up') : '';
    }

    sort(column) {
        const { history } = this.props;
        const { filter } = this.state;
        this.setState({
            filter: {
                ...filter, pagination: {
                    ...filter.pagination, sort: {
                        by: column,
                        order: filter.pagination.sort.by === column && filter.pagination.sort.order === 'ASC' ? 'DESC' : 'ASC'
                    }
                }
            }
        }, () => {
            const { filter } = this.state;
            history.push(getQueryParamsFromFilter(filter))
        });
    }

    render() {
        const { commands, history } = this.props;
        const { filter } = this.state;

        return (
            <div className="finder">
                <SearchInput defaultValue={filter.conditions[0] ? filter.conditions[0].value : ''}
                    onChange={this.handleQueryUpdate.bind(this)} />

                {commands && commands.totalSize > 0 ? <div>
                    <div className="toolbar">
                        <ItemsPerPage pageSize={filter.pagination.pageSize} handlePageSizeChange={this.handlePageSizeChange.bind(this)} />
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th className="name-column" onClick={(e) => this.sort('name')}>
                                    COMMAND {this.getSortIcon('name') && <FontAwesomeIcon icon={this.getSortIcon('name')} />}
                                </th>
                                <th className="syntax-column" onClick={(e) => this.sort('syntax')}>
                                    SYNTAX {this.getSortIcon('syntax') && <FontAwesomeIcon icon={this.getSortIcon('syntax')} />}
                                </th>
                                <th className="desc-column">DESCRIPTION</th>
                            </tr>
                        </thead>
                        <tbody>
                            {commands.records.map(command => <tr key={command.id}
                                onClick={e => { e.preventDefault(); history.push(`/command/build/${command.properties.name}`) }}>
                                <td className="name">{command.properties.name} </td>
                                <td className="syntax">
                                    <code>{command.properties.syntax.replace(/\.\.\./g, '···') /* replacing dots to avoid confusion with ellipsis */}</code>
                                </td>
                                <td className="desc">
                                    <span>{command.properties.desc}</span><br />
                                    {command.properties.desc && <small>{command.properties.long_desc}</small>}
                                </td>
                            </tr>)}
                        </tbody>

                    </table>
                    <Pagination pageNumber={filter.pagination.pageNumber} totalSize={commands.totalSize}
                        totalPages={commands.totalPages} maxPagesToShow={5} handlePageChange={this.handlePageChange.bind(this)} />
                </div> : <div className="no-data-msg">No Commands Found!</div>
                }
            </div>
        )
    }
}

const mapStateToProps = (state, props) => {
    return {
        commands: state.commandReducer.commands
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getCommands: (filter) => API.getCommands(filter).then(commands => {
            dispatch(getCommands(commands));
        })
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Finder));
