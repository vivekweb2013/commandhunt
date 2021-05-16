import React, { Component } from "react";
import "./Finder.scss";
import SearchInput from "./common/SearchInput";
import Pagination from "./common/Pagination";
import ItemsPerPage from "./common/ItemsPerPage";
import { connect } from "react-redux";
import { withRouter } from "react-router";
import * as API from "../api/API";
import { getQueryParamByName, getArrayQueryParamByName, getQueryParamsFromFilter } from "../Utils";
import { getMetaCommands } from "../actions";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

class Finder extends Component {
    state = {
        filter: {
            conditions: getArrayQueryParamByName("conditions"),

            pagination: {
                pageNumber: Number(getQueryParamByName("pagination.pageNumber")) || 1,
                pageSize: Number(getQueryParamByName("pagination.pageSize")) || 10,
                sort: {
                    by: getQueryParamByName("pagination.sort.by") || "name",
                    order: getQueryParamByName("pagination.sort.order") || "ASC"
                }
            }
        }
    }

    componentDidMount() {
        this._isMounted = true;
        this.getMetaCommands(this.state.filter);
    }

    getMetaCommands() {
        const { history } = this.props;

        this.props.getMetaCommands(this.state.filter).then(() => {
            const { metaCommands } = this.props;
            const { filter } = this.state;

            if (metaCommands.pageNumber !== 0 && (filter.pagination.pageNumber > metaCommands.totalPages)) {
                this._isMounted &&
                    this.setState({ filter: { ...filter, pagination: { ...filter.pagination, pageNumber: metaCommands.totalPages } } }, () => {
                        const { filter } = this.state;
                        history.push(getQueryParamsFromFilter(filter));
                    });
            }
        });
    }

    handleQueryUpdate = (value) => {
        const { history } = this.props;

        this.setState({
            filter: {
                ...this.state.filter,
                conditions: value ? [{ key: "name", value, operator: "CONTAINS" }] : []
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

    getSortIcon(columns) {
        const { filter } = this.state;
        return filter.pagination.sort.by === columns ? (filter.pagination.sort.order === "DESC" ? "sort-down" : "sort-up") : "";
    }

    sort(columns) {
        const { history } = this.props;
        const { filter } = this.state;
        this.setState({
            filter: {
                ...filter, pagination: {
                    ...filter.pagination, sort: {
                        by: columns,
                        order: filter.pagination.sort.by === columns && filter.pagination.sort.order === "ASC" ? "DESC" : "ASC"
                    }
                }
            }
        }, () => {
            const { filter } = this.state;
            history.push(getQueryParamsFromFilter(filter))
        });
    }

    render() {
        const { metaCommands, history } = this.props;
        const { filter } = this.state;

        return (
            <div className="finder">
                <SearchInput defaultValue={filter.conditions[0] ? filter.conditions[0].value : ""}
                    onChange={this.handleQueryUpdate.bind(this)} />

                {metaCommands && metaCommands.totalSize > 0 ? <div>
                    <div className="toolbar">
                        <ItemsPerPage pageSize={filter.pagination.pageSize} handlePageSizeChange={this.handlePageSizeChange.bind(this)} />
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th className="name-column" onClick={(e) => this.sort("name")}>
                                    COMMAND {this.getSortIcon("name") && <FontAwesomeIcon icon={this.getSortIcon("name")} />}
                                </th>
                                <th className="syntax-column" onClick={(e) => this.sort("syntax")}>
                                    SYNTAX {this.getSortIcon("syntax") && <FontAwesomeIcon icon={this.getSortIcon("syntax")} />}
                                </th>
                                <th className="desc-column">DESCRIPTION</th>
                            </tr>
                        </thead>
                        <tbody>
                            {metaCommands.records.map(metaCommand => <tr key={metaCommand.id}
                                onClick={e => { e.preventDefault(); history.push(`/public/command/${metaCommand.properties.name}?mode=build`) }}>
                                <td className="name">{metaCommand.properties.name} </td>
                                <td className="syntax">
                                    <code>{metaCommand.properties.syntax.replace(/\.\.\./g, "···") /* replacing dots to avoid confusion with ellipsis */}</code>
                                </td>
                                <td className="desc">
                                    <span>{metaCommand.properties.desc}</span><br />
                                    {metaCommand.properties.desc && <small>{metaCommand.properties.long_desc}</small>}
                                </td>
                            </tr>)}
                        </tbody>

                    </table>
                    <Pagination pageNumber={filter.pagination.pageNumber} totalSize={metaCommands.totalSize}
                        totalPages={metaCommands.totalPages} maxPagesToShow={5} handlePageChange={this.handlePageChange.bind(this)} />
                </div> : <div className="no-data-msg">No Commands Found!</div>
                }
            </div>
        )
    }

    componentWillUnmount() {
        this._isMounted = false;
    }
}

const mapStateToProps = (state, props) => {
    return {
        metaCommands: state.metaCommandReducer.metaCommands
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getMetaCommands: (filter) => API.getMetaCommands(filter).then(metaCommands => {
            dispatch(getMetaCommands(metaCommands));
        })
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Finder));
