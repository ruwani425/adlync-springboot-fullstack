class ForgotPasswordManager {
    constructor() {
        this.currentStep = 1;
        this.userEmail = '';
        this.otpTimer = null;
        this.resendTimer = null;
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupOTPInputs();
        this.setupPasswordValidation();
    }

    setupEventListeners() {
        // Email form submission
        $('#emailForm').on('submit', (e) => this.handleEmailSubmit(e));

        // OTP form submission
        $('#otpForm').on('submit', (e) => this.handleOTPSubmit(e));

        // Password form submission
        $('#passwordForm').on('submit', (e) => this.handlePasswordSubmit(e));

        // Navigation
        $('#backToEmail').on('click', (e) => {
            e.preventDefault();
            this.goToStep(1);
        });

        // Resend OTP
        $('#resendBtn').on('click', () => this.resendOTP());

        // Password visibility toggles
        $('#togglePassword1').on('click', () => this.togglePasswordVisibility('newPassword', 'togglePassword1'));
        $('#togglePassword2').on('click', () => this.togglePasswordVisibility('confirmPassword', 'togglePassword2'));
    }

    setupOTPInputs() {
        $('.otp-input').on('input', function (e) {
            const value = e.target.value;
            if (value && /^[0-9]$/.test(value)) {
                const nextInput = $(this).next('.otp-input');
                if (nextInput.length) {
                    nextInput.focus();
                }
            }
        });

        $('.otp-input').on('keydown', function (e) {
            if (e.key === 'Backspace' && !e.target.value) {
                const prevInput = $(this).prev('.otp-input');
                if (prevInput.length) {
                    prevInput.focus();
                }
            }
        });

        $('.otp-input').on('paste', function (e) {
            e.preventDefault();
            const paste = (e.originalEvent.clipboardData || window.clipboardData).getData('text');
            const digits = paste.replace(/\D/g, '').slice(0, 6);
            $('.otp-input').each(function (index) {
                if (digits[index]) {
                    $(this).val(digits[index]);
                }
            });
            if (digits.length === 6) {
                $('#otpForm').find('button[type="submit"]').focus();
            }
        });
    }

    setupPasswordValidation() {
        $('#newPassword').on('input', () => this.validatePasswordStrength());
        $('#confirmPassword').on('input', () => this.validatePasswordMatch());
    }

    async handleEmailSubmit(e) {
        e.preventDefault();

        const email = $('#email').val().trim();
        if (!this.validateEmail(email)) {
            this.showAlert('warning', 'Invalid Email', 'Please enter a valid email address.');
            return;
        }

        try {
            this.setLoading(true, 'emailForm');

            // Check if email exists
            const checkResponse = await $.ajax({
                url: 'http://localhost:8080/api/users/checkEmail',
                method: 'POST',
                data: JSON.stringify({ email: email }),
                contentType: 'application/json'
            });

            if (!checkResponse.exists) {
                this.showAlert('error', 'Email Not Found', 'This email is not registered in our system.');
                return;
            }

            // Send OTP
            const response = await $.ajax({
                url: 'http://localhost:8080/api/users/sendOTP',
                method: 'POST',
                data: JSON.stringify({ email: email }),
                contentType: 'application/json'
            });

            this.userEmail = email;
            $('#emailDisplay').text(email);
            this.goToStep(2);
            this.startOTPTimer();

            this.showAlert('success', 'Code Sent!', response || 'A verification code has been sent to your email.');

        } catch (error) {
            console.error('Error during email check or OTP send:', error);
            const errorMessage = error.responseJSON?.message || 'Failed to process request. Please try again.';
            this.showAlert('error', 'Error', errorMessage);
        } finally {
            this.setLoading(false, 'emailForm');
        }
    }

    async handleOTPSubmit(e) {
        e.preventDefault();

        const otp = this.getOTPValue();
        if (otp.length !== 6) {
            this.showAlert('warning', 'Incomplete Code', 'Please enter the complete 6-digit code.');
            return;
        }

        try {
            this.setLoading(true, 'otpForm');

            // Verify OTP
            const response = await $.ajax({
                url: 'http://localhost:8080/api/users/verifyOTP',
                method: 'POST',
                data: JSON.stringify({
                    email: this.userEmail,
                    otp: otp
                }),
                contentType: 'application/json'
            });

            this.clearOTPTimer();
            this.goToStep(3);

            this.showAlert('success', 'Verified!', response || 'Code verified successfully.');

        } catch (error) {
            console.error('Error verifying OTP:', error);
            const errorMessage = error.responseJSON?.message || 'The verification code is incorrect or expired.';
            this.showAlert('error', 'Invalid Code', errorMessage);
            this.clearOTPInputs();
        } finally {
            this.setLoading(false, 'otpForm');
        }
    }

    async handlePasswordSubmit(e) {
        e.preventDefault();

        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();

        if (!this.validatePasswordStrength(newPassword)) {
            this.showAlert('warning', 'Weak Password', 'Please choose a stronger password.');
            return;
        }

        if (newPassword !== confirmPassword) {
            $('#passwordError').show();
            return;
        }

        try {
            this.setLoading(true, 'passwordForm');

            // Reset password
            const response = await $.ajax({
                url: 'http://localhost:8080/api/users/resetPassword',
                method: 'POST',
                data: JSON.stringify({
                    email: this.userEmail,
                    newPassword: newPassword
                }),
                contentType: 'application/json'
            });

            this.showAlert('success', 'Password Reset!', response || 'Your password has been successfully reset.', () => {
                window.location.href = 'signin.html';
            });

        } catch (error) {
            console.error('Error resetting password:', error);
            const errorMessage = error.responseJSON?.message || 'Failed to reset password. Please try again.';
            this.showAlert('error', 'Error', errorMessage);
        } finally {
            this.setLoading(false, 'passwordForm');
        }
    }

    async resendOTP() {
        try {
            const response = await $.ajax({
                url: 'http://localhost:8080/api/users/sendOTP',
                method: 'POST',
                data: JSON.stringify({ email: this.userEmail }),
                contentType: 'application/json'
            });

            this.clearOTPInputs();
            this.startOTPTimer();
            this.startResendTimer();

            this.showAlert('success', 'Code Resent!', response || 'A new verification code has been sent.');

        } catch (error) {
            console.error('Error resending OTP:', error);
            const errorMessage = error.responseJSON?.message || 'Failed to resend code. Please try again.';
            this.showAlert('error', 'Error', errorMessage);
        }
    }

    goToStep(step) {
        $('.step-content').hide();
        for (let i = 1; i <= 3; i++) {
            $(`#step${i}`).removeClass('active completed inactive');
            if (i < step) {
                $(`#step${i}`).addClass('completed');
            } else if (i === step) {
                $(`#step${i}`).addClass('active');
            } else {
                $(`#step${i}`).addClass('inactive');
            }
        }
        $(`#line1`).toggleClass('active', step > 1);
        $(`#line2`).toggleClass('active', step > 2);
        if (step === 1) {
            $('#emailStep').show().addClass('fade-in');
        } else if (step === 2) {
            $('#otpStep').show().addClass('fade-in');
        } else if (step === 3) {
            $('#passwordStep').show().addClass('fade-in');
        }
        this.currentStep = step;
    }

    startOTPTimer() {
        let timeLeft = 300; // 5 minutes
        this.otpTimer = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;
            $('#countdown').text(`${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`);
            if (timeLeft <= 60) {
                $('#timer').addClass('warning');
            }
            if (timeLeft <= 0) {
                this.clearOTPTimer();
                this.showAlert('warning', 'Code Expired', 'The verification code has expired. Please request a new one.');
            }
            timeLeft--;
        }, 1000);
        this.startResendTimer();
    }

    startResendTimer() {
        let timeLeft = 60;
        $('#resendBtn').prop('disabled', true);
        this.resendTimer = setInterval(() => {
            $('#resendTimer').text(timeLeft);
            if (timeLeft <= 0) {
                clearInterval(this.resendTimer);
                this.resendTimer = null;
                $('#resendBtn').prop('disabled', false);
                $('#resendBtn').html('<i class="fas fa-redo me-2"></i>Resend Code');
            }
            timeLeft--;
        }, 1000);
    }

    clearOTPTimer() {
        if (this.otpTimer) {
            clearInterval(this.otpTimer);
            this.otpTimer = null;
        }
        if (this.resendTimer) {
            clearInterval(this.resendTimer);
            this.resendTimer = null;
        }
    }

    getOTPValue() {
        return $('.otp-input').map(function () {
            return $(this).val();
        }).get().join('');
    }

    clearOTPInputs() {
        $('.otp-input').val('');
        $('.otp-input').first().focus();
    }

    validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    validatePasswordStrength(password = $('#newPassword').val()) {
        let strength = 0;
        let strengthText = '';
        let strengthClass = '';

        if (password.length >= 8) strength++;
        if (/[a-z]/.test(password)) strength++;
        if (/[A-Z]/.test(password)) strength++;
        if (/[0-9]/.test(password)) strength++;
        if (/[^A-Za-z0-9]/.test(password)) strength++;

        if (strength <= 2) {
            strengthText = 'Weak';
            strengthClass = 'strength-weak';
        } else if (strength <= 3) {
            strengthText = 'Medium';
            strengthClass = 'strength-medium';
        } else {
            strengthText = 'Strong';
            strengthClass = 'strength-strong';
        }

        const percentage = (strength / 5) * 100;
        $('#strengthFill').css('width', percentage + '%').removeClass('strength-fill strength-weak strength-medium strength-strong').addClass('strength-fill ' + strengthClass);
        $('#strengthText').text(strengthText);

        return strength >= 3;
    }

    validatePasswordMatch() {
        const password = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();
        if (confirmPassword && password !== confirmPassword) {
            $('#passwordError').show();
            return false;
        } else {
            $('#passwordError').hide();
            return true;
        }
    }

    togglePasswordVisibility(inputId, buttonId) {
        const input = $(`#${inputId}`);
        const button = $(`#${buttonId} i`);
        if (input.attr('type') === 'password') {
            input.attr('type', 'text');
            button.removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            input.attr('type', 'password');
            button.removeClass('fa-eye-slash').addClass('fa-eye');
        }
    }

    setLoading(isLoading, formId) {
        const button = $(`#${formId} button[type="submit"]`);
        if (isLoading) {
            button.prop('disabled', true).addClass('loading').text('Loading...');
        } else {
            button.prop('disabled', false).removeClass('loading').text(button.data('original-text') || button.text());
        }
        if (!button.data('original-text')) {
            button.data('original-text', button.text());
        }
    }

    showAlert(icon, title, text, callback = null) {
        Swal.fire({
            icon: icon,
            title: title,
            text: text,
            confirmButtonColor: '#059669'
        }).then((result) => {
            if (callback && result.isConfirmed) {
                callback();
            }
        });
    }
}

$(document).ready(function () {
    new ForgotPasswordManager();
});