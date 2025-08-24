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

// Handle login form submit
$('#loginForm').on('submit', function(e) {
    e.preventDefault();

    const email = $('#email').val();
    const password = $('#password').val();

    if (email && password) {
        const $loginBtn = $('#loginBtn');
        $loginBtn.addClass('loading').text('Signing In...');

        // Simulate login process
        setTimeout(() => {
            $loginBtn.removeClass('loading').text('Sign In');
            showAlert('Login successful! Redirecting...', 'success');
        }, 2000);
    }
});
