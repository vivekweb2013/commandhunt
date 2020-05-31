import React, { Component } from 'react';
import './Finder.scss';
import SearchInput from './common/SearchInput';
import Pagination from './common/Pagination';
import ItemsPerPage from './common/ItemsPerPage';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getQueryParamByName, getArrayQueryParamByName, getQueryParamsFromFilter } from '../Utils';
import { getAllCommands } from '../actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

class Finder extends Component {
    state = {
        filter: {
            conditions: getArrayQueryParamByName('conditions'),

            pageable: {
                pageNumber: Number(getQueryParamByName('pageable.pageNumber')) || 1,
                pageSize: Number(getQueryParamByName('pageable.pageSize')) || 10,
                sort: {
                    sortBy: getQueryParamByName('pageable.sort.sortBy') || 'name',
                    sortOrder: getQueryParamByName('pageable.sort.sortOrder') || 'ASC'
                }
            }
        }
    }

    componentDidMount() {
        this.props.getAllCommands(this.state.filter);
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

    render() {
        const { commands, history } = this.props;
        const { filter } = this.state;

        return (
            <div className="finder">
                <SearchInput defaultValue={filter.conditions[0] ? filter.conditions[0].value : ''}
                    onChange={this.handleQueryUpdate.bind(this)} />

                {commands && commands.totalSize > 0 ? <div>
                    <div className="toolbar">
                        <ItemsPerPage pageSize={filter.pageable.pageSize} handlePageSizeChange={this.handlePageSizeChange.bind(this)} />
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
                    <Pagination pageNumber={filter.pageable.pageNumber} totalSize={commands.totalSize}
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
        getAllCommands: (filter) => {
            API.getAllCommands(filter).then(commands => {
                dispatch(getAllCommands(commands));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Finder));
