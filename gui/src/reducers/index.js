import { combineReducers } from 'redux';
import {
    GET_COMMANDS, GET_COMMAND,
    GET_USER_COMMANDS,
    USER_LOGIN, USER_LOGOUT
} from '../actions';

const commandReducer = (state = {}, action) => {
    const { commands, command } = action;
    switch (action.type) {
        case GET_COMMANDS:
            return {
                ...state,
                commands
            };
        case GET_COMMAND:
            return {
                ...state,
                command
            };
        default:
            return state;
    }
};

const userCommandReducer = (state = {}, action) => {
    const { userCommands } = action;
    switch (action.type) {
        case GET_USER_COMMANDS:
            return {
                ...state,
                userCommands
            };
        default:
            return state;
    }
};

const authReducer = (state = {}, action) => {
    const { user } = action;
    switch (action.type) {
        case USER_LOGIN:
            return {
                ...state,
                user
            };
        case USER_LOGOUT:
            return {
                ...state,
                user // user is set to null
            };
        default:
            return state;
    }
};

export default combineReducers({ authReducer, commandReducer, userCommandReducer });
