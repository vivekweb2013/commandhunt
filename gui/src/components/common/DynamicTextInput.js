import React, { Component } from 'react';

class DynamicTextInput extends Component {

    handleChange(i, event) {
        let values = this.props.values;
        values[i] = event.target.value;
        this.props.handleChange(event.target.name, values);
    }

    addClick() {
        const values = [...this.props.values, ''];
        this.props.handleChange(this.props.name, values);
    }

    removeClick(i) {
        let values = this.props.values;
        values.splice(i, 1);
        this.props.handleChange(this.props.name, values);
    }

    render() {
        const { id, name, pattern, required, disabled, values, isRepeatable } = this.props;

        return (<div>{values.map((val, i) =>
            <span key={i} className="dynamic-text-ip-wrapper">
                <input id={`${id}_${i}`} name={name} type="text" value={val || ''} onChange={this.handleChange.bind(this, i)}
                    pattern={pattern} required={required} disabled={disabled} />

                {isRepeatable && !disabled && <input type='button' value={i === 0 ? ' ＋ ' : ' － '}
                    onClick={i === 0 ? this.addClick.bind(this) : this.removeClick.bind(this, i)} />}
            </span>)}
        </div>);
    }

}

export default DynamicTextInput;