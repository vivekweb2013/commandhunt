export const GET_COMMANDS = "GET_COMMANDS";
export const GET_COMMAND = "GET_COMMAND";

export function getCommands(commands) {
    return {
        type: GET_COMMANDS,
        commands
    };
}

export function getCommand(command) {
    return {
        type: GET_COMMAND,
        command
    };
}
