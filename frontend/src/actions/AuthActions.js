export const USER_LOGIN = "USER_LOGIN";
export const USER_LOGOUT = "USER_LOGOUT";
export const IS_MANUAL_AUTH_ALLOWED = "IS_MANUAL_AUTH_ALLOWED";

export function userLogin(user) {
    return {
        type: USER_LOGIN,
        user
    };
}

export function userLogout() {
    return {
        type: USER_LOGOUT
    };
}

export function isManualAuthAllowed(manualAuthAllowed) {
    return {
        type: IS_MANUAL_AUTH_ALLOWED,
        manualAuthAllowed
    };
}
