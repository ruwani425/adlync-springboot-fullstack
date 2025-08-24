// Social signup handlers
function socialSignup(provider) {
    showAlert(`${provider.charAt(0).toUpperCase() + provider.slice(1)} signup initiated...`, 'info');

    // Set loading state
    const $btn = $(`.btn-${provider}`);
    $btn.addClass('loading').css('pointerEvents', 'none');

    // Simulate social login process
    setTimeout(() => {
        $btn.removeClass('loading').css('pointerEvents', 'auto');

        // Simulate success
        showAlert(`Successfully signed up with ${provider}! Redirecting...`, 'success');

        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    }, 2500);

    // In real implementation, you would integrate with:
    // Google: Google OAuth 2.0 / Google Sign-In
    // Facebook: Facebook Login SDK
}

// Form validation and submission
$('#signupForm').on('submit', function(e) {
    e.preventDefault();

    const formData = {
        fullName: $('#fullName').val().trim(),
        email: $('#email').val().trim(),
        phone: $('#phone').val().trim(),
        password: $('#password').val(),
        confirmPassword: $('#confirmPassword').val(),
        termsAccepted: $('#termsCheck').is(':checked')
    };

    // Clear previous alerts and validation
    clearAlerts();
    clearValidation();

    // Validate form
    let isValid = true;

    if (formData.fullName.length < 2) {
        showFieldError('fullName', 'Full name must be at least 2 characters long');
        isValid = false;
    }

    if (!isValidEmail(formData.email)) {
        showFieldError('email', 'Please enter a valid email address');
        isValid = false;
    }

    if (formData.phone && !isValidPhone(formData.phone)) {
        showFieldError('phone', 'Please enter a valid Sri Lankan phone number');
        isValid = false;
    }

    if (formData.password.length < 8) {
        showFieldError('password', 'Password must be at least 8 characters long');
        isValid = false;
    }

    if (formData.password !== formData.confirmPassword) {
        showFieldError('confirmPassword', 'Passwords do not match');
        isValid = false;
    }

    if (!formData.termsAccepted) {
        showAlert('Please accept the Terms of Service and Privacy Policy', 'danger');
        isValid = false;
    }

    if (!isValid) return;

    // Show loading state
    setLoading(true);

    // Simulate signup process
    setTimeout(() => {
        showAlert('Account created successfully! Welcome to Adlync!', 'success');

        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    }, 2500);
});

// Password strength checker
$('#password').on('input', function() {
    const password = $(this).val();
    const strength = checkPasswordStrength(password);
    updatePasswordStrength(strength);
});

function checkPasswordStrength(password) {
    let score = 0;

    if (password.length >= 8) score++;
    if (/[a-z]/.test(password)) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    if (score <= 2) return 'weak';
    if (score <= 3) return 'medium';
    return 'strong';
}

function updatePasswordStrength(strength) {
    const $strengthText = $('#strengthText');
    const $strengthFill = $('#strengthFill');

    $strengthFill.removeClass('strength-weak strength-medium strength-strong')
        .addClass(`strength-${strength}`);
    $strengthText.text(strength.charAt(0).toUpperCase() + strength.slice(1));
}

// Utility functions
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function isValidPhone(phone) {
    // Sri Lankan phone number validation
    const phoneRegex = /^(\+94|0)?[1-9][0-9]{8}$/;
    return phoneRegex.test(phone.replace(/\s+/g, ''));
}

function showAlert(message, type = 'info') {
    const $alertContainer = $('#alertContainer');
    const $alertDiv = $('<div>');

    let iconClass = 'bi-info-circle';
    if (type === 'success') iconClass = 'bi-check-circle';
    if (type === 'danger') iconClass = 'bi-exclamation-triangle';

    $alertDiv.addClass(`alert alert-${type} d-flex align-items-center`)
        .html(`
                <i class="bi ${iconClass} me-2"></i>
                <span>${message}</span>
            `);

    $alertContainer.append($alertDiv);

    // Auto-remove alert after 5 seconds
    setTimeout(() => {
        $alertDiv.remove();
    }, 5000);
}

function clearAlerts() {
    $('#alertContainer').empty();
}

function showFieldError(fieldId, message) {
    const $field = $(`#${fieldId}`);
    const $feedback = $field.next('.invalid-feedback');

    $field.addClass('is-invalid');
    if ($feedback.length) {
        $feedback.text(message);
    }
}

function clearValidation() {
    $('.form-control').removeClass('is-invalid')
        .next('.invalid-feedback').text('');
}

function setLoading(loading) {
    const $form = $('#signupForm');
    const $btn = $('#signupBtn');

    if (loading) {
        $form.addClass('loading');
        $btn.addClass('loading').text('');
    } else {
        $form.removeClass('loading');
        $btn.removeClass('loading').text('Sign Up');
    }
}

function showTerms() {
    showAlert('Terms of Service would open in a new window', 'info');
}

function showPrivacy() {
    showAlert('Privacy Policy would open in a new window', 'info');
}

// Auto-focus on full name field when page loads
$(document).ready(function() {
    $('#fullName').focus();
});