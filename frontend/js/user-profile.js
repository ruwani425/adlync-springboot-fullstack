let currentUserId = null;

$(document).ready(function () {
    initializeProfile();
    getUserByToken();
    initPasswordChange();

    $('#postSelect').change(function () {
        const postId = $(this).val();
        console.log('Selected post ID:', postId);
        if (postId === 'all') {
            loadUserReviews(currentUserId);
        } else {
            loadUserReviews(currentUserId, 0, 5, postId);
        }
    });

    $('#statusFilter').change(function () {
        const selectedStatus = $(this).val();
        console.log('Filter changed to:', selectedStatus);

        const token = getCookie("token");
        if (token) {
            $.ajax({
                url: "http://localhost:8080/api/users/getUserByToken",
                method: "GET",
                headers: {"Authorization": "Bearer " + token},
                success: function (response) {
                    loadUserAdsByUserId(response.id, selectedStatus);
                }
            });
        }
    });

    $('#changePhotoBtn').click(function () {
        $('#photoUploadModal').modal('show');
    });

    $('#photoInput').change(function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $('#photoPreview').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
    });

    $('#uploadPhotoBtn').click(function () {
        const newSrc = $('#photoPreview').attr('src');
        $('#profileImg, #navProfileImg').attr('src', newSrc);
        $('#photoUploadModal').modal('hide');

        Swal.fire({
            icon: 'success',
            title: 'Success!',
            text: 'Profile photo updated successfully',
            timer: 2000,
            showConfirmButton: false
        });
    });

    $('#saveProfileBtn').click(function () {
        const formData = {
            name: $('#editName').val(),
            email: $('#editEmail').val(),
            phone: $('#editPhone').val(),
            location: $('#editLocation').val(),
            bio: $('#editBio').val()
        };

        $('#userName').text(formData.name);
        $('#userEmail').text(formData.email);
        $('#userLocation').text(formData.location);

        $('#editProfileModal').modal('hide');

        Swal.fire({
            icon: 'success',
            title: 'Profile Updated!',
            text: 'Your profile has been updated successfully',
            timer: 2000,
            showConfirmButton: false
        });
    });

    $('#addNewAdBtn').click(function () {
        window.location.href = 'postad.html';
    });

    $('#postAdBtn').off('click.auth').on('click.auth', function () {
        const token = getCookie("token");
        if (token) {
            console.log("Authenticated user: Navigating to postad.html");
            window.location.href = 'postad.html';
        } else {
            console.log("Unauthenticated user: Navigating to signup.html");
            window.location.href = 'signup.html';
        }
    });

    $('#logoutLink').click(function (e) {
        e.preventDefault();
        Swal.fire({
            title: 'Logout?',
            text: 'Are you sure you want to logout?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#059669',
            cancelButtonColor: '#6b7280',
            confirmButtonText: 'Yes, logout'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteCookie("token");
                window.location.href = '../index.html';
            }
        });
    });
});

$('#photoInput').change(function (e) {
    const file = e.target.files[0];
    if (file) {
        const maxSize = 5 * 1024 * 1024;
        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

        if (file.size > maxSize) {
            Swal.fire({
                icon: 'error',
                title: 'File Too Large',
                text: 'Please select an image smaller than 5MB'
            });
            $(this).val('');
            return;
        }

        if (!allowedTypes.includes(file.type)) {
            Swal.fire({
                icon: 'error',
                title: 'Invalid File Type',
                text: 'Please select a JPG, PNG, or WebP image'
            });
            $(this).val('');
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            $('#photoPreview').attr('src', e.target.result);
            $('#uploadPhotoBtn').prop('disabled', false);
        };
        reader.readAsDataURL(file);
    } else {
        $('#uploadPhotoBtn').prop('disabled', true);
    }
});

