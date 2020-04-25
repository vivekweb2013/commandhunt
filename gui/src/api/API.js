import auth from '../components/auth/FirebaseAuth';
import Firestore from 'firebase-firestore-lite';

const api = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

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

export const userSignUp = (signUpRequest) => {
    return fetch(`${api}/auth/signup`, { method: 'POST', body: JSON.stringify(signUpRequest), headers }).then(handleErrors);
}

export const userLogin = (loginRequest) => {
    return fetch(`${api}/auth/login`, { method: 'POST', body: JSON.stringify(loginRequest), headers }).then(handleErrors).then(async res => {
        const payload = await res.json();
        const token = payload.token;
        localStorage.setItem('token', token);
        headers.Authorization = `Bearer ${token}`;
        return payload.user;
    });
};

export const userLogout = () => {
    localStorage.removeItem('token');
    delete headers.Authorization;
    return Promise.resolve();
};

export const getUserProfile = () => fetch(`${api}/auth/user/me`, { headers }).then(handleErrors).then(res => res.json());

export const getAllCommands = () => fetch(`${api}/command`, { headers }).then(handleErrors).then(res => res.json());

export const getMatchingCommands = query => {
    query = encodeURIComponent(query);
    return fetch(`${api}/command/search?query=${query}`, { headers }).then(handleErrors).then(res => res.json());
};

export const getCommand = commandName => {
    commandName = encodeURIComponent(commandName);
    return fetch(`${api}/command/search?name=${commandName}`, { headers }).then(handleErrors).then(res => res.json());
};

export const getUserCommands = (filters) => db.reference('user-commands').query(filters).run();

export const getUserCommand = (userCommandId) => db.reference(`user-commands/${userCommandId}`).get();

export const saveUserCommand = (userCommand, userCommandId) => {
    userCommandId = userCommandId || '';
    const ref = db.reference(`user-commands/${userCommandId}`);
    return ref.set(userCommand);
};

export const deleteUserCommand = userCommand => {
    const ref = db.reference(`user-commands/${userCommand.__meta__.id}`);
    return ref.delete();
};
