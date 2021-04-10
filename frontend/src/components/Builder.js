import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getMetaCommand } from '../actions';
import { getQueryParamByName, getValidationRegex } from '../Utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Builder.scss';
import DynamicTextInput from './common/DynamicTextInput';
import PermissionInput from './common/PermissionInput';
import CopyToClipboard from 'react-copy-to-clipboard';
import ToastMaker from 'toastmaker';

class Builder extends Component {
    state = {
        userCommand: {
            flags: {},
            options: {}
        },
        saveInProgress: false,
        enableEdit: true
    }

    headerDiv = React.createRef();
    contentDiv = React.createRef();

    componentDidUpdate() {
        if (this.headerDiv.current) {
            const headerHeight = this.headerDiv.current.offsetHeight;
            this.contentDiv.current.style.paddingTop = `${headerHeight + 30}px`;
        }
    }

    componentDidMount() {
        window.scrollTo(0, 0);
        const { match, location } = this.props;
        const userCommandId = getQueryParamByName('userCommandId', location.search);
        this.setState({ userCommandId, enableEdit: location.pathname.startsWith('/command/build/') });
        if (userCommandId != null) {
            this.props.getUserCommand(userCommandId).then((userCommand) => {
                this.setState({ userCommand });
                this.props.getMetaCommand(match.params.commandName);
            });
        } else {
            this.props.getMetaCommand(match.params.commandName);
        }
    }

    handleOptionChange(name, value) {
        const userCommand = {
            flags: {
                ...this.state.userCommand.flags,
            },
            options: {
                ...this.state.userCommand.options,
                [name]: value
            }
        }

        this.setState({ userCommand });
    }

    handleFlagChange(event) {
        const { name, checked } = event.target;
        const userCommand = {
            flags: {
                ...this.state.userCommand.flags,
                [name]: checked
            },
            options: {
                ...this.state.userCommand.options
            }
        }

        this.setState({ userCommand });
    }

    hasSolitarySituation(currentSolitaryFlag) {
        // Solitary situation
        // - one of the solitary flag is enabled, because of which no other option or flag(other than selected solitary flag) can be used
        // - non solitary flag or option is used, because of which all the solitary flags should be disabled

        const { userCommand } = this.state;
        const { metaCommand } = this.props;

        if (currentSolitaryFlag) {
            return userCommand.flags[currentSolitaryFlag.properties.name] !== true &&
                (Object.keys(userCommand.options).filter(k => userCommand.options[k].filter(val => !!val).length > 0).length > 0
                    || Object.keys(userCommand.flags).filter(k => userCommand.flags[k]).length > 0)
        }
        return metaCommand.flags.filter(cf => cf.properties.is_solitary).map(cf => cf.properties.name)
            .includes(Object.keys(userCommand.flags).filter(k => userCommand.flags[k])[0]);
    }

