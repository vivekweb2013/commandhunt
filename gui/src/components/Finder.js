import React, { Component } from 'react';
import './Finder.scss';

export default class Finder extends Component {
    render() {
        return (
            <div className="finder">
                <fieldset className="search-box-container">
                    <input type="text" placeholder="Search..." className="field" />
                    <div className="icons-container">
                        <div className="icon-search"></div>
                        <div className="icon-close"> </div>
                    </div>
                </fieldset>
            </div>
        )
    }
}
