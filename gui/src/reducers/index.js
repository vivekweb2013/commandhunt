import { combineReducers } from 'redux';
import {
    GET_ALL_COMMANDS, GET_MATCHING_COMMANDS, GET_COMMAND,
    GET_USER_COMMANDS, DELETE_USER_COMMAND, SET_PAGINATION, SET_FILTERS,
    USER_LOGIN, USER_LOGOUT
} from '../actions';

const commandReducer = (state = {}, action) => {
    const { commands, command, userCommands, userCommand, pagination } = action;
    switch (action.type) {
        case GET_ALL_COMMANDS:
            return {
                ...state,
                commands
            };
        case GET_MATCHING_COMMANDS:
            return {
                ...state,
                commands
            };
        case GET_COMMAND:
            return {
                ...state,
                command
            };
        case GET_USER_COMMANDS:
            return {
                ...state,
                userCommands
            };
        case DELETE_USER_COMMAND:
            return {
                ...state,
                userCommands: state.userCommands.filter(c => c.__meta__.id !== userCommand.__meta__.id)
            };
        case SET_PAGINATION:
            return {
                ...state,
                pagination: { ...state.pagination, ...pagination }
            };
        default:
            return state;
    }
};

const userCommandReducer = (state = {}, action) => {
    const { userCommands, userCommand, filters, pagination } = action;
    switch (action.type) {
        case GET_USER_COMMANDS:
            return {
                ...state,
                userCommands
            };
        case DELETE_USER_COMMAND:
            return {
                ...state,
                userCommands: state.userCommands.filter(c => c.__meta__.id !== userCommand.__meta__.id)
            };
        case SET_FILTERS:
            return {
                ...state,
                filters: { ...state.filters, ...filters }
            }
        case SET_PAGINATION:
            return {
                ...state,
                pagination: { ...state.pagination, ...pagination }
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
