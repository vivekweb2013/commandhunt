import React, { Component } from 'react';
import './Finder.scss';
import { connect } from "react-redux";
import { withRouter } from "react-router";
import * as API from "../api/API";
import { getAllCommands, getMatchingCommands } from "../actions";

class Finder extends Component {
    state = {
        query: ''
    }

    componentDidMount() {
        this.props.getAllCommands();
    }

    handleQueryUpdate = (textValue) => {
        this.setState({ query: textValue });
        textValue ? this.props.getMatchingCommands(textValue) : this.props.getAllCommands();
    }

    handleInputReset = () => {
        this.setState({ query: '' });
        this.handleQueryUpdate('');
    }

    render() {
        const { commands, history } = this.props;
        return (
            <div className="finder">
                <fieldset className="search-box-container">
                    <input type="text" onChange={event => this.handleQueryUpdate(event.target.value)} placeholder="Search..." value={this.state.query} ref={el => this.searchInput = el} className="field" />
                    <div className={'icons-container ' + (this.state.query ? 'icons-container-flip' : '')}>
                        <div className="icon-search"></div>
                        <div className="icon-close" onClick={event => this.handleInputReset()}> </div>
                    </div>
                </fieldset>

                {commands && commands.length > 0 ?
                    <table>
                        <thead>
                            <tr>
                                <th className="name-column">COMMAND</th>
                                <th className="syntax-column">SYNTAX</th>
                                <th className="desc-column">DESCRIPTION</th>
                            </tr>
                        </thead>
                        <tbody>
                            {commands.map(command => <tr key={command.id} onClick={e => { e.preventDefault(); history.push(`/build/${command.properties.name}`) }}>
                                <td className="name">{command.properties.name} </td>
                                <td className="syntax"><code>{command.properties.syntax.replace(/\.\.\./g, '···') /* replacing dots to avoid confusion with ellipsis */}</code></td>
                                <td className="desc">
                                    <span>{command.properties.desc}</span><br />
                                    {command.properties.desc && <small>{command.properties.long_desc}</small>}
                                </td>
                            </tr>)}
                        </tbody>
                    </table>
                    : <div className="no-data-msg">No Commands Found!</div>
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
        getAllCommands: () => {
            API.getAllCommands().then(commands => {
                dispatch(getAllCommands(commands));
            });
        },
        getMatchingCommands: (query) => {
            API.getMatchingCommands(query).then(commands => {
                dispatch(getMatchingCommands(commands));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Finder));