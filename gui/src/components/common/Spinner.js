import React, { Component } from 'react';
import './Spinner.scss';

export class Spinner extends Component {
    render() {
        const size = this.props.size || 20;
        const style = { height: size + 'px', width: size + 'px', borderWidth: size / 10 + 'px' };
        return (
            <div className="spinner" style={style} />
        )
    }
}

export default Spinner;
