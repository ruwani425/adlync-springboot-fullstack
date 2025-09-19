const $ = window.jQuery;

let stompClient = null;

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}

function deleteCookie(name) {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showReviewErrorMessage(message = 'Failed to submit review. Please try again later.') {
    const alertHtml = `
        <div class="alert alert-danger alert-dismissible fade show position-fixed" 
             style="top: 20px; right: 20px; z-index: 9999; min-width: 300px;" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i>
            <strong>Error!</strong> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    $('body').append(alertHtml);
    setTimeout(() => {
        $('.alert-danger').fadeOut();
    }, 5000);
}

function showReviewSuccessMessage() {
    const alertHtml = `
        <div class="alert alert-success alert-dismissible fade show position-fixed" 
             style="top: 20px; right: 20px; z-index: 9999; min-width: 300px;" role="alert">
            <i class="bi bi-check-circle me-2"></i>
            <strong>Review Submitted!</strong> Thank you for your feedback. 
            Your review helps other users make informed decisions.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    $('body').append(alertHtml);
    setTimeout(() => {
        $('.alert-success').fadeOut();
    }, 5000);
}

function showReportSuccessMessage() {
    const alertHtml = `
        <div class="alert alert-success alert-dismissible fade show position-fixed" 
             style="top: 20px; right: 20px; z-index: 9999; min-width: 300px;" role="alert">
            <i class="bi bi-check-circle me-2"></i>
            <strong>Report Submitted!</strong> Thank you for reporting this ad. 
            Our team will review it within 24-48 hours.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    $('body').append(alertHtml);
    setTimeout(() => {
        $('.alert-success').fadeOut();
    }, 5000);
}

function showReportErrorMessage() {
    const alertHtml = `
        <div class="alert alert-danger alert-dismissible fade show position-fixed" 
             style="top: 20px; right: 20px; z-index: 9999; min-width: 300px;" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i>
            <strong>Error!</strong> Failed to submit report. Please try again later.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    $('body').append(alertHtml);
    setTimeout(() => {
        $('.alert-danger').fadeOut();
    }, 5000);
}

function initializeProfileForAdDetails() {
    const token = getCookie("token");
    if (token) {
        $("#profileDropdown").show();
        $("#signInBtn").hide();
        loadUserProfile(token);
        $("#postAdBtn").off("click.auth").on("click.auth", () => window.location.href = "postad.html");
        setupLogoutHandler();
    } else {
        $("#profileDropdown").hide();
        $("#signInBtn").show();
        $("#postAdBtn").off("click.auth").on("click.auth", () => window.location.href = "signup.html");
        $("#signInBtn").off("click.auth").on("click.auth", () => window.location.href = "signin.html");
    }
}

function loadUserProfile(token) {
    $.ajax({
        url: "http://localhost:8080/api/users/getUserByToken",
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: (userData) => {
            let profileUrl = userData.profileImageUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(userData.name || 'User')}&background=059669&color=fff&size=40&rounded=true`;
            $("#profileDropdown img").attr('src', profileUrl).off('error.profile').on('error.profile', function () {
                $(this).attr('src', `https://ui-avatars.com/api/?name=${encodeURIComponent(userData.name || 'User')}&background=059669&color=fff&size=40&rounded=true`);
            });
            $('.profile-avatar').each(function () {
                $(this).attr('src', profileUrl);
            });
            window.currentUser = userData;
        },
        error: () => {
            $("#profileDropdown img").attr('src', 'https://ui-avatars.com/api/?name=User&background=059669&color=fff&size=40&rounded=true');
        }
    });
}

function setupLogoutHandler() {
    $("#profileDropdown .dropdown-menu").off('click.logout').on('click.logout', 'a[href="#"]', function (e) {
        if ($(this).find('i').hasClass('bi-box-arrow-right')) {
            e.preventDefault();
            logout();
        }
    });
    $("#profileDropdown .dropdown-menu").off('click.profile').on('click.profile', 'a[href="#"]', function (e) {
        if ($(this).find('i').hasClass('bi-person')) {
            e.preventDefault();
            window.location.href = "user-profile.html";
        }
    });
}

