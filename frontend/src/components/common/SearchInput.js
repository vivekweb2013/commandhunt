import React, { Component } from "react";
import "./SearchInput.scss";

class SearchInput extends Component {
    constructor(props) {
        super(props);
        this.delayedOnChange = this.debounce(this.delayedOnChange.bind(this), 1000);
    }

    debounce = (fn, time) => {
        let timeout;
        return (...args) => {
            const functionCall = () => fn.apply(this, args);
            clearTimeout(timeout);
            timeout = setTimeout(functionCall, time);
        };
    }

    handleOnChange = (e) => {
        e.preventDefault();
        this.delayedOnChange(e.target.value);
    }

    delayedOnChange(value) {
        this.props.onChange(value);
    }

    handleInputReset = (e) => {
        e.preventDefault();
        this.props.onChange("");
    }

    render() {
        const { defaultValue } = this.props;
        return (
            <fieldset className="search-input">
                <input type="text" onChange={(e) => this.handleOnChange(e)} placeholder="Search..."
                    defaultValue={defaultValue} className="field" />
                <div className={"icons-container " + (defaultValue ? "icons-container-flip" : "")}>
                    <div className="icon-search"></div>
                    <div className="icon-close" onClick={(e) => this.handleInputReset(e)}> </div>
                </div>
            </fieldset>
        );
    }
}

export default SearchInput;