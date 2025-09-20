let postList = [];
let selectedStatus = 'PENDING';
let currentPage = 0;
let currentPostCard = null;
let postImages = [];
let currentImageIndex = 0;
const pageSize = 5;
let reportsList = [];
let currentReportsPage = 0;
const reportsPageSize = 10;

$(document).ready(function () {
    const $navLinks = $('.nav-link');
    const $contentSections = $('.content-section');
    const $pageTitle = $('#pageTitle');
    const $sidebarToggle = $('#sidebarToggle');
    const $sidebar = $('#sidebar');
    const $mainContent = $('.main-content');

    $("#addModeratorForm").on("submit", function (e) {
        e.preventDefault();

        $("#loadingSpinner").removeClass("d-none");
        $("#submitModeratorBtn").prop("disabled", true);

        const moderatorData = {
            name: $("#moderatorName").val(),
            email: $("#moderatorEmail").val()
        };

        $.ajax({
            url: "http://localhost:8080/api/users",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(moderatorData),
            headers: {
                "Authorization": "Bearer " + getCookie("token")
            },
            success: function (response) {
                $("#addModeratorModal").modal("hide");

                $("#addModeratorForm")[0].reset();

                alert("Moderator added successfully!");

                $("#loadingSpinner").addClass("d-none");
                $("#submitModeratorBtn").prop("disabled", false);
            },
            error: function (xhr) {
                if (xhr.responseJSON) {
                    if (xhr.responseJSON.name) {
                        $("#nameError").text(xhr.responseJSON.name).show();
                        $("#moderatorName").addClass("is-invalid");
                    }
                    if (xhr.responseJSON.email) {
                        $("#emailError").text(xhr.responseJSON.email).show();
                        $("#moderatorEmail").addClass("is-invalid");
                    }
                } else {
                    alert("Something went wrong. Please try again.");
                }

                $("#loadingSpinner").addClass("d-none");
                $("#submitModeratorBtn").prop("disabled", false);
            }
        });
    });

    $.ajax({
        url: "http://localhost:8080/api/users/all",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + getCookie("token")
        },
        success: async function (users) {
            console.log(users)
            const $tbody = $("#userTableBody");
            $tbody.empty();

            const normalUsers = users.filter(user => user.role !== "ADMIN");

            for (const user of normalUsers) {
                let postsCount = 0;
                try {
                    console.log(user.id)
                    const response = await $.ajax({
                        url: `http://localhost:8080/api/posts/count/by-user/${user.id}`,
                        method: "GET",
                        headers: {
                            "Authorization": "Bearer " + getCookie("token")
                        }
                    });
                    postsCount = response;
                } catch (err) {
                    console.error("Failed to fetch post count for user", user.id);
                }

                const row = `
                <tr>
                    <td>
                        <div class="d-flex align-items-center">
                            <img alt="User" class="avatar me-3"
                                 src="${user.avatarUrl || 'https://picsum.photos/seed/default/40/40'}">
                            <div>
                                <div class="fw-semibold">${user.name}</div>
                                <small class="text-muted">ID: #${user.id}</small>
                            </div>
                        </div>
                    </td>
                    <td>${user.email}</td>
                    <td><span class="badge bg-primary">${postsCount}</span></td>
                    <td>${formatDate(user.joinDate)}</td>
                </tr>`;
                $tbody.append(row);
            }
        },
        error: function () {
            console.error("Failed to fetch users");
        }
    });

// Helper function to format date
    function formatDate(dateString) {
        const options = {year: 'numeric', month: 'short', day: 'numeric'};
        const date = new Date(dateString);
        return date.toLocaleDateString(undefined, options);
    }


    function formatDate(isoString) {
        const date = new Date(isoString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${year}-${month}-${day}`;
    }

    $(".dropdown-item:contains('Logout')").on("click", function (e) {
        e.preventDefault();
        doLogout("../index.html");
    });

    $navLinks.on('click', function (e) {
        e.preventDefault();
        $navLinks.removeClass('active');
        $(this).addClass('active');
        $contentSections.removeClass('active');

        const targetSection = $(this).data('section') + '-section';
        const $section = $('#' + targetSection);
        if ($section.length) $section.addClass('active');

        $pageTitle.text($(this).text().trim());
    });

    $sidebarToggle.on('click', function () {
        $sidebar.toggleClass('collapsed');
        $mainContent.toggleClass('expanded');
    });

    const $categoryItems = $('.category-item');
    const $selectedCategory = $('#selectedCategory');
    const $statusFilter = $('#statusFilter');
    const $searchPosts = $('#searchPosts');

    let currentFilters = {
        category: 'all',
        status: 'pending',
        search: ''
    };

    $categoryItems.on('click', function (e) {
        e.preventDefault();
        $categoryItems.removeClass('active');
        $(this).addClass('active');

        const categoryName = $(this).text().trim().split('\n')[0];
        const categoryIcon = $(this).find('i').prop('outerHTML');
        $selectedCategory.html(categoryIcon + ' ' + categoryName);

        currentFilters.category = $(this).data('category') || 'all';
        applyAllFilters();
    });

    $statusFilter.on('change', function () {
        const newStatus = $(this).val().toUpperCase();
        selectedStatus = newStatus;
        currentFilters.status = newStatus.toLowerCase();
        loadPosts(0, newStatus);
    });

    $searchPosts.on('input', function () {
        currentFilters.search = $(this).val().toLowerCase();
        applyAllFilters();
    });

    loadPosts(0);
});


// Load reports when reports section is activated
$(document).on('click', '[data-section="reports"]', function () {
    loadReports();
});

function loadReports(page = 0) {
    currentReportsPage = page;

    $.ajax({
        url: "http://localhost:8080/api/reports",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (reports) {
            reportsList = reports;
            renderReports(reports);
            updateReportsStats(reports);
            renderReportsPagination(page, Math.ceil(reports.length / reportsPageSize));
        },
        error: function (xhr) {
            console.error("Failed to load reports:", xhr);
            $('#reportsContainer').html('<p class="text-center text-muted">Failed to load reports</p>');
        }
    });
}

function renderReports(reports) {
    const $container = $("#reportsContainer");
    $container.empty();

    if (!reports.length) {
        $container.html('<p class="text-center text-muted">No reports found</p>');
        $('#reportsCount').text('0');
        return;
    }

    const start = currentReportsPage * reportsPageSize;
    const paginatedReports = reports.slice(start, start + reportsPageSize);

    $('#reportsCount').text(reports.length);

    paginatedReports.forEach(report => {
        const reportDate = new Date(report.date).toLocaleDateString();
        const isAnonymous = report.anonymous || !report.reporterName;

        const card = `
            <div class="card mb-3 report-card" data-report-id="${report.report_id}">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-7">
                            <div class="d-flex align-items-center mb-2">
                                <span class="badge bg-danger me-2">
                                    <i class="bi bi-flag-fill me-1"></i>Report #${report.report_id}
                                </span>
                                <span class="text-muted small">${reportDate}</span>
                                ${isAnonymous ? '<span class="badge bg-secondary ms-2">Anonymous</span>' : ''}
                            </div>
                            <h6 class="mb-1">${report.reason}</h6>
                            <p class="text-muted mb-2">${report.description || 'No description provided'}</p>
                            <div class="d-flex align-items-center">
                                <i class="bi bi-file-post me-1"></i>
                                <small class="text-primary">Post: ${report.postTitle || `ID #${report.postId}`}</small>
                            </div>
                            ${!isAnonymous ? `<div class="mt-1"><i class="bi bi-person me-1"></i><small>By: ${report.reporterName}</small></div>` : ''}
                        </div>
                        <div class="col-md-5 text-end">
                            <div class="d-flex gap-2 flex-wrap justify-content-end">
                                <button class="btn btn-outline-primary btn-sm" onclick="viewReportDetail(${report.report_id})">
                                    <i class="bi bi-eye me-1"></i>View Details
                                </button>
                                <button class="btn btn-success btn-sm" onclick="markAsReviewed(${report.report_id})">
                                    <i class="bi bi-check-circle me-1"></i>Reviewed
                                </button>
                                <button class="btn btn-outline-danger btn-sm" onclick="markAsRejected(${report.report_id})">
                                    <i class="bi bi-x-circle me-1"></i>Rejected
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}

// Placeholder functions for the buttons (you'll implement logic later)
function markAsReviewed(reportId) {
    console.log('Mark as reviewed:', reportId);
    // Your logic here
}

function markAsRejected(reportId) {
    console.log('Mark as rejected:', reportId);
    // Your logic here
}

function updateReportsStats(reports) {
    $('#totalReports').text(reports.length);
    $('#pendingReports').text(reports.length); // All reports are essentially "pending review"
}

function viewReportDetail(reportId) {
    const report = reportsList.find(r => r.report_id === reportId);
    if (!report) return;

    $('#modalReportReason').text(report.reason);

    if (report.customReason) {
        $('#modalCustomReason').text(report.customReason);
        $('#modalCustomReasonDiv').show();
    } else {
        $('#modalCustomReasonDiv').hide();
    }

    $('#modalReportDescription').text(report.description || 'No description provided');
    $('#modalReporterContact').text(report.reporterContact || 'Not provided');
    $('#modalReportDate').text(new Date(report.date).toLocaleString());
    $('#modalPostTitle').text(report.postTitle || 'Unknown Post');
    $('#modalPostId').text(report.postId);

    // Set up view post button
    $('#viewPostBtn').off('click').on('click', function () {
        // You can implement this to show the post detail modal
        // For now, just close the report modal
        $('#reportDetailModal').modal('hide');
        // Then show post detail if you have that functionality
    });

    // Set up delete button
    $('#deleteReportBtn').off('click').on('click', function () {
        deleteReport(reportId);
        $('#reportDetailModal').modal('hide');
    });

    $('#reportDetailModal').modal('show');
}

function deleteReport(reportId) {
    if (!confirm('Are you sure you want to delete this report?')) return;

    $.ajax({
        url: `http://localhost:8080/api/reports/${reportId}`,
        method: "DELETE",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            showToast('Report deleted successfully!');
            loadReports(currentReportsPage);
        },
        error: function (xhr) {
            console.error("Failed to delete report:", xhr);
            showToast('Failed to delete report', 'error');
        }
    });
}

function renderReportsPagination(current, totalPages) {
    const $pagination = $("#reportsPagination");
    $pagination.empty();

    if (totalPages <= 1) return;

    $pagination.append(`<li class="page-item ${current === 0 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="loadReports(${current - 1})">Previous</a></li>`);

    for (let i = 0; i < totalPages; i++) {
        $pagination.append(`<li class="page-item ${i === current ? 'active' : ''}">
            <a class="page-link" href="#" onclick="loadReports(${i})">${i + 1}</a></li>`);
    }

    $pagination.append(`<li class="page-item ${current === totalPages - 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="loadReports(${current + 1})">Next</a></li>`);
}

// Search functionality for reports
$('#searchReports').on('input', function () {
    const searchTerm = $(this).val().toLowerCase();
    $('.report-card').each(function () {
        const reportText = $(this).text().toLowerCase();
        $(this).toggle(reportText.includes(searchTerm));
    });
});

function showPostDetail(postData) {
    try {
        $('#modalPostId').val(postData.id || postData.post_id);
        $("#modalPostTitle").text(postData.title || "No Title");
        $("#modalPostDescription").text(postData.description || "No Description");
        $("#modalPostCategory").text(postData.category?.name || "No Category");
        $("#modalPostStatus").text(postData.status || "Unknown");
        $("#modalPostPrice").text(postData.price ? "Rs. " + Number(postData.price).toLocaleString() : "N/A");

        $("#modalUserName").text(postData.user?.name || "Unknown user");
        $("#modalUserAvatar").attr("src", postData.userAvatar || "https://via.placeholder.com/40");
        $("#modalPostTime").text(postData.createdAt ? new Date(postData.createdAt).toLocaleString() : "N/A");

        $("#modalContactPhone").text(postData.contact_number || "N/A");
        $("#modalContactEmail").text(postData.user?.email || "N/A");
        $("#modalContactLocation").text(postData.location?.address || "N/A");

        const $paymentDiv = $("#modalPaymentMethod").empty();
        if (postData.payment?.payment_type === "BANK_TRANSFER") {
            const bankIcon = `<i class="bi bi-bank2 me-2 text-primary"></i>`;
            $paymentDiv.append(bankIcon + "Bank Transfer");
            if (postData.payment.slip_url) {
                const slipLink = $(`<a href="${postData.payment.slip_url}" target="_blank" class="ms-2 text-decoration-none">
                                        <i class="bi bi-receipt-cutoff me-1"></i>View Bank Slip
                                    </a>`);
                $paymentDiv.append(slipLink);
            }
        } else if (postData.payment?.payment_type) {
            $paymentDiv.text(postData.payment.payment_type);
        } else {
            $paymentDiv.text("N/A");
        }

        postImages = (postData.images || []).map(img => (typeof img === 'string' ? img : img.image_url));
        currentImageIndex = 0;
        setupImageGallery();

        const $actionDiv = $("#modalActionButtons").empty();
        if (postData.status === 'PENDING') {
            const approveBtn = $('<button class="btn btn-success me-2">Approve</button>');
            const rejectBtn = $('<button class="btn btn-danger">Reject</button>');

            approveBtn.on('click', approvePostFromModal);
            rejectBtn.on('click', rejectPostFromModal);

            $actionDiv.append(approveBtn, rejectBtn);
        } else if (postData.status === 'APPROVED') {
            $actionDiv.append('<span class="text-success fw-bold">Already Approved</span>');
        }

        $('body').css('overflow', 'hidden');
        $('#postDetailModal').css('display', 'flex');
    } catch (error) {
        console.error("[showPostDetail] Error:", error);
    }
}

function closePostDetail() {
    $('#postDetailModal').hide();
    $('body').css('overflow', 'auto');
    currentPostCard = null;
}

function approvePostFromModal() {
    if (!currentPostCard) return;
    const postData = $(currentPostCard).data('post');

    $.ajax({
        url: `http://localhost:8080/api/posts/${postData.post_id}/status/APPROVED`,
        method: 'PUT',
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            $(currentPostCard).data('status', 'APPROVED');
            $(currentPostCard).find('.status-badge')
                .text('APPROVED')
                .removeClass()
                .addClass('status-badge status-approved');

            $(currentPostCard).find('.btn-success, .btn-danger').remove();

            showToast('Post approved successfully!');
            closePostDetail();
            applyAllFilters();
        },
        error: function (xhr) {
            console.error("Failed to approve post:", xhr);
            showToast('Failed to approve post.');
        }
    });
}


