export const GET_ALL_COMMANDS = "GET_ALL_COMMANDS";
export const GET_COMMAND = "GET_COMMAND";

export function getAllCommands(commands) {
    return {
        type: GET_ALL_COMMANDS,
        commands
    };
}
export function getCommand(command) {
    return {
        type: GET_COMMAND,
        command
    };
}