$(document).ready(function () {
    $('#loginIdentifier').focus();

    $('#loginForm').on('submit', function (e) {
        e.preventDefault();

        const loginIdentifier = $('#loginIdentifier').val().trim();
        const password = $('#password').val();

        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').text('');

        let isValid = true;

        const identifierValidation = isValidLoginIdentifier(loginIdentifier);
        if (!identifierValidation.valid) {
            $('#loginIdentifier').addClass('is-invalid');
            $('#loginIdentifier').next('.form-text').next('.invalid-feedback').text('Please enter a valid email address or username');
            isValid = false;
        }

        if (password.length < 1) {
            $('#password').addClass('is-invalid');
            $('#password').closest('.input-group').next('.invalid-feedback').text('Please enter your password');
            isValid = false;
        }

        if (!isValid) return;

        if (loginIdentifier && password) {
            const $loginBtn = $('#loginBtn');
            $loginBtn.addClass('loading').text('Signing In...');

            const identifierType = identifierValidation.type === 'email' ? 'email' : 'username';
            console.log(`Attempting login with ${identifierType}: ${loginIdentifier}`);

            const apiUrl = getAuthApiUrl();
            $.ajax({
                url: `${apiUrl}/auth/login`,
                type: 'POST',
                data: JSON.stringify({
                    username: loginIdentifier,
                    password: password
                }),
                contentType: 'application/json',
                dataType: 'json',
                success: function (response) {
                    const token = response.data?.token;
                    const role = response.data?.role;
                    setCookie("token", token, 1);
                    setCookie("msgId", loginIdentifier + "MSG");

                    const proceedRedirect = () => {
                        if (typeof Swal !== 'undefined') {
                            Swal.fire({
                                icon: 'success',
                                title: 'Welcome Back!',
                                text: 'Redirecting...',
                                showConfirmButton: false,
                                timer: 800,
                                timerProgressBar: true
                            });
                        }
                        setTimeout(() => {
                            if (role === "ADMIN") {
                                window.location.href = "../pages/admindashboard.html";
                            } else if (role === "MODERATOR") {
                                window.location.href = "../pages/moderatordashboard.html";
                            } else {
                                window.location.href = "../index.html";
                            }
                        }, 600);
                    };

                    $.ajax({
                        url: `${apiUrl}/api/users/getUserByToken`,
                        type: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + token
                        },
                        success: function (userResponse) {
                            if (userResponse && userResponse.id) {
                                setCookie("userId", userResponse.id, 1);
                            }
                            proceedRedirect();
                        },
                        error: function (xhr, status, error) {
                            console.error('Failed to get user details:', error);
                            proceedRedirect();
                        }
                    });
                },
                error: function (xhr) {
                    const errorMsg = xhr.responseJSON?.message || 'Login failed. Please try again.';
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Login Failed',
                            text: errorMsg,
                            showConfirmButton: false,
                            timer: 2000
                        });
                    } else {
                        showAlert(errorMsg, 'error');
                    }
                    $loginBtn.removeClass('loading').text('Sign In');
                }
            });
        }
    });
});

function togglePassword() {
    const $passwordField = $('#password');
    const $passwordIcon = $('#passwordIcon');

    if ($passwordField.attr('type') === 'password') {
        $passwordField.attr('type', 'text');
        $passwordIcon.attr('class', 'bi bi-eye-slash');
    } else {
        $passwordField.attr('type', 'password');
        $passwordIcon.attr('class', 'bi bi-eye');
    }
}

function socialLogin(provider) {
    console.log(`Social login with ${provider}`);

    if (provider === 'google') {
        handleGoogleLogin();
    } else if (provider === 'facebook') {
        showAlert('Facebook login coming soon!', 'info');
    } else {
        showAlert(`${provider} login coming soon!`, 'info');
    }
}

function showForgotPassword() {
    console.log('Show forgot password');
    window.location.href = '../pages/fogot-password.html';
}

function showAlert(message, type, callback = null) {
    const alertClass = type === 'success' ? 'alert-success'
        : type === 'error' ? 'alert-danger'
            : 'alert-info';

    $('#alertContainer').html(`
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `);

    setTimeout(() => {
        $('#alertContainer .alert').alert('close');
        if (callback) {
            callback();
        }
    }, 2000);
}

function isValidLoginIdentifier(identifier) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailRegex.test(identifier)) {
        return {type: 'email', valid: true};
    }

    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
    if (usernameRegex.test(identifier)) {
        return {type: 'username', valid: true};
    }

    return {type: 'unknown', valid: false};
}

function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getAuthApiUrl() {
    if (typeof API_BASE_URL !== 'undefined') return API_BASE_URL;
    if (typeof window !== 'undefined' && (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')) {
        return 'http://localhost:8080';
    }
    return 'https://adlync-springboot-fullstack.onrender.com';
}