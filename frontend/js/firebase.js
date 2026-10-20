// Use central configuration or fallback to local environment
const firebaseConfig = (typeof FIREBASE_CONFIG !== 'undefined' && FIREBASE_CONFIG.apiKey) 
    ? FIREBASE_CONFIG 
    : (window.__ENV__?.FIREBASE_CONFIG || {});

firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();

const activeImgbbKey = (typeof IMGBB_API_KEY !== 'undefined' && IMGBB_API_KEY) 
    ? IMGBB_API_KEY 
    : (window.__ENV__?.IMGBB_API_KEY || "");



const googleProvider = new firebase.auth.GoogleAuthProvider();
googleProvider.addScope('email');
googleProvider.addScope('profile');

let selectedFiles = [];
let uploadedImageUrls = [];

function handleFileSelection(files) {
    selectedFiles = Array.from(files);
    displayImagePreviews();
}

function displayImagePreviews() {
    const previewContainer = document.getElementById('imagePreviewContainer');
    previewContainer.innerHTML = '';

    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const previewDiv = document.createElement('div');
            previewDiv.className = 'image-preview';
            previewDiv.innerHTML = `
                <img src="${e.target.result}" alt="Preview ${index + 1}">
                <button type="button" class="remove-image" onclick="removeImage(${index})">×</button>
            `;
            previewContainer.appendChild(previewDiv);
        };
        reader.readAsDataURL(file);
    });
}

function removeImage(index) {
    selectedFiles.splice(index, 1);
    displayImagePreviews();
}

async function uploadImagesToFirebase(selected) {
    if (!selected || selected.length === 0) {
        return [];
    }

    const uploadPromises = Array.from(selected).map(async (file, index) => {
        const formData = new FormData();
        formData.append('image', file);

        const response = await fetch(`https://api.imgbb.com/1/upload?key=${activeImgbbKey}`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`Failed to upload image ${index + 1}: ${response.statusText}`);
        }

        const result = await response.json();
        if (result.success && result.data && result.data.url) {
            console.log(`Image ${index + 1} uploaded to ImgBB:`, result.data.url);
            return result.data.url;
        } else {
            throw new Error(result.error?.message || 'ImgBB upload failed');
        }
    });

    try {
        const urls = await Promise.all(uploadPromises);
        uploadedImageUrls = urls;
        return urls;
    } catch (error) {
        console.error('Image upload failed:', error);
        throw error;
    }
}

function validateImages(files) {
    const maxSize = 5 * 1024 * 1024;
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

    for (let file of files) {
        if (file.size > maxSize) {
            alert(`File "${file.name}" is too large. Maximum size is 5MB.`);
            return false;
        }

        if (!allowedTypes.includes(file.type)) {
            alert(`File "${file.name}" is not a valid image type. Allowed types: JPEG, PNG, GIF, WebP.`);
            return false;
        }
    }

    if (files.length > 5) {
        alert('Maximum 5 images allowed.');
        return false;
    }

    return true;
}


async function uploadProfilePhotoToFirebase(file) {
    if (!file) {
        throw new Error('No file selected');
    }

    const maxSize = 5 * 1024 * 1024;
    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (file.size > maxSize) {
        throw new Error('File size exceeds 5MB limit');
    }

    if (!allowedTypes.includes(file.type)) {
        throw new Error('Invalid file type. Only JPG, PNG, and WebP are allowed');
    }

    const formData = new FormData();
    formData.append('image', file);

    const progressBar = document.querySelector('#uploadProgress .progress-bar');
    if (progressBar) {
        progressBar.style.width = '50%';
    }

    const response = await fetch(`https://api.imgbb.com/1/upload?key=${activeImgbbKey}`, {
        method: 'POST',
        body: formData
    });

    if (progressBar) {
        progressBar.style.width = '100%';
    }

    if (!response.ok) {
        throw new Error(`Profile photo upload failed: ${response.statusText}`);
    }

    const result = await response.json();
    if (result.success && result.data && result.data.url) {
        console.log('Profile photo uploaded to ImgBB:', result.data.url);
        return result.data.url;
    } else {
        throw new Error(result.error?.message || 'ImgBB upload failed');
    }
}

async function signInWithGoogle() {
    try {
        const result = await auth.signInWithPopup(googleProvider);
        const user = result.user;

        console.log('Google sign-in successful:', user);

        const userData = {
            uid: user.uid,
            email: user.email,
            displayName: user.displayName,
            photoURL: user.photoURL,
            emailVerified: user.emailVerified
        };

        return userData;
    } catch (error) {
        console.error('Google sign-in error:', error);
        throw error;
    }
}

