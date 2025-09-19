const firebaseConfig = {
    apiKey: "AIzaSyCP8U0SU_P6mQ7YuYdlfFI8AJS_cO8lTsA",
    authDomain: "g-exxplore.firebaseapp.com",
    databaseURL: "https://g-exxplore-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "g-exxplore",
    storageBucket: "g-exxplore.appspot.com",
    messagingSenderId: "171368449973",
    appId: "1:171368449973:web:93e92f9f028dad13503bfc",
    measurementId: "G-SNSBKHN49T"
};

firebase.initializeApp(firebaseConfig);
const storage = firebase.storage();
const auth = firebase.auth();

// Configure Google Auth Provider
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
    console.log(selected)
    if (selected.length === 0) {
        console.log(selectedFiles);
        return [];
    }

    const uploadPromises = [];

    for (let i = 0; i < selected.length; i++) {
        console.log("//////////////////////////////////////////////////////////")
        const file = selected[i];
        const fileName = `animals/${Date.now()}_${i}_${file.name}`;
        const storageRef = storage.ref(fileName);

        const uploadTask = storageRef.put(file);

        uploadPromises.push(
            new Promise((resolve, reject) => {
                uploadTask.on('state_changed',
                    (snapshot) => {
                        const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                        const overallProgress = ((i / selected.length) * 100) + (progress / selected.length);

                        // progressFill.style.width = overallProgress + '%';
                        // progressText.textContent = `Uploading images... ${Math.round(overallProgress)}%`;
                        console.log(overallProgress)
                    },
                    (error) => {
                        console.error('Upload failed:', error);
                        reject(error);
                    },
                    () => {
                        uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
                            resolve(downloadURL);
                        });
                    }
                );
            })
        );
    }

    try {
        const urls = await Promise.all(uploadPromises);
        uploadedImageUrls = urls;
        return urls;
    } catch (error) {
        throw error;
    }
}

function validateImages(files) {
    const maxSize = 5 * 1024 * 1024; // 5MB
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

// Add this function to your firebase.js file

async function uploadProfilePhotoToFirebase(file) {
    if (!file) {
        throw new Error('No file selected');
    }

    // Validate file
    const maxSize = 5 * 1024 * 1024; // 5MB
    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (file.size > maxSize) {
        throw new Error('File size exceeds 5MB limit');
    }

    if (!allowedTypes.includes(file.type)) {
        throw new Error('Invalid file type. Only JPG, PNG, and WebP are allowed');
    }

    const fileName = `profile-photos/${Date.now()}_${file.name}`;
    const storageRef = storage.ref(fileName);
    const uploadTask = storageRef.put(file);

    return new Promise((resolve, reject) => {
        uploadTask.on('state_changed',
            (snapshot) => {
                const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;

                // Update progress bar
                const progressBar = document.querySelector('#uploadProgress .progress-bar');
                if (progressBar) {
                    progressBar.style.width = progress + '%';
                }

                console.log(`Upload progress: ${Math.round(progress)}%`);
            },
            (error) => {
                console.error('Upload failed:', error);
                reject(error);
            },
            () => {
                uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
                    console.log('Profile photo uploaded successfully:', downloadURL);
                    resolve(downloadURL);
                }).catch(reject);
            }
        );
    });
}

