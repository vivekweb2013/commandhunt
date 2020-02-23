import React, { Component } from 'react';
import { connect } from "react-redux";
import { withRouter } from "react-router";
import * as API from "../api/API";
import { getCommand } from "../actions";
import './Builder.scss';

class Builder extends Component {
    componentDidMount() {
        window.scrollTo(0, 0);
        const { match } = this.props;
        this.props.getCommand(match.params.commandName);
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
    }

    render() {
        const { command } = this.props;
        const newlineRegex = /(?:\r\n|\r|\n)/g;

        return command ? (
            <div className="builder">
                <div className="header">
                    <div className="text">
                        <code>
                            {
                                command.properties.syntax.split(newlineRegex)
                                    .map((item, index) => (
                                        <span key={index}>{item.replace(/\.\.\./g, '···') /* replacing dots to avoid confusion with ellipsis */}<br /></span>
                                    ))
                            }
                        </code>
                    </div>
                </div>
                {/* spacer div is add to take up the same space as header so that header don't overlap with content area */}
                <div className="spacer"><br /><br />{command.properties.syntax.split(newlineRegex).map((e, i) => (i > 1 ? <br key={i} /> : ''))}</div>
                <div className="content">
                    <div className="title"><span>Build <span className="command-name">{command.properties.name}</span> Command</span></div>
                    <form onSubmit={this.handleSubmit}>
                        <div className="section">
                            {command.options.length > 0 && (
                                <div className="options">
                                    <div className="category"><span>OPTIONS</span></div>
                                    <div className="fields">
                                        {command.options.map((option, i) => (
                                            <div key={i} className="row">
                                                <div className="label-col">
                                                    <label htmlFor={option.id}>{option.properties.desc}</label>
                                                </div>
                                                <div className="input-col">
                                                    <input id={option.id} type="text" name={option.properties.name}></input>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                    <br />
                                </div>
                            )}
                            {command.flags.length > 0 && (
                                <div className="flags">
                                    <div className="category"><span>FLAGS</span></div>
                                    <div className="fields">
                                        {command.flags.map((flag, i) => (
                                            <div key={i} className="row">
                                                <div className="label-col">
                                                    <label htmlFor={flag.id}>{flag.properties.desc}</label>
                                                </div>
                                                <div className="input-col">
                                                    <input id={flag.id} type="checkbox" name={flag.properties.name}></input>
                                                    <label htmlFor={flag.id}>
                                                        <svg viewBox="0,0,50,50">
                                                            <path d="M5 30 L 20 45 L 45 5"></path>
                                                        </svg>
                                                    </label>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="form-buttons"><button className="ripple">PRINT</button><input className="ripple" type="submit" value="SAVE" /></div>
                    </form>
                </div>
            </div>
        ) : '';
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