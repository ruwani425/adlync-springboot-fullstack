const $ = window.jQuery;

let stompClient = null;

$(document).ready(() => {
    // Initialize user profile
    initializeProfileForAdDetails();

    // Load post and category info from URL
    const urlParams = new URLSearchParams(window.location.search);
    const categoryName = urlParams.get("categoryName");
    const postId = urlParams.get("postId");

    if (!categoryName || !postId) return;

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
    if (!category) return console.warn("Category not found");

    updateCategoryDetailsTitle(category.icon, categoryName);

    // Fetch post details
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
            console.log(data)
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
                setCookie("receiverId",seller.name);

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

            // Render images carousel
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

            // Render location
            if (data.location) {
                $("#locationDetails").html(`
                    <div class="col-12 mb-2"><strong>Address:</strong><br><span class="text-muted">${data.location.address}</span></div>
                    <div class="col-12 mb-2"><strong>City:</strong><br><span class="text-muted">${data.location.city}</span></div>
                    <div class="col-12 mb-2"><strong>District:</strong><br><span class="text-muted">${data.location.district}</span></div>
                `);
            }
        },
        error: (xhr) => console.error("Error loading post:", xhr)
    });

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

    // Navigation
    $(".nav-link").filter(function () {
        return $(this).text().trim() === "Browse";
    })
        .on("click", e => {
            e.preventDefault();
            window.location.href = "advertisement.html";
        });

    $("#breadcrumbNav .breadcrumb-item a").first().on("click", e => {
        e.preventDefault();
        window.location.href = "../index.html";
    });

    function addBreadcrumb(name, url = "#") {
        const $breadcrumb = $(`<li class="breadcrumb-item"><a class="text-decoration-none text-primary-emerald" href="${url}">${name}</a></li>`);
        $("#breadcrumbNav").append($breadcrumb);
    }
});

// ====== Profile Handling ======
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

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
}

function deleteCookie(name) {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
}

// Chat button handlers
$(document).ready(() => {

    // Report Modal JavaScript
    // Show/hide custom reason field
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

    // Character counter for description
    $('#reportDescription').on('input', function () {
        const currentLength = $(this).val().length;
        $('#reportCharCount').text(currentLength);

        if (currentLength > 450) {
            $('#reportCharCount').addClass('text-warning');
        } else {
            $('#reportCharCount').removeClass('text-warning');
        }
    });

    // Report button click handler
    $('#reportBtn').on('click', function () {
        // Populate modal with current ad data
        const adTitle = $('#adTitle').text() || 'Advertisement';
        const postId = $('#postId').text() || '-';

        $('#reportAdTitle').text(adTitle);
        $('#reportPostId').text(postId);

        // Reset form
        $('#reportForm')[0].reset();
        $('#customReasonContainer').hide();
        $('#customReason').prop('required', false);
        $('#reportCharCount').text('0');
        $('.form-control, .form-select').removeClass('is-valid is-invalid');

        // Show modal
        $('#reportModal').modal('show');
    });

    // Submit report handler
    $('#submitReportBtn').on('click', function () {
        const $btn = $(this);
        const form = $('#reportForm')[0];

        // Validate form
        let isValid = true;

        // Validate category
        const category = $('#reportCategory').val();
        if (!category) {
            $('#reportCategory').addClass('is-invalid');
            isValid = false;
        } else {
            $('#reportCategory').removeClass('is-invalid').addClass('is-valid');
        }

        // Validate custom reason if "other" is selected
        if (category === 'other') {
            const customReason = $('#customReason').val().trim();
            if (!customReason) {
                $('#customReason').addClass('is-invalid');
                isValid = false;
            } else {
                $('#customReason').removeClass('is-invalid').addClass('is-valid');
            }
        }

        // Validate email format if provided
        const email = $('#reporterContact').val().trim();
        if (email && !isValidEmail(email)) {
            $('#reporterContact').addClass('is-invalid');
            isValid = false;
        } else {
            $('#reporterContact').removeClass('is-invalid');
            if (email) $('#reporterContact').addClass('is-valid');
        }

        if (!isValid) {
            return;
        }

        // Show loading state
        $btn.prop('disabled', true)
            .html('<span class="spinner-border spinner-border-sm me-2" role="status"></span>Submitting...');

        // Prepare report data
        const reportData = {
            postId: $('#reportPostId').text(),
            category: category,
            customReason: category === 'other' ? $('#customReason').val().trim() : null,
            description: $('#reportDescription').val().trim(),
            reporterContact: email || null,
            anonymous: $('#anonymousReport').is(':checked'),
            timestamp: new Date().toISOString()
        };

        // Simulate API call (replace with actual API endpoint)
        setTimeout(() => {
            console.log('Report submitted:', reportData);

            // Show success message
            showReportSuccessMessage();

            // Close modal
            $('#reportModal').modal('hide');

            // Reset button
            $btn.prop('disabled', false)
                .html('<i class="bi bi-flag me-2"></i>Submit Report');

        }, 2000);

        // In a real implementation, you would make an AJAX call like this:
        /*
        $.ajax({
            url: 'http://localhost:8080/api/reports/submit',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getCookie('token')
            },
            data: JSON.stringify(reportData),
            success: function(response) {
                showReportSuccessMessage();
                $('#reportModal').modal('hide');
            },
            error: function(xhr, status, error) {
                showReportErrorMessage();
                console.error('Report submission failed:', error);
            },
            complete: function() {
                $btn.prop('disabled', false)
                    .html('<i class="bi bi-flag me-2"></i>Submit Report');
            }
        });
        */
    });

    // Helper function to validate email
    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // Success message
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

        // Auto-remove after 5 seconds
        setTimeout(() => {
            $('.alert-success').fadeOut();
        }, 5000);
    }

    // Error message
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

        // Auto-remove after 5 seconds
        setTimeout(() => {
            $('.alert-danger').fadeOut();
        }, 5000);
    }
});
