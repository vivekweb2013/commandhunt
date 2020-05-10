import auth from '../components/auth/FirebaseAuth';
import Firestore from 'firebase-firestore-lite';

export const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

const projectId = 'command-builder';
const db = new Firestore({ projectId, auth });

const headers = {
    Accept: "application/json",
    Authorization: 'Bearer ' + localStorage.getItem('token'),
    "Content-Type": "application/json"
};

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
    return fetch(`${API_URL}/auth/signup`, { method: 'POST', body: JSON.stringify(signUpRequest), headers }).then(handleErrors).catch(catchError);
}

export const userLogin = (loginRequest) => {
    return fetch(`${API_URL}/auth/login`, { method: 'POST', body: JSON.stringify(loginRequest), headers }).then(handleErrors).then(async res => {
        const payload = await res.json();
        const token = payload.token;
        localStorage.setItem('token', token);
        headers.Authorization = `Bearer ${token}`;
        const userPayload = payload.user;
        //TODO: Rather than transforming, set the user as it is in redux store, update the views accordingly
        const user = {
            localId: userPayload.id, displayName: userPayload.properties.name, photoUrl: null,
            email: userPayload.properties.email, emailVerified: userPayload.properties.emailVerified
        }
        return user;
    }).catch(catchError);
};

export const userLogout = () => {
    localStorage.removeItem('token');
    delete headers.Authorization;
    return Promise.resolve();
};

export const getUserProfile = (token) => {
    if (token) {
        localStorage.setItem('token', token);
        headers.Authorization = `Bearer ${token}`;
    }

    return fetch(`${API_URL}/auth/user/me`, { headers }).then(handleErrors).then(async res => {
        const payload = await res.json();
        const userPayload = payload;
        //TODO: Rather than transforming, set the user as it is in redux store, update the views accordingly
        const user = {
            localId: userPayload.id, displayName: userPayload.properties.name, photoUrl: userPayload.properties.imageUrl,
            email: userPayload.properties.email, emailVerified: userPayload.properties.emailVerified
        }
        return user;
    }).catch(catchError);
}

export const getAllCommands = () => fetch(`${API_URL}/command`, { headers }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getMatchingCommands = query => {
    query = encodeURIComponent(query);
    return fetch(`${API_URL}/command/search?query=${query}`, { headers }).then(handleErrors).then(res => res.json()).catch(catchError);
};

export const getCommand = commandName => {
    commandName = encodeURIComponent(commandName);
    return fetch(`${API_URL}/command/search?name=${commandName}`, { headers }).then(handleErrors).then(res => res.json()).catch(catchError);
};

export const getUserCommands = () => fetch(`${API_URL}/user/user-command`, { headers }).then(handleErrors).then(res => res.json()).catch(catchError);

export const getUserCommand = (userCommandId) =>
    fetch(`${API_URL}/user/user-command/${userCommandId}`, { headers }).then(handleErrors).then(res => res.json()).catch(catchError);

export const saveUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command`, {
    method: 'POST',
    body: JSON.stringify(userCommand),
    headers
}).then(handleErrors).then(res => res.json()).catch(catchError);

export const updateUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command/${userCommand.id}`, {
    method: 'PUT',
    body: JSON.stringify(userCommand),
    headers
}).then(handleErrors).then(res => res.json()).catch(catchError);

export const deleteUserCommand = (userCommand) => fetch(`${API_URL}/user/user-command/${userCommand.id}`, {
    method: 'DELETE',
    headers
}).then(handleErrors).then(res => res.json()).catch(catchError);
