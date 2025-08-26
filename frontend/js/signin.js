// Toggle password visibility
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

// Social login
function socialLogin(provider) {
    console.log(`Social login with ${provider}`);
    showAlert(`Redirecting to ${provider} login...`, 'success');
}

// Forgot password
function showForgotPassword() {
    console.log('Show forgot password');
    showAlert('Forgot password functionality would be implemented here', 'info');
}

// Show alert
function showAlert(message, type) {
    const alertClass = type === 'success' ? 'alert-success'
        : type === 'error'   ? 'alert-danger'
            : 'alert-info';

    $('#alertContainer').html(`
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `);
}

// Validate login identifier (email or username)
function isValidLoginIdentifier(identifier) {
    // Check if it's a valid email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailRegex.test(identifier)) {
        return { type: 'email', valid: true };
    }

    // Check if it's a valid username
    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
    if (usernameRegex.test(identifier)) {
        return { type: 'username', valid: true };
    }

    return { type: 'unknown', valid: false };
}

// Handle login form submit
$('#loginForm').on('submit', function(e) {
    e.preventDefault();

    const loginIdentifier = $('#loginIdentifier').val().trim();
    const password = $('#password').val();

    // Clear previous validation
    $('.form-control').removeClass('is-invalid');
    $('.invalid-feedback').text('');

    let isValid = true;

    // Validate login identifier
    const identifierValidation = isValidLoginIdentifier(loginIdentifier);
    if (!identifierValidation.valid) {
        $('#loginIdentifier').addClass('is-invalid');
        $('#loginIdentifier').next('.form-text').next('.invalid-feedback').text('Please enter a valid email address or username');
        isValid = false;
    }

    // Validate password
    if (password.length < 1) {
        $('#password').addClass('is-invalid');
        $('#password').closest('.input-group').next('.invalid-feedback').text('Please enter your password');
        isValid = false;
    }

    if (!isValid) return;

    if (loginIdentifier && password) {
        const $loginBtn = $('#loginBtn');
        $loginBtn.addClass('loading').text('Signing In...');

        // Show what type of identifier was used
        const identifierType = identifierValidation.type === 'email' ? 'email' : 'username';
        console.log(`Attempting login with ${identifierType}: ${loginIdentifier}`);

        // Simulate login process
        setTimeout(() => {
            $loginBtn.removeClass('loading').text('Sign In');
            showAlert('Login successful! Redirecting...', 'success');
        }, 2000);
    }
});

// Auto-focus on login identifier field when page loads
$(document).ready(function() {
    $('#loginIdentifier').focus();
});