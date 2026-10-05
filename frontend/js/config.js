// Central Application Configuration
const APP_ENV = (typeof window !== 'undefined' && window.__ENV__) ? window.__ENV__ : {};

// Base URL for Backend APIs (automatically switches between localhost and hosted backend)
const API_BASE_URL = APP_ENV.API_BASE_URL || (
    (typeof window !== 'undefined' && (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'))
        ? 'http://localhost:8080'
        : 'https://adlync-backend.onrender.com'
);

// ImgBB API Key for Image Uploads
const IMGBB_API_KEY = APP_ENV.IMGBB_API_KEY || "";

// Firebase Configuration for Google Authentication
const FIREBASE_CONFIG = APP_ENV.FIREBASE_CONFIG || {
    apiKey: "",
    authDomain: "",
    projectId: "",
    storageBucket: "",
    messagingSenderId: "",
    appId: ""
};