// Google Authentication Functions
async function signInWithGoogle() {
    try {
        const result = await auth.signInWithPopup(googleProvider);
        const user = result.user;

        console.log('Google sign-in successful:', user);

        // Extract user information
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

// Google Login - Only for Login Page
async function handleGoogleLogin() {
    try {
        // Show loading state
        const googleBtn = document.querySelector('button[onclick="socialLogin(\'google\')"]');
        if (googleBtn) {
            googleBtn.disabled = true;
            googleBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Signing in...';
        }

        // Sign in with Google
        const googleUser = await signInWithGoogle();

        // Prepare login data for existing backend endpoint
        const loginData = {
            username: googleUser.email, // Use full email as username for login
            password: googleUser.email // Using email as password
        };

        console.log('Attempting Google login:', loginData);

        // Only try to login (don't register)
        const loginResponse = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        if (loginResponse.ok) {
            const loginResult = await loginResponse.json();
            console.log('Google login successful:', loginResult);

            // Handle successful login
            handleSuccessfulAuth(loginResult, 'login');
        } else {
            // Login failed - user not found
            throw new Error('User not found. Please register first or use a different account.');
        }

    } catch (error) {
        console.error('Google login error:', error);

        // Show user-friendly error message
        if (error.message.includes('User not found')) {
            alert('Account not found!\n\nThis Google account is not registered in our system.\nPlease go to the registration page to create an account first.');
        } else {
            alert('Google login failed: ' + error.message);
        }
    } finally {
        // Reset button state
        resetGoogleButton('socialLogin');
    }
}

// Google Registration - Only for Registration Page
async function handleGoogleRegistration() {
    try {
        // Show loading state
        const googleBtn = document.querySelector('button[onclick="socialSignup(\'google\')"]');
        if (googleBtn) {
            googleBtn.disabled = true;
            googleBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Registering...';
        }

        // Sign in with Google
        const googleUser = await signInWithGoogle();

        // Prepare registration data
        const registerData = {
            password: googleUser.email, // Using email as password
            username: googleUser.email, // Use full email as username for consistency
            role: 'USER', // Default role for Google users
            name: googleUser.displayName || googleUser.email.split('@')[0],
            email: googleUser.email,
            status: 'ACTIVE', // Default status for new users
            joinDate: new Date().toISOString() // Current date/time
        };

        console.log('Attempting Google registration:', registerData);

        // Only try to register (don't login)
        const registerResponse = await fetch('http://localhost:8080/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        });

        if (registerResponse.ok) {
            const registerResult = await registerResponse.json();
            console.log('Google registration successful:', registerResult);

            // Handle successful registration
            handleSuccessfulAuth(registerResult, 'register');
        } else {
            const errorText = await registerResponse.text();
            let errorMessage = 'Registration failed';
            try {
                const errorData = JSON.parse(errorText);
                errorMessage = errorData.message || errorMessage;
            } catch (e) {
                errorMessage = errorText || errorMessage;
            }

            // Check if user already exists
            if (errorMessage.includes('already exists') || errorMessage.includes('duplicate')) {
                throw new Error('Account already exists. Please go to the login page to sign in.');
            } else {
                throw new Error(errorMessage);
            }
        }

    } catch (error) {
        console.error('Google registration error:', error);

        // Show user-friendly error message
        if (error.message.includes('already exists')) {
            alert('Account already exists!\n\nThis Google account is already registered.\nPlease go to the login page to sign in.');
        } else {
            alert('Google registration failed: ' + error.message);
        }
    } finally {
        // Reset button state
        resetGoogleButton('socialSignup');
    }
}

// Helper function to reset Google button state
function resetGoogleButton(functionType) {
    const selector = functionType === 'socialLogin' ?
        'button[onclick="socialLogin(\'google\')"]' :
        'button[onclick="socialSignup(\'google\')"]';

    const googleBtn = document.querySelector(selector);
    if (googleBtn) {
        googleBtn.disabled = false;
        googleBtn.innerHTML = `
            <svg class="google-icon" viewBox="0 0 24 24">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285f4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34a853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#fbbc05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#ea4335"/>
            </svg>
            Google
        `;
    }
}

function handleSuccessfulAuth(result, type) {
    // This function will be called from signin.js
    // Store token and user data
    if (result.data && result.data.token) {
        setCookie("token", result.data.token, 1);

        // Get user ID from token and store it
        getUserIdFromToken(result.data.token).then(userId => {
            if (userId) {
                setCookie("userId", userId, 1);
            }

            // Show success message
            if (type === 'register') {
                alert('Google registration successful! Welcome to Adlync!');
            } else {
                alert('Google login successful! Welcome back!');
            }

            // Redirect to home page (handle both signin and signup page locations)
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
        const response = await fetch('http://localhost:8080/api/users/getUserByToken', {
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

// Cookie helper function
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`;
}