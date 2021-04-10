export const GET_META_COMMANDS = "GET_META_COMMANDS";
export const GET_META_COMMAND = "GET_META_COMMAND";

export function getMetaCommands(metaCommands) {
    return {
        type: GET_META_COMMANDS,
        metaCommands
    };
}

export function getMetaCommand(metaCommand) {
    return {
        type: GET_META_COMMAND,
        metaCommand
    };
}
