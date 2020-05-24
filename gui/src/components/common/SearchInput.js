import React, { Component } from 'react';
import './SearchInput.scss';

export class SearchInput extends Component {
    constructor(props) {
        super(props);
        this.delayedOnChange = this.debounce(this.delayedOnChange.bind(this), 1000);
    }

    state = {
        query: ''
    }

    debounce = (fn, time) => {
        let timeout;
        return (...args) => {
            const functionCall = () => fn.apply(this, args);
            clearTimeout(timeout);
            timeout = setTimeout(functionCall, time);
        }
    }

    handleOnChange = (e) => {
        e.preventDefault();
        this.setState({ query: e.target.value }, () => {
            this.delayedOnChange(this.state.query);
        });
    }

    delayedOnChange(value) {
        this.props.onChange(value);
    }

    handleInputReset = (e) => {
        e.preventDefault();
        this.setState({ query: '' }, () => {
            this.props.onChange('');
        });
    }

    render() {
        return (
            <fieldset className="search-input">
                <input type="text" onChange={e => this.handleOnChange(e)} placeholder="Search..." value={this.state.query} className="field" />
                <div className={'icons-container ' + (this.state.query ? 'icons-container-flip' : '')}>
                    <div className="icon-search"></div>
                    <div className="icon-close" onClick={e => this.handleInputReset(e)}> </div>
                </div>
            </fieldset>
        )
    }
}

export default SearchInput;