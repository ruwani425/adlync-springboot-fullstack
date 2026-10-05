// Central Application Configuration
const APP_ENV = (typeof window !== 'undefined' && window.__ENV__) ? window.__ENV__ : {};

// Base URL for Backend APIs (automatically switches between localhost and hosted backend)
const API_BASE_URL = APP_ENV.API_BASE_URL || (
    (typeof window !== 'undefined' && (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'))
        ? 'http://localhost:8080'
        : 'https://adlync-springboot-fullstack.onrender.com'
);

// ImgBB API Key for Image Uploads
const IMGBB_API_KEY = APP_ENV.IMGBB_API_KEY || "80d92b58e454a7c677c313a1c9db6d2f";

// Firebase Configuration for Google Authentication
const FIREBASE_CONFIG = APP_ENV.FIREBASE_CONFIG || {
    apiKey: atob("QUl6YVN5Q3N2bFZKWHpuaG94VXFrdk9TM2RWN1BOallPbXZqWjFj"),
    authDomain: "adlync-9f07b.firebaseapp.com",
    projectId: "adlync-9f07b",
    storageBucket: "adlync-9f07b.firebasestorage.app",
    messagingSenderId: "634898785545",
    appId: "1:634898785545:web:77d8d82fa981b23bc79fd8"
};

// Automatic Network Interceptor: Redirects hardcoded http://localhost:8080 to live backend when deployed
(function() {
    if (typeof window === 'undefined') return;

    const isLocal = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
    if (!isLocal) {
        // Intercept window.fetch
        const originalFetch = window.fetch;
        window.fetch = function(input, init) {
            if (typeof input === 'string' && input.includes('http://localhost:8080')) {
                input = input.replace('http://localhost:8080', API_BASE_URL);
            } else if (input instanceof Request && input.url.includes('http://localhost:8080')) {
                const newUrl = input.url.replace('http://localhost:8080', API_BASE_URL);
                input = new Request(newUrl, input);
            }
            return originalFetch.call(this, input, init);
        };

        // Intercept XMLHttpRequest (used by jQuery $.ajax)
        if (typeof XMLHttpRequest !== 'undefined') {
            const originalOpen = XMLHttpRequest.prototype.open;
            XMLHttpRequest.prototype.open = function(method, url, ...rest) {
                if (typeof url === 'string' && url.includes('http://localhost:8080')) {
                    url = url.replace('http://localhost:8080', API_BASE_URL);
                }
                return originalOpen.call(this, method, url, ...rest);
            };
        }
    }
})();