$('#uploadPhotoBtn').click(async function () {
    const fileInput = $('#photoInput')[0];
    const file = fileInput.files[0];

    if (!file) {
        Swal.fire('Error', 'Please select a photo first', 'error');
        return;
    }

    $('#uploadProgress').show();
    $(this).prop('disabled', true).html('<i class="bi bi-hourglass-split me-2"></i>Uploading...');

    try {
        const photoUrl = await uploadProfilePhotoToFirebase(file);

        await updateProfilePhoto(photoUrl);

        $('#profileImg, #navProfileImg').attr('src', photoUrl);
        $('#photoUploadModal').modal('hide');

        Swal.fire({
            icon: 'success',
            title: 'Success!',
            text: 'Profile photo updated successfully',
            timer: 2000,
            showConfirmButton: false
        });

    } catch (error) {
        console.error('Upload failed:', error);
        Swal.fire({
            icon: 'error',
            title: 'Upload Failed',
            text: error.message || 'Failed to update profile photo'
        });
    } finally {
        $('#uploadProgress').hide();
        $('.progress-bar').css('width', '0%');
        $(this).prop('disabled', false).html('<i class="bi bi-cloud-upload me-2"></i>Upload & Save');
    }
});

function initPasswordChange() {
    $('#toggleCurrentPassword').click(function () {
        togglePasswordVisibility('#currentPassword', '#toggleCurrentPassword');
    });

    $('#toggleNewPassword').click(function () {
        togglePasswordVisibility('#newPassword', '#toggleNewPassword');
    });

    $('#toggleConfirmPassword').click(function () {
        togglePasswordVisibility('#confirmPassword', '#toggleConfirmPassword');
    });

    $('#newPassword').on('input', function () {
        const password = $(this).val();
        checkPasswordStrength(password);
        validatePasswordRequirements();
        checkPasswordMatch();
    });

    $('#confirmPassword').on('input', function () {
        checkPasswordMatch();
        validatePasswordRequirements();
    });

    $('#currentPassword').on('input', function () {
        validatePasswordRequirements();
    });

    $('#savePasswordBtn').click(function () {
        if (validatePasswordForm()) {
            changePassword();
        }
    });

    $(document).on('click', '.btn-outline-danger:contains("Change Password")', function (e) {
        e.preventDefault();
        $('#changePasswordModal').modal('show');
        resetPasswordForm();
    });
}

function togglePasswordVisibility(inputSelector, buttonSelector) {
    const input = $(inputSelector);
    const button = $(buttonSelector);
    const icon = button.find('i');

    if (input.attr('type') === 'password') {
        input.attr('type', 'text');
        icon.removeClass('bi-eye').addClass('bi-eye-slash');
    } else {
        input.attr('type', 'password');
        icon.removeClass('bi-eye-slash').addClass('bi-eye');
    }
}

function checkPasswordStrength(password) {
    const strengthContainer = $('#passwordStrength');
    const progressBar = strengthContainer.find('.progress-bar');
    const strengthText = $('#strengthText');

    if (password.length === 0) {
        strengthContainer.hide();
        return;
    }

    strengthContainer.show();

    let strength = 0;
    let strengthLabel = '';

    if (password.length >= 8) strength += 20;
    if (password.length >= 12) strength += 10;

    if (/[a-z]/.test(password)) strength += 20;
    if (/[A-Z]/.test(password)) strength += 20;
    if (/[0-9]/.test(password)) strength += 15;
    if (/[^A-Za-z0-9]/.test(password)) strength += 15;

    if (strength < 40) {
        strengthLabel = 'Weak';
        progressBar.removeClass().addClass('progress-bar strength-weak');
    } else if (strength < 70) {
        strengthLabel = 'Medium';
        progressBar.removeClass().addClass('progress-bar strength-medium');
    } else {
        strengthLabel = 'Strong';
        progressBar.removeClass().addClass('progress-bar strength-strong');
    }

    progressBar.css('width', strength + '%');
    strengthText.text(`Password strength: ${strengthLabel}`);
}

