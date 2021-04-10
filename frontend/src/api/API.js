import { getQueryParamsFromFilter } from '../Utils';

// when we start the application using 'npm start' then process.env.NODE_ENV will be automatically set to 'development'
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

export const userLogout = () => {
    localStorage.removeItem('token');
    return Promise.resolve();
};

export const getUserProfile = (token) => {
    if (token) {
        localStorage.setItem('token', token);
    }

    // There is no point in calling profile api without token, since it will fail without token
    // So in case of page reload etc we call this endpoint only when there is token in local-storage, which implies
    // that user has already logged-in
    return localStorage.getItem('token') ? fetch(`${API_URL}/auth/user/me`, { headers: getHeaders() }).then(async res => {
        if (!res.ok) {
            console.log('User session has been expired. Login again!');
            localStorage.removeItem('token');
            return null;
        }
        const user = await res.json();
        return user;
    }).catch(catchError) : Promise.resolve();
}

export const isManualAuthAllowed = () => fetch(`${API_URL}/auth/isManualAuthAllowed`).then(handleErrors).then(res => res.text()).catch(catchError);

export const getMetaCommands = (filter) => fetch(`${API_URL}/meta-command` + getQueryParamsFromFilter(filter),
    { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getMetaCommand = commandName => {
    commandName = encodeURIComponent(commandName);
    return fetch(`${API_URL}/meta-command/search?name=${commandName}`, { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);
};

export const getUserCommands = (filter) => fetch(`${API_URL}/user/user-command` + getQueryParamsFromFilter(filter),
    { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getUserCommand = (userCommandId) =>
    fetch(`${API_URL}/user/user-command/${userCommandId}`, { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getPublicCommand = (publicCommandId) =>
    fetch(`${API_URL}/public/public-command/${publicCommandId}`, { headers: getHeaders() }).then(handleErrors).then(res => res.json()).catch(catchError);

export const saveUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command`, {
    method: 'POST',
    body: JSON.stringify(userCommand),
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);

export const savePublicCommand = (publicCommand) => fetch(`${API_URL}/public/public-command`, {
    method: 'POST',
    body: JSON.stringify(publicCommand),
    headers: getHeaders()
}).then(handleErrors).then(res => res.json()).catch(catchError);

export const updateUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command/${userCommand.id}`, {
    method: 'PUT',
    body: JSON.stringify(userCommand),
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);

export const deleteUserCommand = (userCommandId) => fetch(`${API_URL}/user/user-command/${userCommandId}`, {
    method: 'DELETE',
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);

export const deletePublicCommand = (publicCommandId) => fetch(`${API_URL}/public/public-command/${publicCommandId}`, {
    method: 'DELETE',
    headers: getHeaders()
}).then(handleErrors).then(res => res.text()).catch(catchError);
