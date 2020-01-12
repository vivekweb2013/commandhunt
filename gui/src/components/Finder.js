import React, { Component } from 'react';
import './Finder.scss';
import { connect } from "react-redux";
import { withRouter } from "react-router";
import * as API from "../api/API";
import { getAllCommands } from "../actions";



class Finder extends Component {
    state = {}

    componentDidMount() {
        this.props.getAllCommands();
    }

    render() {
        const { commands } = this.props;
        return (
            <div className="finder">
                <fieldset className="search-box-container">
                    <input type="text" placeholder="Search..." className="field" />
                    <div className="icons-container">
                        <div className="icon-search"></div>
                        <div className="icon-close"> </div>
                    </div>
                </fieldset>

                {commands && commands.length > 0 ?
                    <table>
                        <thead>
                            <tr>
                                <th>COMMAND</th>
                                <th>DESCRIPTION</th>
                            </tr>
                        </thead>
                        <tbody>
                            {commands.map(command => <tr key={command.id}>
                                <td>{command.properties.name}</td>
                                <td>{command.properties.desc}</td>
                            </tr>)}
                        </tbody>
                    </table>
                    : <span>No Commands Found!</span>
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
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Finder));