function validatePasswordRequirements() {
    const currentPassword = $('#currentPassword').val();
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();

    let allValid = true;

    if (currentPassword.length === 0) {
        allValid = false;
    }

    const lengthValid = newPassword.length >= 8;
    updateRequirement('#req-length', lengthValid);
    if (!lengthValid) allValid = false;

    const uppercaseValid = /[A-Z]/.test(newPassword);
    updateRequirement('#req-uppercase', uppercaseValid);
    if (!uppercaseValid) allValid = false;

    const lowercaseValid = /[a-z]/.test(newPassword);
    updateRequirement('#req-lowercase', lowercaseValid);
    if (!lowercaseValid) allValid = false;

    const numberValid = /[0-9]/.test(newPassword);
    updateRequirement('#req-number', numberValid);
    if (!numberValid) allValid = false;

    const specialValid = /[^A-Za-z0-9]/.test(newPassword);
    updateRequirement('#req-special', specialValid);
    if (!specialValid) allValid = false;

    const matchValid = newPassword.length > 0 && newPassword === confirmPassword;
    updateRequirement('#req-match', matchValid);
    if (!matchValid) allValid = false;

    $('#savePasswordBtn').prop('disabled', !allValid);

    return allValid;
}

function updateRequirement(selector, isValid) {
    const element = $(selector);
    const icon = element.find('i');

    if (isValid) {
        element.addClass('valid');
        icon.removeClass('bi-x-circle text-danger').addClass('bi-check-circle text-success');
    } else {
        element.removeClass('valid');
        icon.removeClass('bi-check-circle text-success').addClass('bi-x-circle text-danger');
    }
}

function checkPasswordMatch() {
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();
    const confirmInput = $('#confirmPassword');
    const errorDiv = $('#confirmPasswordError');

    if (confirmPassword.length > 0) {
        if (newPassword !== confirmPassword) {
            confirmInput.addClass('is-invalid');
            errorDiv.text('Passwords do not match');
        } else {
            confirmInput.removeClass('is-invalid').addClass('is-valid');
            errorDiv.text('');
        }
    } else {
        confirmInput.removeClass('is-invalid is-valid');
        errorDiv.text('');
    }
}

function validatePasswordForm() {
    const currentPassword = $('#currentPassword').val().trim();
    const newPassword = $('#newPassword').val().trim();
    const confirmPassword = $('#confirmPassword').val().trim();

    $('.form-control').removeClass('is-invalid');
    $('.invalid-feedback').text('');

    let isValid = true;

    if (!currentPassword) {
        $('#currentPassword').addClass('is-invalid');
        $('#currentPasswordError').text('Current password is required');
        isValid = false;
    }

    if (!newPassword) {
        $('#newPassword').addClass('is-invalid');
        $('#newPasswordError').text('New password is required');
        isValid = false;
    }

    if (!confirmPassword) {
        $('#confirmPassword').addClass('is-invalid');
        $('#confirmPasswordError').text('Please confirm your new password');
        isValid = false;
    }

    if (newPassword && currentPassword && newPassword === currentPassword) {
        $('#newPassword').addClass('is-invalid');
        $('#newPasswordError').text('New password must be different from current password');
        isValid = false;
    }

    return isValid && validatePasswordRequirements();
}

function changePassword() {
    const currentPassword = $('#currentPassword').val();
    const newPassword = $('#newPassword').val();
    const token = getCookie("token");

    if (!token) {
        Swal.fire({
            icon: 'error',
            title: 'Authentication Error',
            text: 'Please log in again to change your password'
        });
        return;
    }

    const saveBtn = $('#savePasswordBtn');
    const originalText = saveBtn.html();
    saveBtn.prop('disabled', true).html('<i class="bi bi-hourglass-split me-2"></i>Changing Password...');

    $.ajax({
        url: "http://localhost:8080/api/users/change-password",
        method: "PATCH",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            currentPassword: currentPassword,
            newPassword: newPassword
        }),
        success: function (response) {
            $('#changePasswordModal').modal('hide');

            Swal.fire({
                icon: 'success',
                title: 'Password Changed!',
                text: 'Your password has been updated successfully. You will be logged out for security.',
                showConfirmButton: true,
                confirmButtonColor: '#059669',
                confirmButtonText: 'Continue'
            }).then((result) => {
                if (result.isConfirmed) {
                    deleteCookie("token");
                    window.location.href = '../index.html';
                }
            });
        },
        error: function (xhr, status, error) {
            console.error("Password change error:", error);
            let errorMessage = 'Failed to change password. Please try again.';

            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }

            if (xhr.status === 400) {
                $('#currentPassword').addClass('is-invalid');
                $('#currentPasswordError').text('Current password is incorrect');
            } else if (xhr.status === 401) {
                $('#currentPassword').addClass('is-invalid');
                $('#currentPasswordError').text('Current password is incorrect');
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: errorMessage
                });
            }
        },
        complete: function () {
            saveBtn.prop('disabled', false).html(originalText);
        }
    });
}

