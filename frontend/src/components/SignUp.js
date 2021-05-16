import React, { Component } from "react";
import { connect } from "react-redux";
import { withRouter } from "react-router";
import Spinner from "./common/Spinner";
import * as API from "../api/API";
import "./SignUp.scss";

class SignUp extends Component {
    state = {
        signupInProgress: false,
        signUpRequest: {}
    }

    handleInputChange(event) {
        const { name, value } = event.target;
        const { signUpRequest } = this.state;

        this.setState({ signUpRequest: { ...signUpRequest, [name]: value } });
    }

    handleSignUp(e) {
        e.preventDefault();
        const { signUpRequest } = this.state;
        this.props.userSignUp(signUpRequest).then(() => {
            this.props.history.push("/login");
        });
    }
    render() {
        const { user } = this.props;
        if (user) { this.props.history.goBack(); }

        const { signupInProgress } = this.state;
        return (
            signupInProgress ? <div className="app loading" ><Spinner size="50" /> <br />LOADING</div> :
                <div className="signup">
                    <form onSubmit={(e) => this.handleSignUp(e)} className="signup">
                        <div className="container">
                            <h1>Register</h1>
                            <p>Please fill in this form to create an account.</p>
                            <hr />

                            <label htmlFor="name"><b>Name</b></label>
                            <input type="text" placeholder="Enter Name" name="name" onChange={(e) => this.handleInputChange(e)} required />

                            <label htmlFor="email"><b>Email</b></label>
                            <input type="text" placeholder="Enter Email" name="email" onChange={(e) => this.handleInputChange(e)} required />

                            <label htmlFor="password"><b>Password</b></label>
                            <input type="password" placeholder="Password" name="password" onChange={(e) => this.handleInputChange(e)} required />

                            <button type="submit" className="registerbtn">Register</button>
                        </div>
                    </form>
                </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.authReducer.user
    };
};

const mapDispatchToProps = () => {
    return {
        userSignUp: (signUpRequest) => {
            return API.userSignUp(signUpRequest);
        }
    };
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(SignUp));
