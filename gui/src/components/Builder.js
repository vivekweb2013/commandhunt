import React, { Component } from 'react';
import { connect } from "react-redux";
import { withRouter } from "react-router";
import * as API from "../api/API";
import { getCommand } from "../actions";
import './Builder.scss';

class Builder extends Component {
    componentDidMount() {
        const { match } = this.props;
        this.props.getCommand(match.params.commandName);
    }

    render() {
        const { command } = this.props;
        return (<div>{command && JSON.stringify(command)}</div>)
    }
}

const mapStateToProps = (state, props) => {
    return {
        command: state.commandReducer.command
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getCommand: (commandId) => {
            API.getCommand(commandId).then(command => {
                dispatch(getCommand(command));
            });
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Builder));