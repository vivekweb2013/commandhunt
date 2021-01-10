import { getQueryParamsFromFilter } from '../Utils';

export const API_URL = process.env.NODE_ENV === 'development' ? "http://localhost:8080/api" : '/api';

const getHeaders = () => {
    return {
        Accept: "application/json",
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        "Content-Type": "application/json"
    }
}

const handleErrors = (response) => {
    // the fetch() API only rejects a promise when a “network error is encountered, although this usually means permissions issues or similar.”
    // fetch provides a simple ok flag that indicates whether an HTTP response’s status code is in the successful range or not
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

const catchError = (error) => {
    console.log(error);
}

export const userSignUp = (signUpRequest) => {
    return fetch(`${API_URL}/auth/signup`, { method: 'POST', body: JSON.stringify(signUpRequest), headers: getHeaders() }).then(handleErrors).catch(catchError);
}

export const userLogin = (loginRequest) => {
    return fetch(`${API_URL}/auth/login`, { method: 'POST', body: JSON.stringify(loginRequest), headers: getHeaders() }).then(handleErrors).then(async res => {
        const payload = await res.json();
        const token = payload.token;
        localStorage.setItem('token', token);
        const user = payload.user;
        return user;
    }).catch(catchError);
};

export const userLogout = () => {
    localStorage.removeItem('token');
    return Promise.resolve();
};

export const getUserProfile = (token) => {
    if (token) {
        localStorage.setItem('token', token);
    }

    return fetch(`${API_URL}/auth/user/me`, { headers: getHeaders() }).then(handleErrors).then(async res => {
        const user = await res.json();
        return user;
    }).catch(catchError);
}

export const getCommands = (filter) => fetch(`${API_URL}/command` + getQueryParamsFromFilter(filter),
    { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getCommand = commandName => {
    commandName = encodeURIComponent(commandName);
    return fetch(`${API_URL}/command/search?name=${commandName}`, { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);
};

export const getUserCommands = (filter) => fetch(`${API_URL}/user/user-command` + getQueryParamsFromFilter(filter),
    { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getUserCommand = (userCommandId) =>
    fetch(`${API_URL}/user/user-command/${userCommandId}`, { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const saveUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command`, {
    method: 'POST',
    body: JSON.stringify(userCommand),
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);

export const updateUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command/${userCommand.id}`, {
    method: 'PUT',
    body: JSON.stringify(userCommand),
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);

export const deleteUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command/${userCommand.id}`, {
    method: 'DELETE',
    headers: getHeaders()
}).then(handleErrors).then(res => res.json()).catch(catchError);
