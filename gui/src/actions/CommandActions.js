export const GET_ALL_COMMANDS = "GET_ALL_COMMANDS";
export const GET_COMMAND = "GET_COMMAND";
export const GET_MATCHING_COMMANDS = "GET_MATCHING_COMMANDS";

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