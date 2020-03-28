import { combineReducers } from 'redux';
import { GET_ALL_COMMANDS, GET_MATCHING_COMMANDS, GET_COMMAND, USER_LOGIN, USER_LOGOUT } from '../actions';

const commandReducer = (state = {}, action) => {
    const { commands, command } = action;
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

export default combineReducers({ commandReducer, authReducer });
