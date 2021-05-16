import React, { Component } from "react";
import { Link } from "react-router-dom";
import "./Sidebar.scss";

class Sidebar extends Component {
    render() {
        return (
            <div className="left-sidebar">
                <div className="sticky-sidebar">
                    <Link to="/home" key="home">Home</Link>
                    <Link className="active" to="/news" key="news">News</Link>
                    <Link to="/contact" key="contact">Contact</Link>
                    <Link to="/about" key="about">About</Link>
                </div>
            </div >
        );
    }
}

export default Sidebar;
