import React, { Component } from 'react';
import './Content.scss';
import Finder from './Finder';

class Content extends Component {
    render() {
        return (
            <div className="content">
                <Finder />
            </div>

        )
    }
}

export default Content;
