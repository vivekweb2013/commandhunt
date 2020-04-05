export const GET_ALL_COMMANDS = "GET_ALL_COMMANDS";
export const GET_COMMAND = "GET_COMMAND";
export const GET_MATCHING_COMMANDS = "GET_MATCHING_COMMANDS";
export const GET_USER_COMMANDS = "GET_USER_COMMANDS";
export const DELETE_USER_COMMAND = "DELETE_USER_COMMAND";

export function getAllCommands(commands) {
    return {
        type: GET_ALL_COMMANDS,
        commands
    };
}

export function getMatchingCommands(commands) {
    return {
        type: GET_MATCHING_COMMANDS,
        commands
    };
}

export function getCommand(command) {
    return {
        type: GET_COMMAND,
        command
    };
}

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
