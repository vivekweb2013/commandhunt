const api = "http://localhost:8080" || `${process.env.REACT_APP_BACKEND}`;

localStorage.token = localStorage.token || Math.random().toString(36).substring(2);

const headers = {
    Accept: "application/json",
    Authorization: localStorage.token,
    "Content-Type": "application/json"
};

export const getAllCommands = () => fetch(`${api}/command`, { headers }).then(res => res.json());

export const getCommand = commandId => fetch(`${api}/command/${commandId}`, { headers }).then(res => res.json());
