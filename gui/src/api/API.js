import auth from '../components/auth/FirebaseAuth';
import Firestore from 'firebase-firestore-lite';

const api = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

const projectId = 'command-builder';
const db = new Firestore({ projectId, auth });

localStorage.token = localStorage.token || Math.random().toString(36).substring(2);

const headers = {
    Accept: "application/json",
    Authorization: localStorage.token,
    "Content-Type": "application/json"
};

export const getAllCommands = () => fetch(`${api}/command`, { headers }).then(res => res.json());

export const getMatchingCommands = query => {
    query = encodeURIComponent(query);
    return fetch(`${api}/command/search?query=${query}`, { headers }).then(res => res.json());
};

export const getCommand = commandName => {
    commandName = encodeURIComponent(commandName);
    return fetch(`${api}/command/search?name=${commandName}`, { headers }).then(res => res.json());
};

export const getUserCommands = () => db.reference('user-commands').query().run();

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