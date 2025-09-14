$(document).ready(function () {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    console.log("Token:", token);

    function validatePasswordStrength(password) {
        const errors = [];

        if (password.length < 8) {
            errors.push("Password must be at least 8 characters long");
        }
        if (password.length > 128) {
            errors.push("Password must not exceed 128 characters");
        }
        if (!/[a-z]/.test(password)) {
            errors.push("Must contain at least one lowercase letter");
        }
        if (!/[A-Z]/.test(password)) {
            errors.push("Must contain at least one uppercase letter");
        }
        if (!/[0-9]/.test(password)) {
            errors.push("Must contain at least one number");
        }
        if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
            errors.push("Must contain at least one special character");
        }
        if (/\s/.test(password)) {
            errors.push("Password must not contain spaces");
        }

        return errors;
    }

    function isWeakPassword(password) {
        const weakPasswords = [
            'password', 'password123', '12345678', 'qwerty', 'abc123',
            'password1', '123456789', 'welcome', 'admin', 'moderator'
        ];
        return weakPasswords.includes(password.toLowerCase());
    }

    $('#moderator-password').on('input', function () {
        const password = $(this).val();
        const $passwordError = $('#passwordError');

        $(this).removeClass('is-invalid is-valid');
        $passwordError.text('');

        if (password.length === 0) {
            return;
        }

        const errors = validatePasswordStrength(password);

        if (errors.length > 0) {
            $passwordError.html(errors.join('<br>'));
            $(this).addClass('is-invalid');
        } else if (isWeakPassword(password)) {
            $passwordError.text('This password is too common. Please choose a stronger password.');
            $(this).addClass('is-invalid');
        } else {
            $(this).addClass('is-valid');
        }

        if ($('#confirmPassword').val()) {
            $('#confirmPassword').trigger('input');
        }
    });

    $("#moderator-sign-up").click(function () {
        const password = $("#moderator-password").val();
        const confirmPassword = $("#confirmPassword").val();
        const $passwordError = $('#passwordError');
        const $confirmPasswordError = $('#confirmPasswordError');

        $passwordError.text('');
        $confirmPasswordError.text('');
        $('#moderator-password, #confirmPassword').removeClass('is-invalid');

        let hasError = false;

        if (!password || password.trim() === '') {
            $passwordError.text('Password is required');
            $('#moderator-password').addClass('is-invalid');
            hasError = true;
        } else {
            const passwordErrors = validatePasswordStrength(password);
            if (passwordErrors.length > 0) {
                $passwordError.html(passwordErrors.join('<br>'));
                $('#moderator-password').addClass('is-invalid');
                hasError = true;
            } else if (isWeakPassword(password)) {
                $passwordError.text('This password is too common. Please choose a stronger password.');
                $('#moderator-password').addClass('is-invalid');
                hasError = true;
            }
        }

        if (!confirmPassword || confirmPassword.trim() === '') {
            $confirmPasswordError.text('Please confirm your password');
            $('#confirmPassword').addClass('is-invalid');
            hasError = true;
        } else if (password !== confirmPassword) {
            $confirmPasswordError.text('Passwords do not match');
            $('#confirmPassword').addClass('is-invalid');
            hasError = true;
        }

        if (!hasError) {
            $.ajax({
                url: `http://localhost:8080/api/users/set-moderator-password?token=${token}&password=${password}`,
                type: "PATCH",
                success: function (response) {
                    console.log("Password set successfully!", response);
                },
                error: function (xhr) {
                    console.error("Error:", xhr.responseText);
                }
            });
        }
    });
});

function togglePassword(fieldId) {
    let $passwordField, $eyeIcon;

    if (fieldId === 'password') {
        $passwordField = $('#moderator-password');
        $eyeIcon = $('#password-eye');
    } else if (fieldId === 'confirmPassword') {
        $passwordField = $('#confirmPassword');
        $eyeIcon = $('#confirmPassword-eye');
    } else {
        $passwordField = $('#' + fieldId);
        $eyeIcon = $('#' + fieldId + '-eye');
    }

    if ($passwordField.length && $eyeIcon.length) {
        if ($passwordField.attr('type') === 'password') {
            $passwordField.attr('type', 'text');
            $eyeIcon.removeClass('bi-eye').addClass('bi-eye-slash');
        } else {
            $passwordField.attr('type', 'password');
            $eyeIcon.removeClass('bi-eye-slash').addClass('bi-eye');
        }
    }
}

$('#moderatorSignupForm').on('submit', function (e) {
    e.preventDefault();

    const password = $('#moderator-password').val();
    const confirmPassword = $('#confirmPassword').val();
    const $passwordError = $('#passwordError');
    const $confirmPasswordError = $('#confirmPasswordError');
    const $loadingSpinner = $('#loadingSpinner');

    $passwordError.text('');
    $confirmPasswordError.text('');
    $('#moderator-password').removeClass('is-invalid');
    $('#confirmPassword').removeClass('is-invalid');

    let hasError = false;

    if (!password || password.trim() === '') {
        $passwordError.text('Password is required');
        $('#moderator-password').addClass('is-invalid');
        hasError = true;
    } else {
        const passwordErrors = validatePasswordStrength(password);
        if (passwordErrors.length > 0) {
            $passwordError.html(passwordErrors.join('<br>'));
            $('#moderator-password').addClass('is-invalid');
            hasError = true;
        } else if (isWeakPassword(password)) {
            $passwordError.text('This password is too common. Please choose a stronger password.');
            $('#moderator-password').addClass('is-invalid');
            hasError = true;
        }
    }

    if (!confirmPassword || confirmPassword.trim() === '') {
        $confirmPasswordError.text('Please confirm your password');
        $('#confirmPassword').addClass('is-invalid');
        hasError = true;
    } else if (password !== confirmPassword) {
        $confirmPasswordError.text('Passwords do not match');
        $('#confirmPassword').addClass('is-invalid');
        hasError = true;
    }

    if (!hasError) {
        $loadingSpinner.removeClass('d-none');
        setTimeout(() => {
            $loadingSpinner.addClass('d-none');
            alert('Sign up successful! Redirecting to moderator dashboard...');
        }, 2000);
    }
});

$('#confirmPassword').on('input', function () {
    const password = $('#moderator-password').val();
    const confirmPassword = $(this).val();
    const $confirmPasswordError = $('#confirmPasswordError');

    $(this).removeClass('is-invalid is-valid');
    $confirmPasswordError.text('');

    if (!confirmPassword) {
        return;
    }

    if (!password) {
        $confirmPasswordError.text('Please enter your password first');
        $(this).addClass('is-invalid');
    } else if (password !== confirmPassword) {
        $confirmPasswordError.text('Passwords do not match');
        $(this).addClass('is-invalid');
    } else {
        $(this).addClass('is-valid');
    }
});
