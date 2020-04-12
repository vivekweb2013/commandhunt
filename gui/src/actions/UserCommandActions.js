export const GET_USER_COMMANDS = "GET_USER_COMMANDS";
export const DELETE_USER_COMMAND = "DELETE_USER_COMMAND";
export const SET_PAGINATION = "SET_PAGINATION";

export function getUserCommands(userCommands) {
    return {
        type: GET_USER_COMMANDS,
        userCommands
    };
}

export function deleteUserCommand(userCommand) {
    return {
        type: DELETE_USER_COMMAND,
        userCommand
    };
}

export function setPagination(pagination) {
    return {
        type: SET_PAGINATION,
        pagination
    };
}