function logout() {
    if (confirm("Are you sure you want to logout?")) {
        deleteCookie("token");
        deleteCookie("user");
        window.location.href = "../index.html";
    }
}

function updateCategoryDetailsTitle(iconClass, categoryName) {
    const titleText = getTitleForCategory(categoryName);
    $("#categoryDetailsTitle").html(`<i class="bi ${iconClass} me-2 text-primary-emerald"></i>${titleText}`);
}

function getTitleForCategory(categoryName) {
    const titleMap = {
        agriculture: "Product Details",
        animal: "Animal Details",
        education: "Course Details",
        electronic: "Device Specifications",
        entertainment: "Item Details",
        essential: "Product Information",
        "fashion and beauty": "Item Details",
        "home and garden": "Product Specifications",
        job: "Job Details",
        kids: "Item Details",
        mobile: "Phone Specifications",
        property: "Property Details",
        service: "Service Information",
        sport: "Equipment Details",
        vehicle: "Vehicle Specifications",
        "work over seas": "Job Details",
    };
    return titleMap[categoryName.toLowerCase()] || "Specifications";
}

function renderCategoryFields(data, fields, iconClass, categoryName) {
    const $container = $("#categoryDetailsContent").empty();
    const categoryData = data.common?.[categoryName] || {};

    fields.forEach((field) => {
        const value = categoryData[field] || "-";
        if (!value || field.toLowerCase().endsWith("_id")) return;
        const cardHtml = `
            <div class="col-md-4 mb-3">
                <div class="card h-100 border-success">
                    <div class="card-body">
                        <h6 class="card-title"><i class="bi ${iconClass} me-2"></i>${field.replace(/_/g, " ")}</h6>
                        <p class="card-text">${value}</p>
                    </div>
                </div>
            </div>`;
        $container.append(cardHtml);
    });
}

function addBreadcrumb(name, url = "#") {
    const $breadcrumb = $(`<li class="breadcrumb-item"><a class="text-decoration-none text-primary-emerald" href="${url}">${name}</a></li>`);
    $("#breadcrumbNav").append($breadcrumb);
}

function validateReviewForm() {
    let isValid = true;

    const rating = $('#selectedRating').val();
    if (!rating || rating < 1 || rating > 5) {
        $('.rating-selector .star-rating').addClass('is-invalid');
        isValid = false;
    } else {
        $('.rating-selector .star-rating').removeClass('is-invalid');
    }

    const title = $('#reviewTitle').val().trim();
    if (!title || title.length < 3) {
        $('#reviewTitle').addClass('is-invalid');
        isValid = false;
    } else {
        $('#reviewTitle').removeClass('is-invalid').addClass('is-valid');
    }

    const content = $('#reviewContent').val().trim();
    if (!content || content.length < 10) {
        $('#reviewContent').addClass('is-invalid');
        isValid = false;
    } else {
        $('#reviewContent').removeClass('is-invalid').addClass('is-valid');
    }

    return isValid;
}

function resetReviewForm() {
    $('#reviewForm')[0].reset();

    $('.star-rating').each(function () {
        $(this).attr('data-rating', '0');
        $(this).find('.star-btn').removeClass('filled');
    });

    $('#selectedRating').val('');

    $('#titleCharCount').text('0');
    $('#contentCharCount').text('0');

    $('#ratingText').text('Click stars to rate');

    $('.form-control, .form-select').removeClass('is-valid is-invalid');
    $('.star-rating').removeClass('is-invalid');

    $('input[name="recommendation"]').prop('checked', false);
    $('.btn-check').siblings('label').removeClass('active');
}