function resetPasswordForm() {
    $('#changePasswordForm')[0].reset();
    $('.form-control').removeClass('is-invalid is-valid');
    $('.invalid-feedback').text('');
    $('.requirement').removeClass('valid');
    $('.requirement i').removeClass('bi-check-circle text-success').addClass('bi-x-circle text-danger');
    $('#passwordStrength').hide();
    $('#savePasswordBtn').prop('disabled', true);
}

async function updateProfilePhoto(photoUrl) {
    const token = getCookie("token");
    if (!token) {
        throw new Error("Authentication token not found");
    }

    return new Promise((resolve, reject) => {
        $.ajax({
            url: "http://localhost:8080/api/users/update-profile-photo",
            method: "PATCH",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            data: JSON.stringify({
                profileImageUrl: photoUrl
            }),
            success: function (response) {
                console.log("Profile photo updated:", response);
                resolve(response);
            },
            error: function (xhr, status, error) {
                console.error("API Error:", error);
                reject(new Error(xhr.responseJSON?.message || "Failed to update profile photo"));
            }
        });
    });
}

function getUserByToken() {
    const token = getCookie("token");
    if (!token) return;

    $.ajax({
        url: "http://localhost:8080/api/users/getUserByToken",
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: function (response) {
            console.log("User data:", response);
            currentUserId = response.id;
            $('#userName').text(response.name);
            $('#userEmail').text(response.email);
            $('#joinDate').text(formatJoinDate(response.joinDate));

            let profileUrl = response.profileImageUrl;

            if (!profileUrl || profileUrl.trim() === '') {
                profileUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(response.name)}&background=059669&color=fff&size=120&rounded=true`;
                console.log("Using fallback avatar:", profileUrl);
            } else {
                profileUrl = profileUrl.trim();
                console.log("Using database profile photo:", profileUrl);
            }

            $('#profileImg').attr('src', profileUrl);

            if (profileUrl.includes("ui-avatars")) {
                $('#navProfileImg').attr('src', profileUrl.replace('size=120', 'size=40'));
            } else {
                $('#navProfileImg').attr('src', profileUrl);
            }

            $('#photoPreview').attr('src', profileUrl);
            loadUserAdsByUserId(currentUserId);
            loadUserPostsForDropdown(currentUserId);
            loadUserReviews(currentUserId);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Could not load user profile!'
            });
        }
    });
}

function loadUserAdsByUserId(userId, status = 'all', page = 0, size = 3) {
    const container = $('#userAdsContainer');
    container.html(`
        <div class="col-12">
            <div class="empty-state">
                <i class="bi bi-arrow-clockwise"></i>
                <h4>Loading your ads...</h4>
                <p>Please wait while we fetch your advertisements.</p>
            </div>
        </div>
    `);

    const token = getCookie("token");
    if (!token) return;

    $.ajax({
        url: `http://localhost:8080/api/posts/by-user/${userId}?page=${page}&size=${size}`,
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: function (response) {
            let posts = response.content || [];

            // ✅ Filter by status if needed
            if (status !== 'all') {
                posts = posts.filter(p => p.status.toLowerCase() === status.toLowerCase());
            }

            renderUserAds(posts);
            updateStats(posts);
            renderPagination(response, status, userId, size);
        },
        error: function (xhr, status, error) {
            console.error("Error loading ads:", error);
            container.html(`
                <div class="col-12">
                    <div class="empty-state">
                        <i class="bi bi-x-circle"></i>
                        <h4>Error loading ads</h4>
                        <p>Please try again later.</p>
                    </div>
                </div>
            `);
        }
    });
}


function renderPagination(response, status, userId, size) {
    const {totalPages, number: currentPage} = response;
    const paginationContainer = $('#paginationContainer');
    paginationContainer.empty();

    if (totalPages <= 1) return;

    let html = `<nav><ul class="pagination">`;

    html += `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
        </li>
    `;

    for (let i = 0; i < totalPages; i++) {
        html += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
            </li>
        `;
    }

    html += `
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
        </li>
    `;

    html += `</ul></nav>`;
    paginationContainer.html(html);

    paginationContainer.find('a.page-link').on('click', function (e) {
        e.preventDefault();
        const targetPage = $(this).data('page');
        if (targetPage >= 0 && targetPage < totalPages) {
            loadUserAdsByUserId(userId, status, targetPage, size);
        }
    });
}


function renderUserAds(posts) {
    const container = $('#userAdsContainer');
    if (!posts || posts.length === 0) {
        container.html(`
            <div class="col-12">
                <div class="empty-state">
                    <i class="bi bi-box-seam"></i>
                    <h4>No ads found</h4>
                    <p>You haven't posted any advertisements yet.</p>
                    <button class="btn btn-emerald mt-3" onclick="window.location.href='postad.html'">
                        <i class="bi bi-plus-circle me-2"></i>Create Your First Ad
                    </button>
                </div>
            </div>
        `);
        return;
    }

    const html = posts.map(ad => `
        <div class="col-md-6 col-lg-4">
            <div class="card ad-card h-100">
                <div class="ad-image-container position-relative">
                    <img src="${ad.images && ad.images[0]?.image_url || 'https://via.placeholder.com/400x300?text=No+Image'}" 
                         alt="${ad.title}" class="w-100 object-cover" style="height: 200px;">
                    <span class="status-badge ${getStatusClass(ad.status)} position-absolute top-0 end-0 m-3">
                        ${ad.status.charAt(0).toUpperCase() + ad.status.slice(1)}
                    </span>
                </div>
                <div class="card-body">
                    <span class="category-badge mb-2">${ad.category?.name || "Uncategorized"}</span>
                    <h6 class="card-title text-truncate" title="${ad.title}">${ad.title}</h6>
                    <div class="price-tag mb-3">Rs. ${formatPrice(ad.price)}</div>
                    <div class="d-flex justify-content-between text-muted small mb-3">
                        <span><i class="bi bi-geo-alt me-1"></i>${ad.location?.address || 'Location not specified'}</span>
                        <span><i class="bi bi-eye me-1"></i>${ad.views || 0} views</span>
                    </div>
                    <div class="d-flex gap-2 action-buttons">
                        <button class="btn btn-outline-secondary btn-sm flex-fill" onclick="editAd(${ad.id})">
                            <i class="bi bi-pencil me-1"></i>Edit
                        </button>
                        <button class="btn btn-outline-danger btn-sm flex-fill" onclick="deleteAd(${ad.id})">
                            <i class="bi bi-trash me-1"></i>Delete
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');

    container.html(html);
}

function updateStats(posts) {
    const totalAds = posts.length;
    const activeAds = posts.filter(p => p.status.toLowerCase() === 'active').length;
    const soldAds = posts.filter(p => p.status.toLowerCase() === 'sold').length;
    const pendingAds = posts.filter(p => p.status.toLowerCase() === 'pending').length;
    const totalViews = posts.reduce((sum, p) => sum + (p.views || 0), 0);

    $('#totalAds').text(totalAds);
    $('#activeAds').text(activeAds);
    $('#soldAds').text(soldAds);
    $('#totalViews').text(formatViews(totalViews));
}

function getStatusClass(status) {
    switch (status.toUpperCase()) {
        case "ACTIVE":
        case "APPROVED":
            return "status-active";
        case "PENDING":
            return "status-pending";
        case "SOLD":
            return "status-sold";
        case "REJECTED":
            return "status-rejected";
        case "DRAFT":
            return "status-draft";
        default:
            return "status-pending";
    }
}

function formatPrice(price) {
    if (!price) return "0";
    return new Intl.NumberFormat('en-LK').format(price);
}

function formatViews(views) {
    if (views >= 1000000) {
        return (views / 1000000).toFixed(1) + 'M';
    } else if (views >= 1000) {
        return (views / 1000).toFixed(1) + 'k';
    }
    return views.toString();
}

function editAd(adId) {
    Swal.fire({
        title: 'Edit Advertisement',
        text: 'This will redirect you to the edit page',
        icon: 'info',
        confirmButtonColor: '#059669',
        showCancelButton: true,
        confirmButtonText: 'Go to Edit',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            // Redirect to edit page with ad ID
            window.location.href = `edit-ad.html?id=${adId}`;
        }
    });
}

function deleteAd(adId) {
    Swal.fire({
        title: 'Delete Advertisement?',
        text: 'This action cannot be undone!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc2626',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            const token = getCookie("token");
            if (!token) {
                Swal.fire('Error', 'Please log in again', 'error');
                return;
            }

            Swal.fire({
                title: 'Deleting...',
                text: 'Please wait while we delete your advertisement',
                allowOutsideClick: false,
                showConfirmButton: false,
                willOpen: () => {
                    Swal.showLoading();
                }
            });

            $.ajax({
                url: `http://localhost:8080/api/posts/${adId}`,
                method: "DELETE",
                headers: {"Authorization": "Bearer " + token},
                success: function () {
                    Swal.fire({
                        title: 'Deleted!',
                        text: 'Your advertisement has been deleted successfully.',
                        icon: 'success',
                        timer: 2000,
                        showConfirmButton: false
                    }).then(() => {
                        getUserByToken();
                    });
                },
                error: function (xhr, status, error) {
                    console.error("Delete error:", error);
                    Swal.fire({
                        title: 'Error!',
                        text: 'Failed to delete advertisement. Please try again.',
                        icon: 'error',
                        confirmButtonColor: '#059669'
                    });
                }
            });
        }
    });
}