function rejectPostFromModal() {
    if (!currentPostCard) return;
    const postData = $(currentPostCard).data('post');

    if (!confirm('Are you sure you want to reject this post?')) return;

    $.ajax({
        url: `http://localhost:8080/api/posts/${postData.post_id}/status/REJECTED`,
        method: 'PUT',
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            $(currentPostCard).remove();
            showToast('Post rejected successfully!');
            closePostDetail();
            applyAllFilters();
        },
        error: function (xhr) {
            console.error("Failed to reject post:", xhr);
            showToast('Failed to reject post.');
        }
    });
}

function showToast(message) {
    $('.toast-notification').remove();
    const $toast = $(`
        <div class="toast-notification position-fixed top-0 end-0 m-3 alert alert-success" style="z-index: 9999;">
            <i class="bi bi-check-circle me-2"></i>${message}
            <button type="button" class="btn-close ms-2"></button>
        </div>
    `);
    $toast.find('.btn-close').on('click', () => $toast.remove());
    $('body').append($toast);
    setTimeout(() => $toast.remove(), 3000);
}

function applyAllFilters() {
    const $posts = $('.post-card');
    const search = $('#searchPosts').val().toLowerCase();
    const statusFilter = selectedStatus.toLowerCase();
    const categoryFilter = $('.category-item.active').data('category') || 'all';

    let visibleCount = 0;

    $posts.each(function () {
        const $post = $(this);
        const postStatus = ($post.data('status') || '').toLowerCase();
        const postCategory = ($post.data('category') || '').toLowerCase();
        const postTitle = ($post.data('post')?.title || '').toLowerCase();
        const postDesc = ($post.data('post')?.description || '').toLowerCase();

        let shouldShow = true;

        if (statusFilter !== 'all' && postStatus !== statusFilter) shouldShow = false;
        if (categoryFilter !== 'all' && postCategory !== categoryFilter) shouldShow = false;
        if (search && !postTitle.includes(search) && !postDesc.includes(search)) shouldShow = false;

        if (shouldShow) {
            $post.show();
            visibleCount++;
        } else $post.hide();
    });

    $('#postsCount').text(visibleCount.toLocaleString());
}