function getAuthApiUrl() {
    if (typeof API_BASE_URL !== 'undefined') return API_BASE_URL;
    if (typeof window !== 'undefined' && (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')) {
        return 'http://localhost:8080';
    }
    return 'https://adlync-springboot-fullstack.onrender.com';
}

async function handleGoogleLogin() {
    try {
        const googleBtn = document.querySelector('button[onclick="socialLogin(\'google\')"]');
        if (googleBtn) {
            googleBtn.disabled = true;
            googleBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Signing in...';
        }

        const googleUser = await signInWithGoogle();
        const apiUrl = getAuthApiUrl();

        const loginData = {
            username: googleUser.email,
            password: googleUser.email
        };

        console.log('Attempting Google login:', loginData);

        let loginResponse = await fetch(`${apiUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        // If user not found, auto-register and retry login seamlessly
        if (!loginResponse.ok) {
            console.log('User not found on login, attempting auto-registration...');
            const registerData = {
                password: googleUser.email,
                username: googleUser.email,
                role: 'USER',
                name: googleUser.displayName || googleUser.email.split('@')[0],
                email: googleUser.email,
                status: 'ACTIVE',
                joinDate: new Date().toISOString()
            };

            const registerResponse = await fetch(`${apiUrl}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registerData)
            });

            if (registerResponse.ok) {
                loginResponse = await fetch(`${apiUrl}/auth/login`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });
            }
        }

        if (loginResponse.ok) {
            const loginResult = await loginResponse.json();
            console.log('Google login successful:', loginResult);
            handleSuccessfulAuth(loginResult, 'login');
        } else {
            throw new Error('Google sign in failed. Please try again.');
        }

    } catch (error) {
        console.error('Google login error:', error);
        alert('Google login failed: ' + error.message);
    } finally {
        resetGoogleButton('socialLogin');
    }
}

async function handleGoogleRegistration() {
    try {
        const googleBtn = document.querySelector('button[onclick="socialSignup(\'google\')"]');
        if (googleBtn) {
            googleBtn.disabled = true;
            googleBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Registering...';
        }

        const googleUser = await signInWithGoogle();
        const apiUrl = getAuthApiUrl();

        const registerData = {
            password: googleUser.email,
            username: googleUser.email,
            role: 'USER',
            name: googleUser.displayName || googleUser.email.split('@')[0],
            email: googleUser.email,
            status: 'ACTIVE',
            joinDate: new Date().toISOString()
        };

        console.log('Attempting Google registration:', registerData);

        const registerResponse = await fetch(`${apiUrl}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        });

        // If registration failed, check if account already exists
        if (!registerResponse.ok) {
            const errorText = await registerResponse.text();
            let errorMessage = 'Registration failed';
            try {
                const errorData = JSON.parse(errorText);
                errorMessage = errorData.message || errorMessage;
            } catch (e) {
                errorMessage = errorText || errorMessage;
            }

            if (!errorMessage.includes('already exists') && !errorMessage.includes('duplicate')) {
                throw new Error(errorMessage);
            }
            console.log('Account already exists for this Google user, continuing with auto-login...');
        } else {
            console.log('Google registration successful, proceeding to auto-login...');
        }

        // Auto-login to obtain JWT token and log user in immediately
        if (googleBtn) {
            googleBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Logging in...';
        }

        const loginData = {
            username: googleUser.email,
            password: googleUser.email
        };

        const loginResponse = await fetch(`${apiUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        if (loginResponse.ok) {
            const loginResult = await loginResponse.json();
            console.log('Google auto-login successful:', loginResult);
            handleSuccessfulAuth(loginResult, 'register');
        } else {
            throw new Error('Registration was completed, but automatic login failed. Please sign in.');
        }

    } catch (error) {
        console.error('Google registration error:', error);
        alert('Google registration failed: ' + error.message);
    } finally {
        resetGoogleButton('socialSignup');
    }
}

function resetGoogleButton(functionType) {
    const selector = functionType === 'socialLogin' ?
        'button[onclick="socialLogin(\'google\')"]' :
        'button[onclick="socialSignup(\'google\')"]';

    const googleBtn = document.querySelector(selector);
    if (googleBtn) {
        googleBtn.disabled = false;
        const buttonText = functionType === 'socialLogin' ? 'Google' : 'Continue with Google';
        googleBtn.innerHTML = `
            <svg class="google-icon" width="20" height="20" viewBox="0 0 24 24" style="width: 20px; height: 20px; min-width: 20px; min-height: 20px; vertical-align: middle; flex-shrink: 0;">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285f4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34a853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#fbbc05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#ea4335"/>
            </svg>
            ${buttonText}
        `;
    }
}

function handleSuccessfulAuth(result, type) {
    if (result.data && result.data.token) {
        setCookie("token", result.data.token, 1);

        getUserIdFromToken(result.data.token).then(userId => {
            if (userId) {
                setCookie("userId", userId, 1);
            }

            if (type === 'register') {
                alert('Google registration successful! Welcome to Adlync!');
            } else {
                alert('Google login successful! Welcome back!');
            }

            const currentPath = window.location.pathname;
            if (currentPath.includes('/pages/')) {
                window.location.href = '../index.html';
            } else {
                window.location.href = 'index.html';
            }
        });
    }
}

async function getUserIdFromToken(token) {
    try {
        const apiUrl = getAuthApiUrl();
        const response = await fetch(`${apiUrl}/api/users/getUserByToken`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const userData = await response.json();
            return userData.id;
        }
    } catch (error) {
        console.error('Error getting user ID from token:', error);
    }
    return null;
}

function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`;
}