function formatJoinDate(isoDate) {
    const date = new Date(isoDate);
    const options = {year: 'numeric', month: 'long'};
    return `Joined ${date.toLocaleDateString('en-US', options)}`;
}

function initializeProfile() {
    console.log('Profile initialized');
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function deleteCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
}

function changePage(page, status = 'all') {
    if (!currentUserId) return;
    loadUserAdsByUserId(currentUserId, status, page, 6);
}

function loadUserPostsForDropdown(userId) {
    const token = getCookie("token");
    if (!token) {
        console.error("No token found for dropdown fetch");
        $('#postSelect').html('<option value="">Please log in to view posts</option>');
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/posts/by-user/${userId}`,
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: function (response) {
            console.log("Posts for dropdown:", response);
            const posts = response.content || [];
            const dropdown = $('#postSelect');
            dropdown.empty();
            dropdown.append('<option value="all">All Posts</option>');
            if (posts.length === 0) {
                dropdown.append('<option value="">No posts available</option>');
            } else {
                posts.forEach(post => {
                    dropdown.append(`<option value="${post.post_id || post.id}">${post.title} (ID: ${post.post_id || post.id})</option>`);
                });
            }
        },
        error: function (xhr) {
            console.error("Error fetching posts for dropdown:", xhr);
            $('#postSelect').html('<option value="">No posts available</option>');
        }
    });
}

function loadUserReviews(userId, page = 0, size = 5, postId = null) {
    const token = getCookie("token");
    if (!token) {
        $('#reviewsContainer').html('<p class="text-danger">Please log in to view reviews.</p>');
        return;
    }

    const container = $('#reviewsContainer');
    container.html(`
        <div class="col-12">
            <div class="empty-state">
                <i class="bi bi-arrow-clockwise"></i>
                <h4>Loading reviews...</h4>
                <p>Please wait while we fetch the reviews.</p>
            </div>
        </div>
    `);

    if (postId && postId !== 'all') {
        $.ajax({
            url: `http://localhost:8080/api/reviews/post/${postId}?limit=5`,
            method: "GET",
            headers: {"Authorization": "Bearer " + token},
            success: function (reviews) {
                console.log(`Fetched reviews for post ID ${postId}:`, reviews);
                renderReviews(reviews, postId);
            },
            error: function (xhr) {
                console.error(`Error fetching reviews for post ID ${postId}:`, xhr);
                container.html('<p class="text-danger">Failed to load reviews for this post. Please try again later.</p>');
            }
        });
    } else {
        $.ajax({
            url: `http://localhost:8080/api/posts/by-user/${userId}?page=${page}&size=${size}`,
            method: "GET",
            headers: {"Authorization": "Bearer " + token},
            success: function (response) {
                console.log("Fetched posts:", response);
                const posts = (response.content || []).filter(post => ['APPROVED', 'SOLD'].includes(post.status.toUpperCase()));
                if (posts.length === 0) {
                    container.html('<p>No reviews yet. Post an ad to receive reviews.</p>');
                    return;
                }

                let allReviews = [];
                let reviewPromises = posts.map(post => {
                    return $.ajax({
                        url: `http://localhost:8080/api/reviews/post/${post.post_id || post.id}?limit=5`,
                        method: "GET",
                        headers: {"Authorization": "Bearer " + token}
                    }).then(reviews => {
                        reviews.forEach(review => {
                            review.postTitle = post.title;
                            review.postId = post.post_id || post.id;
                        });
                        allReviews = allReviews.concat(reviews);
                    }).catch(xhr => {
                        console.error(`Error fetching reviews for post ${post.post_id || post.id}:`, xhr);
                    });
                });

                Promise.all(reviewPromises).then(() => {
                    console.log("All fetched reviews:", allReviews);
                    allReviews.sort((a, b) => new Date(b.created_at) - new Date(a.created_at));
                    renderReviews(allReviews);
                }).catch(error => {
                    console.error("Error fetching reviews:", error);
                    container.html('<p class="text-danger">Failed to load reviews. Please try again later.</p>');
                });
            },
            error: function (xhr) {
                console.error("Error fetching posts:", xhr);
                container.html('<p class="text-danger">Failed to load reviews. Please try again later.</p>');
            }
        });
    }
}