function setupImageGallery() {
    const $container = $('#imageGalleryContainer');
    const $indicators = $('#imageIndicators');
    $container.empty();
    $indicators.empty();

    if (!postImages.length) {
        $container.html('<img src="/placeholder.svg" alt="No Image" class="post-detail-image active">');
        return;
    }

    if (postImages.length > 1) {
        $container.html(`
            <button class="image-nav prev">‹</button>
            <button class="image-nav next">›</button>
        `);
        $container.find('.prev').on('click', previousImage);
        $container.find('.next').on('click', nextImage);
    }

    postImages.forEach((src, index) => {
        $container.append(`<img src="${src}" alt="Image ${index + 1}" class="post-detail-image ${index === 0 ? 'active' : ''}">`);
    });

    postImages.forEach((_, index) => {
        const $indicator = $(`<div class="image-indicator ${index === 0 ? 'active' : ''}"></div>`);
        $indicator.on('click', () => goToImage(index));
        $indicators.append($indicator);
    });
}

function nextImage() {
    currentImageIndex = (currentImageIndex + 1) % postImages.length;
    updateImageDisplay();
}

function previousImage() {
    currentImageIndex = (currentImageIndex - 1 + postImages.length) % postImages.length;
    updateImageDisplay();
}

function goToImage(index) {
    currentImageIndex = index;
    updateImageDisplay();
}