function updateStarDisplay($container, rating) {
    $container.find('.star-btn').each(function (index) {
        if (index + 1 <= rating) {
            $(this).addClass('bi-star-fill filled').removeClass('bi-star');
        } else {
            $(this).addClass('bi-star').removeClass('bi-star-fill filled');
        }
    });
}

function updateSidebarReviewStats() {
    const currentCount = parseInt($('#sidebarTotalReviews').text()) || 0;
    $('#sidebarTotalReviews').text(currentCount + 1);
}

function setCookie(name, value) {
    document.cookie = `${name}=${value}; path=/`;
}

$(document).ready(() => {
    initializeProfileForAdDetails();

    const urlParams = new URLSearchParams(window.location.search);
    const categoryName = urlParams.get("categoryName");
    let postId = urlParams.get("postId");

    if (!categoryName || !postId) {
        console.error("Missing categoryName or postId in URL");
        return;
    }

    const categoryMap = {
        agriculture: {
            fields: ["product_type", "quantity", "season", "variety", "production_Date", "certifications", "condition"],
            icon: "bi-seedling"
        },
        animal: {fields: ["species", "breed", "age", "gender", "vaccination_status"], icon: "bi-heart"},
        education: {
            fields: ["course_name", "institute", "duration", "qualification_offered", "subject_area", "study_mode", "education_level", "schedule", "requirements"],
            icon: "bi-book"
        },
        electronic: {fields: ["brand", "type", "model", "warranty", "condition", "accessories"], icon: "bi-tv"},
        entertainment: {
            fields: ["type", "format", "brand", "genre", "release_year", "rating", "creator", "condition"],
            icon: "bi-music-note-beamed"
        },
        essential: {
            fields: ["brand", "quantity", "expiry_date", "product_type", "storage_instructions", "condition"],
            icon: "bi-bag"
        },
        "fashion and beauty": {
            fields: ["item_type", "brand", "size", "gender", "condition", "color", "material", "style_note"],
            icon: "bi-gem"
        },
        "home and garden": {
            fields: ["item_type", "material", "dimensions", "condition", "brand", "color", "weight", "assembly_required", "special_features"],
            icon: "bi-house"
        },
        job: {
            fields: ["position", "company", "salary_min", "salary_max", "industry", "job_type", "requirements", "expiriance_level"],
            icon: "bi-briefcase"
        },
        kids: {
            fields: ["item_type", "age_rang", "brand", "condition", "size", "gender", "safety_information"],
            icon: "bi-emoji-smile"
        },
        mobile: {
            fields: ["storage", "condition", "warranty_status", "ram", "brand", "model", "colour", "included_accessories", "additional_information"],
            icon: "bi-phone"
        },
        property: {fields: ["type", "land_size", "bedroom", "barthroom", "furnished"], icon: "bi-building"},
        service: {
            fields: ["service_type", "provider_name", "availability", "charges", "service_area", "qualifications"],
            icon: "bi-tools"
        },
        sport: {fields: ["equipment_type", "brand", "condition", "size", "additional_information"], icon: "bi-basket"},
        vehicle: {
            fields: ["vehicle_type", "mileage", "year", "brand", "model", "fuel_type", "transmission", "condition"],
            icon: "bi-car-front"
        },
        "work over seas": {
            fields: ["position", "country", "salary", "requirements", "contract_duration", "company_or_agency_name", "visa_status", "accommodation", "additional_benefits"],
            icon: "bi-globe"
        },
    };

    const category = categoryMap[categoryName.toLowerCase()];
    if (!category) {
        console.warn("Category not found");
        return;
    }

    updateCategoryDetailsTitle(category.icon, categoryName);

    let numericPostId = null;

    $.ajax({
        url: `http://localhost:8080/api/posts/post-detail/${postId}`,
        method: "GET",
        success: (data) => {
            const formatDate = (isoString) => {
                if (!isoString) return "Posted today";
                const date = new Date(isoString);
                return date.toLocaleString("en-US", {
                    day: "2-digit",
                    month: "short",
                    year: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                    hour12: true
                });
            };

            let adTitle = data.title || "-";

            const conditionRaw = data.common?.[categoryName]?.condition;
            console.log(data);
            if (conditionRaw) adTitle += " - " + conditionRaw.toLowerCase() + " condition ";

            $("#adTitle").text(adTitle);
            $("#adPrice").text(`Rs. ${data.price?.toLocaleString() || "-"}`);
            $("#postId").text(data.post_id || "-");
            $("#contactNumber").text(data.contact_number || "-");
            $("#postStatus").text(data.status || "-");
            $("#adDescription").html(`<p>${data.description || "-"}</p>`);
            $("#adCategory").html(`<i class="bi bi-tag me-1"></i>${categoryName}`);
            $("#adCreatedAt").html(`<i class="bi bi-calendar me-1"></i>${formatDate(data.createdAt)}`);
            $("#adType").html(`<i class="bi bi-bookmark me-1"></i>${(data.advertisement_type.type || "-").toLowerCase()}`);

            if (data.user) {
                const seller = data.user;
                setCookie("receiverId", seller.name);

                $("#sellerNameValue").text(seller.name);
                $("#sellerEmail").attr("href", `mailto:${seller.email}`).text(seller.email);

                const joinDate = seller.joinDate.split("T")[0];
                $("#sellerMemberSince").text(joinDate);

                $("#sellerRating").text("4.8/5");

                let profileUrl = seller.profileImageUrl;
                if (!profileUrl || profileUrl.trim() === '') {
                    profileUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(seller.name || 'Seller')}&background=059669&color=fff&size=80&rounded=true`;
                }

                $("#sellerProfileImage")
                    .attr("src", profileUrl)
                    .off('error')
                    .on('error', function () {
                        $(this).attr("src", `https://ui-avatars.com/api/?name=${encodeURIComponent(seller.name || 'Seller')}&background=059669&color=fff&size=80&rounded=true`);
                    });
            }

            window.currentSellerData = data.user;
            window.currentAdData = data;

            renderCategoryFields(data, category.fields, category.icon, categoryName);

            if (data.images?.length > 0) {
                const $indicators = $("#carouselIndicators").empty();
                const $inner = $("#carouselInner").empty();
                data.images.forEach((img, index) => {
                    const $indicator = $("<button>").attr({
                        type: "button",
                        "data-bs-target": "#carouselImages",
                        "data-bs-slide-to": index
                    });
                    if (index === 0) $indicator.addClass("active");
                    $indicators.append($indicator);

                    const $item = $("<div>").addClass(`carousel-item ${index === 0 ? "active" : ""}`);
                    $item.html(`<img src="${img.image_url}" class="d-block w-100 object-cover" style="height:480px;width:480px" alt="Image ${index + 1}">`);
                    $inner.append($item);
                });
            }

            if (data.location) {
                $("#locationDetails").html(`
                    <div class="col-12 mb-2"><strong>Address:</strong><br><span class="text-muted">${data.location.address}</span></div>
                    <div class="col-12 mb-2"><strong>City:</strong><br><span class="text-muted">${data.location.city}</span></div>
                    <div class="col-12 mb-2"><strong>District:</strong><br><span class="text-muted">${data.location.district}</span></div>
                `);
            }

            numericPostId = data.post_id;
            if (!numericPostId) {
                console.error("No numeric post_id in response");
            } else {
                console.log("Set numeric postId:", numericPostId);
            }
        },
        error: (xhr) => console.error("Error loading post:", xhr)
    });

    $(".nav-link").filter(function () {
        return $(this).text().trim() === "Browse";
    }).on("click", e => {
        e.preventDefault();
        window.location.href = "advertisement.html";
    });

    $("#breadcrumbNav .breadcrumb-item a").first().on("click", e => {
        e.preventDefault();
        window.location.href = "../index.html";
    });

    $('#reportCategory').on('change', function () {
        const customContainer = $('#customReasonContainer');
        const customReason = $('#customReason');

        if ($(this).val() === 'other') {
            customContainer.slideDown(300);
            customReason.prop('required', true);
        } else {
            customContainer.slideUp(300);
            customReason.prop('required', false).val('').removeClass('is-invalid');
        }
    });

    $('#reportDescription').on('input', function () {
        const currentLength = $(this).val().length;
        $('#reportCharCount').text(currentLength);

        if (currentLength > 450) {
            $('#reportCharCount').addClass('text-warning');
        } else {
            $('#reportCharCount').removeClass('text-warning');
        }
    });

    $('#reportBtn').on('click', function () {
        const adTitle = $('#adTitle').text() || 'Advertisement';
        const postIdText = $('#postId').text() || '-';

        $('#reportAdTitle').text(adTitle);
        $('#reportPostId').text(postIdText);

        $('#reportForm')[0].reset();
        $('#customReasonContainer').hide();
        $('#customReason').prop('required', false);
        $('#reportCharCount').text('0');
        $('.form-control, .form-select').removeClass('is-valid is-invalid');

        $('#reportModal').modal('show');
    });

    $('#submitReportBtn').on('click', function () {
        const $btn = $(this);
        const form = $('#reportForm')[0];

        let isValid = true;

        const category = $('#reportCategory').val();
        if (!category) {
            $('#reportCategory').addClass('is-invalid');
            isValid = false;
        } else {
            $('#reportCategory').removeClass('is-invalid').addClass('is-valid');
        }

        if (category === 'other') {
            const customReason = $('#customReason').val().trim();
            if (!customReason) {
                $('#customReason').addClass('is-invalid');
                isValid = false;
            } else {
                $('#customReason').removeClass('is-invalid').addClass('is-valid');
            }
        }

        const email = $('#reporterContact').val().trim();
        if (email && !isValidEmail(email)) {
            $('#reporterContact').addClass('is-invalid');
            isValid = false;
        } else {
            $('#reporterContact').removeClass('is-invalid');
            if (email) $('#reporterContact').addClass('is-valid');
        }

        if (!isValid) return;

        $btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-2" role="status"></span>Submitting...');

        const reportData = {
            postId: $('#reportPostId').text(),
            category: category,
            customReason: category === 'other' ? $('#customReason').val().trim() : null,
            description: $('#reportDescription').val().trim(),
            reporterContact: email || null,
            anonymous: $('#anonymousReport').is(':checked'),
            timestamp: new Date().toISOString()
        };

        setTimeout(() => {
            console.log('Report submitted:', reportData);
            showReportSuccessMessage();
            $('#reportModal').modal('hide');
            $btn.prop('disabled', false).html('<i class="bi bi-flag me-2"></i>Submit Report');
        }, 2000);
    });

    $('#addReviewBtnSidebar').on('click', function () {
        const adTitle = $('#adTitle').text() || 'Advertisement';
        const sellerName = $('#sellerNameValue').text() || 'Seller';

        $('#reviewAdTitle').text(adTitle);
        $('#reviewSellerName').text(sellerName);

        resetReviewForm();
        $('#reviewModal').modal('show');
    });

    $('.rating-selector .star-rating .star-btn').on('click', function () {
        const rating = $(this).data('value');
        const $starContainer = $(this).closest('.star-rating');

        $starContainer.attr('data-rating', rating);
        $('#selectedRating').val(rating);

        updateStarDisplay($starContainer, rating);

        const ratingTexts = ['', 'Poor', 'Fair', 'Good', 'Very Good', 'Excellent'];
        $('#ratingText').text(`${rating}/5 - ${ratingTexts[rating]}`);

        $starContainer.removeClass('is-invalid');
    });

    $('.aspect-rating .star-rating .star-btn').on('click', function () {
        const rating = $(this).data('value');
        const $starContainer = $(this).closest('.star-rating');

        $starContainer.attr('data-rating', rating);
        updateStarDisplay($starContainer, rating);
    });

    $('.star-rating .star-btn').on('mouseenter', function () {
        const hoverValue = $(this).data('value');
        const $starContainer = $(this).closest('.star-rating');

        $starContainer.find('.star-btn').each(function (index) {
            if (index + 1 <= hoverValue) {
                $(this).addClass('bi-star-fill').removeClass('bi-star');
            } else {
                $(this).addClass('bi-star').removeClass('bi-star-fill');
            }
        });
    });

    $('.star-rating').on('mouseleave', function () {
        const currentRating = parseInt($(this).attr('data-rating')) || 0;
        updateStarDisplay($(this), currentRating);
    });

    $('#reviewTitle').on('input', function () {
        const currentLength = $(this).val().length;
        $('#titleCharCount').text(currentLength);
    });

    $('#reviewContent').on('input', function () {
        const currentLength = $(this).val().length;
        $('#contentCharCount').text(currentLength);
    });

    $('#submitReviewBtn').off('click').on('click', function () {
        const $btn = $(this);

        let isValid = validateReviewForm();
        if (!isValid) return;

        $btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-2" role="status"></span>Submitting...');

        let finalPostId = numericPostId;
        if (!finalPostId || isNaN(finalPostId)) {
            console.error("Invalid numeric postId:", numericPostId, postId);
            showReviewErrorMessage('Invalid ad ID. Please refresh the page and try again.');
            $btn.prop('disabled', false).html('<i class="bi bi-check-lg me-2"></i>Submit Review');
            return;
        }

        const token = getCookie('token');
        if (!token) {
            showReviewErrorMessage('Please log in to submit a review');
            $btn.prop('disabled', false).html('<i class="bi bi-check-lg me-2"></i>Submit Review');
            return;
        }

        const reviewData = {
            rating: parseFloat($('#selectedRating').val()),
            title: $('#reviewTitle').val().trim(),
            content: $('#reviewContent').val().trim(),
            qualityRating: parseInt($('.aspect-rating[data-aspect="quality"] .star-rating').attr('data-rating')) || null,
            communicationRating: parseInt($('.aspect-rating[data-aspect="communication"] .star-rating').attr('data-rating')) || null,
            valueRating: parseInt($('.aspect-rating[data-aspect="value"] .star-rating').attr('data-rating')) || null,
            deliveryRating: parseInt($('.aspect-rating[data-aspect="delivery"] .star-rating').attr('data-rating')) || null,
            recommendation: $('input[name="recommendation"]:checked').val() === 'yes' ? 'YES' : 'NO',
            anonymous: $('#anonymousReview').is(':checked'),
            postId: finalPostId  // FIXED: Numeric, non-null
        };

        console.log('Submitting review:', reviewData);

        $.ajax({
            url: 'http://localhost:8080/api/reviews',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            data: JSON.stringify(reviewData),
            success: function (response) {
                console.log('Review submitted successfully:', response);
                showReviewSuccessMessage();
                $('#reviewModal').modal('hide');
                updateSidebarReviewStats();
            },
            error: function (xhr, status, error) {
                console.error('Review submission failed:', xhr.responseText);
                let errorMsg = 'Failed to submit review. Please try again later.';
                if (xhr.status === 400) {
                    errorMsg = 'Invalid review data or ad not found.';
                } else if (xhr.status === 401) {
                    errorMsg = 'Please log in again.';
                }
                showReviewErrorMessage(errorMsg);
            },
            complete: function () {
                $btn.prop('disabled', false).html('<i class="bi bi-check-lg me-2"></i>Submit Review');
            }
        });
    });

    $('#viewAllReviewsBtn').on('click', function () {
        console.log('View all reviews clicked');
        // window.location.href = `reviews.html?postId=${numericPostId}`;
    });

    addBreadcrumb("Home", "../index.html");
    addBreadcrumb(categoryName, "#");
    addBreadcrumb("Ad Details", "#");
});