import { combineReducers } from 'redux';
import {
    GET_META_COMMANDS, GET_META_COMMAND,
    GET_USER_COMMANDS,
    USER_LOGIN, USER_LOGOUT, IS_MANUAL_AUTH_ALLOWED
} from '../actions';

const metaCommandReducer = (state = {}, action) => {
    const { metaCommands, metaCommand } = action;
    switch (action.type) {
        case GET_META_COMMANDS:
            return {
                ...state,
                metaCommands
            };
        case GET_META_COMMAND:
            return {
                ...state,
                metaCommand
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
    const { user, manualAuthAllowed } = action;
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
        case IS_MANUAL_AUTH_ALLOWED:
            return {
                ...state,
                manualAuthAllowed
            };
        default:
            return state;
    }
};

export default combineReducers({ authReducer, metaCommandReducer, userCommandReducer });