function updateImageDisplay() {
    const $images = $('#imageGalleryContainer .post-detail-image');
    const $indicators = $('.image-indicator');
    $images.each((i, el) => $(el).toggleClass('active', i === currentImageIndex));
    $indicators.each((i, el) => $(el).toggleClass('active', i === currentImageIndex));
}

function loadPosts(page = 0, status) {
    currentPage = page;
    if (status) selectedStatus = status;

    $.ajax({
        url: `http://localhost:8080/api/posts/page?page=${page}&size=${pageSize}&status=${encodeURIComponent(selectedStatus)}`,
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            postList = data.content || [];
            renderPosts(postList);
            renderPagination(data.pageNumber, data.totalPages);
            applyAllFilters();
        },
        error: function (xhr) {
            console.error("Failed to load posts:", xhr);
        }
    });
}

function renderPosts(posts) {
    const $container = $("#postsContainer");
    $container.empty();

    if (!posts.length) {
        $container.html("<p class='text-center text-muted'>No posts found</p>");
        return;
    }

    posts.forEach(post => {
        const createdTime = post.createdAt ? new Date(post.createdAt).toLocaleString() : "Unknown time";
        const mainImage = (post.images && post.images.length > 0) ? (post.images[0].image_url || post.images[0]) : "https://via.placeholder.com/120x80";
        const card = `
            <div class="post-card card mb-3" 
                 data-category="${post.category?.name?.toLowerCase() || "unknown"}"
                 data-status="${post.status?.toLowerCase() || "unknown"}"
                 data-post='${JSON.stringify(post)}'>
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-2 col-3">
                            <img alt="Post Image" class="img-fluid rounded" src="${mainImage}">
                        </div>
                        <div class="col-md-7 col-6">
                            <h6 class="mb-1">${post.title || "No title"}</h6>
                            <div class="d-flex align-items-center mb-1">
                                <img alt="User" class="avatar me-2 rounded-circle" src="${post.userAvatar || "https://via.placeholder.com/24"}">
                                <span class="text-muted">${post.user?.name || "Unknown"} • ${createdTime}</span>
                            </div>
                            <p class="text-muted mb-2" style="font-size:0.8rem;line-height:1.3;">${post.description || ""}</p>
                            <div class="d-flex gap-2">
                                <span class="badge bg-primary">${post.category?.name || "No Category"}</span>
                                <span class="status-badge status-${(post.status || "unknown").toLowerCase()}">${post.status || "Unknown"}</span>
                            </div>
                        </div>
                        <div class="col-md-3 col-3 text-end">
                            <h6 class="text-success mb-2">${post.price ? "Rs. " + Number(post.price).toLocaleString() : "No Price"}</h6>
                            <button class="btn btn-primary" onclick="event.stopPropagation(); showPostDetailFromCard(this)" title="View Details">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}

function showPostDetailFromCard(button) {
    const $card = $(button).closest('.post-card');
    const postData = JSON.parse($card.attr('data-post'));
    $card.data('post', postData);
    currentPostCard = $card;
    showPostDetail(postData);
}

function renderPagination(current, totalPages) {
    const $pagination = $(".pagination");
    $pagination.empty();

    $pagination.append(`<li class="page-item ${current === 0 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="changePage(${current - 1})">Previous</a></li>`);

    for (let i = 0; i < totalPages; i++) {
        $pagination.append(`<li class="page-item ${i === current ? 'active' : ''}">
            <a class="page-link" href="#" onclick="changePage(${i})">${i + 1}</a></li>`);
    }

    $pagination.append(`<li class="page-item ${current === totalPages - 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="changePage(${current + 1})">Next</a></li>`);
}

function changePage(page) {
    if (page < 0) return;
    loadPosts(page);
}

// Add this function to your admindashboard.js file

function resetFilters() {
    $('#statusFilter').val('PENDING');
    selectedStatus = 'PENDING';

    $('.category-item').removeClass('active');
    $('.category-item[data-category="all"]').addClass('active');

    const $selectedCategory = $('#selectedCategory');
    $selectedCategory.html('<i class="bi bi-grid me-2"></i>All Categories');

    $('#dateFilter').val('all');

    $('#searchPosts').val('');

    currentFilters = {
        category: 'all',
        status: 'pending',
        search: ''
    };

    currentPage = 0;
    loadPosts(0, 'PENDING');

    showToast('Filters have been reset successfully!');
}


function resetStatusFilter() {
    $('#statusFilter').val('PENDING');
    selectedStatus = 'PENDING';
    currentFilters.status = 'pending';
    loadPosts(0, 'PENDING');
}

function resetCategoryFilter() {
    $('.category-item').removeClass('active');
    $('.category-item[data-category="all"]').addClass('active');
    $('#selectedCategory').html('<i class="bi bi-grid me-2"></i>All Categories');
    currentFilters.category = 'all';
    applyAllFilters();
}

function resetSearchFilter() {
    $('#searchPosts').val('');
    currentFilters.search = '';
    applyAllFilters();
}

function resetDateFilter() {
    $('#dateFilter').val('all');
}