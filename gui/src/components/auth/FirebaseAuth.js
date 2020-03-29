import Auth from 'firebase-oauth-lite';

const firebaseConfig = {
    apiKey: process.env.API_KEY,
    redirectURL: 'http://localhost:3000/login',
    providers: ['google.com', 'facebook.com', 'twitter.com', 'github.com']
};

const FirebaseAuth = new Auth(firebaseConfig);

export default FirebaseAuth;