    getGeneratedFlags(metaCommand) {
        let flagsStr = '';
        let hyphenPrefixedFlags = '';
        let otherFlags = '';
        metaCommand.flags.filter(f => this.state.userCommand.flags[f.properties.name]).forEach(f => {
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

    getGeneratedOptions(metaCommand) {
        const { userCommand } = this.state;
        let optionsStr = '';
        metaCommand.options.filter(o => (userCommand.options[o.properties.name] != null)
            && (userCommand.options[o.properties.name].filter(val => !!val).length > 0)).forEach(o => {
                const prefix = !o.properties.prefix.endsWith('=') ? `${o.properties.prefix} ` : o.properties.prefix;
                optionsStr += ` ${prefix}${userCommand.options[o.properties.name].join(' ')}`;
            });

        return optionsStr.trim();
    }

    getGeneratedCommand(metaCommand) {
        if (!metaCommand) return null;
        let commandStr = '';
        const flagsStr = this.getGeneratedFlags(metaCommand);
        const optionsStr = this.getGeneratedOptions(metaCommand);

        commandStr = `${metaCommand.properties.name} ${flagsStr} ${optionsStr}`;
        return commandStr;
    }

    handleSave(e) {
        e.preventDefault();
        const userCommand = {
            ...this.state.userCommand,
            id: this.state.userCommandId,
            commandName: this.props.metaCommand.properties.name,
            commandText: this.getGeneratedCommand(this.props.metaCommand)
        };
        this.setState({ saveInProgress: true });
        this.props.saveUserCommand(userCommand).then(() => {
            this.setState({ saveInProgress: false });
            this.props.history.push('/command/user-commands');
        });
    }

    render() {
        const { metaCommand, user } = this.props;
        const { enableEdit } = this.state;
        const userCommand = this.state.userCommand || { flags: {}, options: {} };
        const newlineRegex = /(?:\r\n|\r|\n)/g;

        const generatedCommand = this.getGeneratedCommand(metaCommand);

        return metaCommand ? (
            <div className="builder">
                <div className="header" ref={this.headerDiv}>
                    <div className="text">
                        <code>
                            {generatedCommand != null && <span>{generatedCommand}</span>}
                        </code>
                    </div>
                    <CopyToClipboard text={generatedCommand}>
                        <button className="copy" onClick={(e) => ToastMaker("Copied!")}>
                            <span className="copy-icon" title="copy">
                                <FontAwesomeIcon icon="copy" color="white" size="lg" />
                            </span><br />
                            <span className="copy-label">COPY</span>
                        </button>
                    </CopyToClipboard>

                </div>

                <div className="content" ref={this.contentDiv}>
                    <div className="title"><span>Build <span className="command-name">{metaCommand.properties.name}</span> Command</span></div>
                    <div className="category"><span>SYNTAX</span></div>
                    <div className="syntax"><code>
                        {metaCommand.properties.syntax.split(newlineRegex).map((item, index) => (
                            <span key={index}>{item.replace(/\.\.\./g, '···') /* replacing dots to avoid confusion with ellipsis */}<br /></span>
                        ))}</code><br />
                    </div>
                    <form onSubmit={(e) => this.handleSave(e)}>
                        <div className="section">
                            {metaCommand.options.length > 0 && (
                                <div className="options">
                                    <div className="category"><span>OPTIONS</span></div>
                                    <div className="fields">
                                        {metaCommand.options.map((option, i) => (
                                            <div key={i} className="row">
                                                <div className="label-col">
                                                    <label htmlFor={option.id} className={option.properties.is_mandatory === 'true' ?
                                                        'required' : ''}>{option.properties.desc}</label>
                                                </div>
                                                <div className="input-col">{option.properties.data_type === 'PERMISSION' ?
                                                    <PermissionInput id={option.id} name={option.properties.name}
                                                        handleChange={this.handleOptionChange.bind(this)}
                                                        pattern={getValidationRegex(option.properties.data_type)}
                                                        disabled={!enableEdit || this.hasSolitarySituation()}
                                                        required={option.properties.is_mandatory === 'true'}
                                                        value={userCommand.options[option.properties.name] || ['']} />
                                                    :
                                                    <DynamicTextInput id={option.id} name={option.properties.name}
                                                        handleChange={this.handleOptionChange.bind(this)}
                                                        pattern={getValidationRegex(option.properties.data_type)}
                                                        disabled={!enableEdit || this.hasSolitarySituation()}
                                                        required={option.properties.is_mandatory === 'true'}
                                                        isRepeatable={option.properties.is_repeatable === 'true'}
                                                        values={userCommand.options[option.properties.name] || ['']} />}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                    <br />
                                </div>
                            )}
                            {metaCommand.flags.length > 0 && (
                                <div className="flags">
                                    <div className="category"><span>FLAGS</span></div>
                                    <div className="fields">
                                        {metaCommand.flags.map((flag, i) => (
                                            <div key={i} className="row">
                                                <div className="label-col">
                                                    <label htmlFor={flag.id}>{flag.properties.desc}</label>
                                                </div>
                                                <div className="input-col">
                                                    <input id={flag.id} type="checkbox" name={flag.properties.name}
                                                        onChange={(e) => this.handleFlagChange(e)}
                                                        disabled={!enableEdit ||
                                                            (flag.properties.is_solitary === 'true' ? this.hasSolitarySituation(flag) : this.hasSolitarySituation())}
                                                        checked={!!userCommand.flags[flag.properties.name]} />
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
                        <div className="form-buttons no-print">
                            <button className="ripple" onClick={e => window.print()} type="button">PRINT</button>
                            {enableEdit && <button className="ripple tooltip-t"
                                {...(!user ? { 'data-tooltip': 'Login to Save' } : {})} type="submit"
                                disabled={!user || this.state.saveInProgress}>
                                {this.state.saveInProgress && <FontAwesomeIcon icon="circle-notch" spin />} SAVE
                            </button>}
                        </div>
                    </form>
                </div>
            </div>
        ) : '';
    }
}

const mapStateToProps = (state, props) => {
    return {
        metaCommand: state.metaCommandReducer.metaCommand,
        user: state.authReducer.user
    }
}

const mapDispatchToProps = dispatch => {
    return {
        getMetaCommand: (commandId) => {
            API.getMetaCommand(commandId).then(metaCommand => dispatch(getMetaCommand(metaCommand)));
        },
        getUserCommand: (userCommandId) => {
            return API.getUserCommand(userCommandId);
        },
        saveUserCommand: (userCommand) => {
            return userCommand.id ? API.updateUserCommand(userCommand) : API.saveUserCommand(userCommand);
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Builder));
