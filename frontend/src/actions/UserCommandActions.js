export const GET_USER_COMMANDS = "GET_USER_COMMANDS";

export function getUserCommands(userCommands) {
    return {
        type: GET_USER_COMMANDS,
        userCommands
    };
}