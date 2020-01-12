import { combineReducers } from "redux";
import { GET_ALL_COMMANDS, GET_COMMAND } from "../actions";

const commandReducer = (state = {}, action) => {
    const { commands, command } = action;
    switch (action.type) {
        case GET_ALL_COMMANDS:
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


export default combineReducers({commandReducer});
