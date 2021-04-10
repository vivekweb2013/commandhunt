import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import * as API from '../api/API';
import { getMetaCommand } from '../actions';
import { getQueryParamByName, getValidationRegex } from '../Utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import DynamicTextInput from './common/DynamicTextInput';
import PermissionInput from './common/PermissionInput';
import CopyToClipboard from 'react-copy-to-clipboard';
import ToastMaker from 'toastmaker';
import './Builder.scss';
import Modal from './common/Modal';

class Builder extends Component {
    state = {
        commandInstance: {
            flags: {},
            options: {}
        },
        saveInProgress: false,
        publishInProgress: false
    }

    commandForm = React.createRef();
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

        const publicCommandId = getQueryParamByName('publicCommandId', location.search);
        const userCommandId = getQueryParamByName('userCommandId', location.search);
        if (publicCommandId != null) {
            this.props.getPublicCommand(publicCommandId).then((commandInstance) => {
                this.setState({ commandInstance });
                this.props.getMetaCommand(match.params.commandName);
            });
        } else if (userCommandId != null) {
            this.props.getUserCommand(userCommandId).then((commandInstance) => {
                this.setState({ commandInstance });
                this.props.getMetaCommand(match.params.commandName);
            });
        } else {
            this.props.getMetaCommand(match.params.commandName);
        }
    }

    handleOptionChange(name, value) {
        const commandInstance = {
            id: this.state.commandInstance.id,
            flags: {
                ...this.state.commandInstance.flags,
            },
            options: {
                ...this.state.commandInstance.options,
                [name]: value
            }
        }

        this.setState({ commandInstance });
    }

    handleFlagChange(event) {
        const { name, checked } = event.target;
        const commandInstance = {
            id: this.state.commandInstance.id,
            flags: {
                ...this.state.commandInstance.flags,
                [name]: checked
            },
            options: {
                ...this.state.commandInstance.options
            }
        }

        this.setState({ commandInstance });
    }

    hasSolitarySituation(currentSolitaryFlag) {
        // Solitary situation
        // - one of the solitary flag is enabled, because of which no other option or flag(other than selected solitary flag) can be used
        // - non solitary flag or option is used, because of which all the solitary flags should be disabled

        const { commandInstance } = this.state;
        const { metaCommand } = this.props;

        if (currentSolitaryFlag) {
            return commandInstance.flags[currentSolitaryFlag.properties.name] !== true &&
                (Object.keys(commandInstance.options).filter(k => commandInstance.options[k].filter(val => !!val).length > 0).length > 0
                    || Object.keys(commandInstance.flags).filter(k => commandInstance.flags[k]).length > 0)
        }
        return metaCommand.flags.filter(cf => cf.properties.is_solitary).map(cf => cf.properties.name)
            .includes(Object.keys(commandInstance.flags).filter(k => commandInstance.flags[k])[0]);
    }

    getGeneratedFlags(metaCommand) {
        let flagsStr = '';
        let hyphenPrefixedFlags = '';
        let otherFlags = '';
        metaCommand.flags.filter(f => this.state.commandInstance.flags[f.properties.name]).forEach(f => {
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
        const { commandInstance } = this.state;
        let optionsStr = '';
        metaCommand.options.filter(o => (commandInstance.options[o.properties.name] != null)
            && (commandInstance.options[o.properties.name].filter(val => !!val).length > 0)).forEach(o => {
                const prefix = !o.properties.prefix.endsWith('=') ? `${o.properties.prefix} ` : o.properties.prefix;
                optionsStr += ` ${prefix}${commandInstance.options[o.properties.name].join(' ')}`;
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
        const commandInstance = {
            ...this.state.commandInstance,
            commandName: this.props.metaCommand.properties.name,
            commandText: this.getGeneratedCommand(this.props.metaCommand)
        };
        this.setState({ saveInProgress: true });
        this.props.saveUserCommand(commandInstance).then(() => {
            this.setState({ saveInProgress: false });
            this.props.history.push('/command/user-commands');
        });
    }

    handlePublish(e) {
        e.stopPropagation();
        if (!this.commandForm.current.checkValidity()) {
            this.commandForm.current.reportValidity();
            return;
        }
        const commandInstance = {
            ...this.state.commandInstance,
            id: null, // Override the id
            commandName: this.props.metaCommand.properties.name,
            commandText: this.getGeneratedCommand(this.props.metaCommand)
        };
        this.setState({ publishInProgress: true });
        this.props.savePublicCommand(commandInstance).then((commandInstance) => {
            this.setState({ publishInProgress: false });
            const url = `/command/view/${commandInstance.commandName}?publicCommandId=${commandInstance.id}`;
            this.props.history.push(url, { published: true });
        });
    }

    onClosePublishModal() {
        const { location } = this.props;
        this.props.history.replace(location.pathname + location.search); // reset the action
    }

    isEditable() {
        const { location } = this.props;
        return location.pathname.startsWith('/command/build/') &&
            !getQueryParamByName('publicCommandId', location.search);
    }

    isSaveAllowed() {
        const { location } = this.props;
        return location.pathname.startsWith('/command/build/') &&
            !getQueryParamByName('publicCommandId', location.search);
    }

    isPublishAllowed() {
        const { location } = this.props;
        return location.pathname.startsWith('/command/build/') &&
            !getQueryParamByName('publicCommandId', location.search);
    }

    render() {
        const { metaCommand, user, location } = this.props;
        const enableEdit = this.isEditable();
        const showSaveButton = this.isSaveAllowed();
        const showPublishButton = this.isPublishAllowed();

        const commandInstance = this.state.commandInstance || { flags: {}, options: {} };
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
                        <button className="copy" type="button" onClick={(e) => ToastMaker("Copied!")}>
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
                    <form onSubmit={(e) => this.handleSave(e)} ref={this.commandForm}>
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
                                                        value={commandInstance.options[option.properties.name] || ['']} />
                                                    :
                                                    <DynamicTextInput id={option.id} name={option.properties.name}
                                                        handleChange={this.handleOptionChange.bind(this)}
                                                        pattern={getValidationRegex(option.properties.data_type)}
                                                        disabled={!enableEdit || this.hasSolitarySituation()}
                                                        required={option.properties.is_mandatory === 'true'}
                                                        isRepeatable={option.properties.is_repeatable === 'true'}
                                                        values={commandInstance.options[option.properties.name] || ['']} />}
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
                                                        checked={!!commandInstance.flags[flag.properties.name]} />
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
                            {showSaveButton && <button className="ripple tooltip-t" value="SAVE"
                                {...(!user ? { 'data-tooltip': 'Login to Save' } : {})} type="submit"
                                disabled={!user || this.state.saveInProgress}>
                                {this.state.saveInProgress && <FontAwesomeIcon icon="circle-notch" spin />} SAVE
                            </button>}
                            {showPublishButton && <button type="button" className="ripple tooltip-t" onClick={e => this.handlePublish(e)}
                                {...(!user ? { 'data-tooltip': 'Login to Publish' } : {})}
                                disabled={!user || this.state.publishInProgress}>
                                {this.state.publishInProgress && <FontAwesomeIcon icon="circle-notch" spin />} PUBLISH
                            </button>}
                        </div>
                    </form>
                </div>
                {location.state && location.state.published &&
                    <Modal title="Command Published" style={{ width: '90%', maxWidth: '500px' }} type="info" onClose={this.onClosePublishModal.bind(this)}>
                        <span style={{ fontSize: '1rem', lineHeight: '2rem' }}>Link to Published Command</span>
                        <span className="txt-btn-input-wrapper">
                            <input type="text" value={window.location.href} onFocus={e => e.target.select()} readOnly />
                            <CopyToClipboard text={window.location.href}>
                                <button onClick={(e) => ToastMaker("Link Copied!")} type="button" style={{ width: '45px' }}>
                                    <FontAwesomeIcon icon="copy" color="#666666" size="lg" />
                                </button>
                            </CopyToClipboard>
                        </span>
                    </Modal>}
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
        getPublicCommand: (publicCommandId) => {
            return API.getPublicCommand(publicCommandId);
        },
        saveUserCommand: (commandInstance) => {
            return commandInstance.id ? API.updateUserCommand(commandInstance) : API.saveUserCommand(commandInstance);
        },
        savePublicCommand: (commandInstance) => {
            return API.savePublicCommand(commandInstance);
        }
    }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Builder));
