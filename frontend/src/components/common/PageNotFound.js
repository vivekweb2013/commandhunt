import React from "react";
import { Link } from "react-router-dom";
import "./PageNotFound.scss";

function PageNotFound() {
    return (
        <div className="page-not-found">
            <div className="scene">
                <div className="text-main">404 Page Not Found</div>
                <br />
                <div className="sheep">
                    <span className="top">
                        <div className="body"></div>
                        <div className="head">
                            <div className="eye one"></div>
                            <div className="eye two"></div>
                            <div className="ear one"></div>
                            <div className="ear two"></div>
                        </div>
                    </span>
                    <div className="legs">
                        <div className="leg"></div>
                        <div className="leg"></div>
                        <div className="leg"></div>
                        <div className="leg"></div>
                    </div>
                </div>
                <div className="text-note">The page you are looking for does not exist. How you got here is a mystery.
                <br />But you can click the button below to go back to the homepage.</div>
                <Link to="/" replace={true} className="home-link"><span>Go to Home</span></Link>
            </div>
        </div>
    )
}

export default PageNotFound;
