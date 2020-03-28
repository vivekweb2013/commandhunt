import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getCommand } from '../actions';
import './Builder.scss';

class Builder extends Component {
    state = {
        flags: {},
        options: {}
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        const { match } = this.props;
        this.props.getCommand(match.params.commandName);
    }

    handleOptionChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            options: {
                ...this.state.options,
                [name]: value
            }
        });
    }

    handleFlagChange(event) {
        const target = event.target;
        const value = target.checked; // since all flag inputs are checkboxes
        const name = target.name;

        this.setState({
            flags: {
                ...this.state.flags,
                [name]: value
            }
        });
    }

    getGeneratedFlags(command) {
        let flagsStr = '';
        let hyphenPrefixedFlags = '';
        let otherFlags = '';
        command.flags.filter(f => this.state.flags[f.properties.name]).forEach(f => {
            if (f.properties.prefix === '-') {
                // normally only single hyphen prefixed flags are allowed to group
                hyphenPrefixedFlags += f.properties.name;
            } else {
                // double hyphen prefixed flags or non-prefixed flags are generally not grouped
                otherFlags += ` ${f.properties.prefix}${f.properties.name}`;
            }
        });

        hyphenPrefixedFlags = hyphenPrefixedFlags ? `-${hyphenPrefixedFlags.trim()}` : hyphenPrefixedFlags;
        otherFlags = otherFlags.trim();

        if (hyphenPrefixedFlags !== '' || otherFlags !== '') {
            flagsStr = `${hyphenPrefixedFlags} ${otherFlags}`;
        }
        return flagsStr;
    }

    getGeneratedOptions(command) {
        let optionsStr = '';
        command.options.filter(o => this.state.options[o.properties.name]).forEach(o => {
            const prefix = !o.properties.prefix.endsWith('=') ? `${o.properties.prefix} ` : o.properties.prefix;
            optionsStr += ` ${prefix}${this.state.options[o.properties.name]}`;
        });

        return optionsStr.trim();
    }

    getGeneratedCommand(command) {
        if (!command) return null;
        let commandStr = '';
        const flagsStr = this.getGeneratedFlags(command);
        const optionsStr = this.getGeneratedOptions(command);

        commandStr = `${command.properties.name} ${flagsStr} ${optionsStr}`;
        return commandStr;
    }

    render() {
        const { command, user } = this.props;
        const newlineRegex = /(?:\r\n|\r|\n)/g;

        const generatedCommand = this.getGeneratedCommand(command);

        return command ? (
            <div className="builder">
                <div className="header">
                    <div className="text">
                        <code>
                            {generatedCommand != null && (<span>{generatedCommand}</span>)}
                        </code>
                    </div>
                </div>

                <div className="content">
                    <div className="title"><span>Build <span className="command-name">{command.properties.name}</span> Command</span></div>
                    <div className="category"><span>SYNTAX</span></div>
                    <div className="syntax"><code>
                        {command.properties.syntax.split(newlineRegex).map((item, index) => (
                            <span key={index}>{item.replace(/\.\.\./g, '···') /* replacing dots to avoid confusion with ellipsis */}<br /></span>
                        ))}</code><br />
                    </div>
                    <form onSubmit={(e) => e.preventDefault()}>
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
                                                    <input id={option.id} type="text" name={option.properties.name}
                                                        onChange={(e) => this.handleOptionChange(e)} />
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
                                                    <input id={flag.id} type="checkbox" name={flag.properties.name} checked={this.state.flags[flag.properties.name] || false}
                                                        onChange={(e) => this.handleFlagChange(e)} />
                                                    <label htmlFor={flag.id}>
                                                        <svg viewBox="0,0,50,50"><path d="M5 30 L 20 45 L 45 5"></path></svg>
                                                    </label>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="form-buttons">
                            <button className="ripple" type="button">PRINT</button>
                            <button className="ripple tooltip tooltip-t" data-tooltip="Login to Save" type="button" disabled={!user}>SAVE</button>
                        </div>
                    </form>
                </div>
            </div>
        ) : '';
    }
}

const mapStateToProps = (state, props) => {
    return {
        command: state.commandReducer.command,
        user: state.authReducer.user
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
