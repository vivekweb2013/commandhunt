export const GET_USER_COMMANDS = "GET_USER_COMMANDS";
export const GET_MATCHING_USER_COMMANDS = "GET_MATCHING_USER_COMMANDS";

export function getUserCommands(userCommands) {
    return {
        type: GET_USER_COMMANDS,
        userCommands
    };
}

export function getMatchingUserCommands(userCommands) {
    return {
        type: GET_MATCHING_USER_COMMANDS,
        userCommands
    };
}