function renderReviews(reviews, specificPostId = null) {
    const container = $('#reviewsContainer');
    container.empty();

    if (!reviews || reviews.length === 0) {
        container.html(`
            <div class="col-12">
                <div class="empty-state">
                    <i class="bi bi-star"></i>
                    <h4>No reviews yet</h4>
                    <p>You haven't received any reviews${specificPostId ? ` for this post` : ''} yet.</p>
                </div>
            </div>
        `);
        return;
    }

    const reviewsHtml = reviews.map(review => {
        if (!review.reviewerName || !review.rating || !review.title || !review.content || !review.created_at) {
            console.warn("Invalid review data:", review);
            return '';
        }

        const reviewDate = new Date(review.created_at).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        const fullStars = Math.floor(review.rating);
        const emptyStars = 5 - fullStars;
        const starsHtml =
            '<i class="bi bi-star-fill text-warning"></i>'.repeat(fullStars) +
            '<i class="bi bi-star text-warning"></i>'.repeat(emptyStars);

        const verifiedBadge = review.verified ?
            '<span class="badge bg-success ms-2"><i class="bi bi-patch-check-fill me-1"></i>Verified</span>' : '';

        const recommendationBadge = review.recommendation === 'YES' ?
            '<span class="badge bg-emerald ms-2"><i class="bi bi-thumbs-up-fill me-1"></i>Recommends</span>' :
            review.recommendation === 'NO' ?
                '<span class="badge bg-danger ms-2"><i class="bi bi-thumbs-down-fill me-1"></i>Not Recommended</span>' : '';

        return `
            <div class="col-md-6 mb-4">
                <div class="card h-100 review-card">
                    <div class="card-body">
                        <!-- Reviewer Info -->
                        <div class="d-flex align-items-center mb-3">
                            <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(review.reviewerName)}&background=059669&color=fff&size=50&rounded=true" 
                                 class="rounded-circle me-3" 
                                 style="width: 50px; height: 50px;"
                                 alt="${review.reviewerName}">
                            <div class="flex-grow-1">
                                <h6 class="mb-0">${review.reviewerName}</h6>
                                <small class="text-muted">${reviewDate}</small>
                                ${verifiedBadge}
                                ${recommendationBadge}
                            </div>
                        </div>

                        <!-- Rating Stars -->
                        <div class="d-flex align-items-center mb-2">
                            <div class="me-2">${starsHtml}</div>
                            <span class="text-muted small">(${review.rating}/5)</span>
                        </div>

                        <!-- Review Title -->
                        <h5 class="card-title mb-2">${review.title}</h5>

                        <!-- Post Reference -->
                        <p class="text-muted small mb-3">
                            <i class="bi bi-box me-1"></i>
                            For: <strong>${review.postTitle || `Post ID ${review.postId}`}</strong>
                        </p>

                        <!-- Review Content -->
                        <p class="card-text">${review.content}</p>

                        <!-- Detailed Ratings -->
                        ${review.qualityRating || review.communicationRating || review.deliveryRating || review.valueRating ? `
                            <div class="mt-3 pt-3 border-top">
                                <h6 class="text-muted small mb-2">Detailed Ratings:</h6>
                                <div class="row g-2 text-center">
                                    ${review.qualityRating ? `
                                        <div class="col-6 col-md-3">
                                            <div class="small text-muted">Quality</div>
                                            <div class="fw-bold text-emerald">${review.qualityRating}/5</div>
                                        </div>
                                    ` : ''}
                                    ${review.communicationRating ? `
                                        <div class="col-6 col-md-3">
                                            <div class="small text-muted">Communication</div>
                                            <div class="fw-bold text-emerald">${review.communicationRating}/5</div>
                                        </div>
                                    ` : ''}
                                    ${review.deliveryRating ? `
                                        <div class="col-6 col-md-3">
                                            <div class="small text-muted">Delivery</div>
                                            <div class="fw-bold text-emerald">${review.deliveryRating}/5</div>
                                        </div>
                                    ` : ''}
                                    ${review.valueRating ? `
                                        <div class="col-6 col-md-3">
                                            <div class="small text-muted">Value</div>
                                            <div class="fw-bold text-emerald">${review.valueRating}/5</div>
                                        </div>
                                    ` : ''}
                                </div>
                            </div>
                        ` : ''}
                    </div>
                </div>
            </div>
        `;
    }).filter(html => html !== '').join('');

    if (reviewsHtml) {
        container.html(`<div class="row">${reviewsHtml}</div>`);

        const avgRating = (reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length).toFixed(1);
        const recommendedCount = reviews.filter(r => r.recommendation === 'YES').length;
        const recommendationPercentage = ((recommendedCount / reviews.length) * 100).toFixed(0);

        container.prepend(`
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card bg-light">
                        <div class="card-body">
                            <div class="row text-center">
                                <div class="col-md-3">
                                    <h3 class="text-emerald mb-0">${avgRating}</h3>
                                    <small class="text-muted">Average Rating</small>
                                </div>
                                <div class="col-md-3">
                                    <h3 class="text-emerald mb-0">${reviews.length}</h3>
                                    <small class="text-muted">Total Reviews</small>
                                </div>
                                <div class="col-md-3">
                                    <h3 class="text-emerald mb-0">${recommendationPercentage}%</h3>
                                    <small class="text-muted">Recommend</small>
                                </div>
                                <div class="col-md-3">
                                    <div class="mb-1">${'<i class="bi bi-star-fill text-warning"></i>'.repeat(Math.floor(avgRating))}</div>
                                    <small class="text-muted">Overall</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `);
    } else {
        container.html(`
            <div class="col-12">
                <div class="empty-state">
                    <i class="bi bi-exclamation-triangle"></i>
                    <h4>No valid reviews found</h4>
                    <p>The review data appears to be incomplete${specificPostId ? ` for this post` : ''}.</p>
                </div>
            </div>
        `);
    }
}

function initializeReviewsTab() {
    const reviewsTabContent = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="mb-0">Customer Reviews</h3>
            <div class="d-flex gap-2">
                <select class="form-select form-select-sm" id="postSelect" style="width: auto;">
                    <option value="all">All Posts</option>
                    <!-- Options will be populated dynamically -->
                </select>
            </div>
        </div>
        <div id="reviewsContainer">
            <!-- Reviews will be loaded here -->
        </div>
    `;

    $('#reviews .container, #reviews').html(reviewsTabContent);
}