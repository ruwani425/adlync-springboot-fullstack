let filteredPosts = [];
let currentPost = null;
let currentPage = 0;
const pageSize = 10;
let selectedStatus = 'PENDING';
let selectedCategory = '';
let selectedDateRange = 'all';
let reportsList = [];
let currentReportsPage = 0;
const reportsPageSize = 10;
let selectedReportStatus = 'PENDING';
let selectedReportDateRange = 'all';

$(document).ready(function () {
    loadPosts();

    if ($('#dashboard').hasClass('active')) {
        loadDashboardStatsOptimized();
    }

    $(document).on('click', '[onclick*="showSection(\'dashboard\')"]', function () {
        setTimeout(() => {
            loadDashboardStatsOptimized();
        }, 100);
    });

    $(document).on('postActionCompleted', function () {
        refreshDashboardStats();
    });

    $('#reportStatusFilter').on('change', () => {
        selectedReportStatus = $('#reportStatusFilter').val();
        currentReportsPage = 0;
        loadReports();
    });

    $('#reportDateFilter').on('change', () => {
        selectedReportDateRange = $('#reportDateFilter').val();
        currentReportsPage = 0;
        loadReports();
    });

    $('.nav-link').on('click', function (e) {
        e.preventDefault();
        const section = $(this).attr('onclick').match(/'(\w+)'/)[1];
        showSection(section, this);
    });

    $('#statusFilter').on('change', () => {
        selectedStatus = $('#statusFilter').val();
        currentPage = 0;
        loadPosts();
    });

    $('#dateFilter').on('change', () => {
        selectedDateRange = $('#dateFilter').val();
        currentPage = 0;
        loadPosts();
    });

    const $categoryItems = $('.category-item');
    const $selectedCategory = $('#selectedCategory');

    $categoryItems.on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();

        $categoryItems.removeClass('active');
        $(this).addClass('active');

        const categoryText = $(this).text().trim().split('\n')[0].trim();
        const categoryIcon = $(this).find('i').prop('outerHTML');
        $selectedCategory.html(categoryIcon + ' ' + categoryText);

        selectedCategory = $(this).data('category') || '';
        if (selectedCategory === 'all') selectedCategory = '';

        currentPage = 0;
        loadPosts();

        $('#categoryDropdown').dropdown('hide');
    });

    $('#searchFilter').on('keyup', () => {
        currentPage = 0;
        loadPosts();
    });

    $('.btn-outline-secondary').not('.dropdown-toggle').on('click', resetFilters);
});

function formatPrice(value) {
    return new Intl.NumberFormat('en-LK', {style: 'currency', currency: 'LKR'}).format(value || 0);
}

function getCategoryName(category) {
    if (!category) return "Unknown";
    return typeof category === "string"
        ? category
        : (category.name || "Unknown");
}

function getSubcategoryName(subcategory) {
    if (!subcategory) return "";
    return typeof subcategory === "string"
        ? subcategory
        : (subcategory.name || "");
}

function showSection(sectionName, btn) {
    $('.section').removeClass('active');
    $('#' + sectionName).addClass('active');
    $('.nav-link').removeClass('active');
    $(btn).addClass('active');

    if (sectionName === 'posts') loadPosts();
    if (sectionName === 'reports') loadReports();
}

function loadPosts(page = currentPage) {
    currentPage = page;
    const searchQuery = $('#searchFilter').val() || '';

    let params = {
        page: currentPage,
        size: pageSize,
        status: selectedStatus || null
    };

    if (selectedCategory && selectedCategory !== 'all') {
        params.category = selectedCategory;
    }

    if (selectedDateRange && selectedDateRange !== 'all') {
        const now = new Date();
        let startDate = null;

        switch (selectedDateRange) {
            case 'today':
                startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                break;
            case 'week':
                startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
                break;
            case 'month':
                startDate = new Date(now.getFullYear(), now.getMonth(), 1);
                break;
        }

        if (startDate) {
            params.startDate = startDate.toISOString().split('T')[0];
        }
    }

    const queryString = new URLSearchParams(params).toString();

    $.ajax({
        url: `http://localhost:8080/api/posts/page?${queryString}`,
        method: 'GET',
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            filteredPosts = data.content || [];
            renderPosts(filteredPosts);
            renderPagination(data.pageNumber, data.totalPages);
            updateCategoryCounts();
        },
        error: function () {
            $('#postsGrid').html('<p class="text-center text-danger">Failed to load posts.</p>');
        }
    });
}

function loadReports(page = currentReportsPage) {
    currentReportsPage = page;

    $.ajax({
        url: "http://localhost:8080/api/reports/status/PENDING",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (reports) {
            reportsList = reports;
            renderReports(reports);
            updateReportsStats(reports);
            renderReportsPagination(page, Math.ceil(reports.length / reportsPageSize));
            $('#reportsCountInfo').text(`Pending Reports (${reports.length})`);
        },
        error: function (xhr) {
            console.error("Failed to load reports:", xhr);
            $('#reportsTableBody').html('<tr><td colspan="6" class="text-center text-danger">Failed to load reports.</td></tr>');
            showToast('Failed to load reports.', 'error');
        }
    });
}

function renderReports(reports) {
    const tbody = $('#reportsTableBody');
    tbody.empty();

    if (!reports.length) {
        tbody.html('<tr><td colspan="6" class="text-center text-muted">No reports found.</td></tr>');
        return;
    }

    const start = currentReportsPage * reportsPageSize;
    const paginatedReports = reports.slice(start, start + reportsPageSize);

    paginatedReports.forEach(report => {
        const statusClass = `status-${report.status?.toLowerCase()}`;
        const reportId = report.id || report.report_id;

        const row = `
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <div>
                            <div class="fw-semibold">${report.reporterName || 'Unknown'}</div>
                            <small class="text-muted">${report.reporterEmail || ''}</small>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="fw-semibold">${report.postTitle || 'N/A'}</div>
                    <small class="text-muted">Post ID: #${report.postId || 'N/A'}</small>
                </td>
                <td>
                    <span class="badge bg-secondary">${report.reason || 'Custom Reason'}</span>
                    <div class="text-muted small mt-1">${report.customReason || report.description || ''}</div>
                </td>
                <td>${new Date(report.date).toLocaleDateString()}</td>
                <td><span class="badge ${statusClass}">${report.status || 'PENDING'}</span></td>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        ${report.status === 'PENDING' ? `
                            <button class="btn btn-success" onclick="markAsReviewed(${reportId})" title="Approve report and mark post as reported">
                                <i class="bi bi-check"></i> Review
                            </button>
                            <button class="btn btn-danger" onclick="markAsRejected(${reportId})" title="Delete this report">
                                <i class="bi bi-trash"></i> Reject
                            </button>
                        ` : `
                            <button class="btn btn-outline-secondary disabled">Handled</button>
                        `}
                        <button class="btn btn-outline-primary" onclick="viewReportDetail(${reportId})">
                            <i class="bi bi-eye"></i> View
                        </button>
                    </div>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

function renderReportsPagination(current, totalPages) {
    const container = $('#reportsPagination');
    container.empty();

    if (totalPages <= 1) return;

    container.append(`<li class="page-item ${current === 0 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="loadReports(${current - 1})">Previous</a></li>`);

    for (let i = 0; i < totalPages; i++) {
        const activeClass = i === current ? 'active' : '';
        container.append(`<li class="page-item ${activeClass}">
            <a class="page-link" href="#" onclick="loadReports(${i})">${i + 1}</a></li>`);
    }

    container.append(`<li class="page-item ${current === totalPages - 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" onclick="loadReports(${current + 1})">Next</a></li>`);
}

function viewReportDetail(reportId) {
    const report = reportsList.find(r => (r.id || r.report_id) == reportId);
    if (!report) {
        console.error('Report not found with ID:', reportId);
        showToast('Report not found', 'error');
        return;
    }

    const modalContent = `
        <div class="row">
            <div class="col-md-6">
                <h5>Reported Post</h5>
                <p><strong>Title:</strong> ${report.postTitle || 'N/A'}</p>
                <p><strong>Post ID:</strong> #${report.postId || 'N/A'}</p>
            </div>
            <div class="col-md-6">
                <h5>Report Details</h5>
                <div class="detail-group">
                    <div class="detail-label">Reporter:</div>
                    <div class="detail-value">
                        ${report.reporterName || 'Unknown'} ${report.reporterEmail ? `(${report.reporterEmail})` : "(No Email Provided)"}
                    </div>
                </div>
                <div class="detail-group">
                    <div class="detail-label">Reason:</div>
                    <div class="detail-value">${report.reason || 'No reason provided'}</div>
                </div>
                <div class="detail-group">
                    <div class="detail-label">Description:</div>
                    <div class="detail-value">${report.description || report.customReason || 'No description provided'}</div>
                </div>
                <div class="detail-group">
                    <div class="detail-label">Date:</div>
                    <div class="detail-value">${new Date(report.date).toLocaleString()}</div>
                </div>
                <div class="detail-group">
                    <div class="detail-label">Status:</div>
                    <div class="detail-value"><span class="badge status-${report.status?.toLowerCase()}">${report.status || 'PENDING'}</span></div>
                </div>
            </div>
        </div>
    `;

    $('#modalBody').html(modalContent);
    new bootstrap.Modal($('#postDetailModal')[0]).show();
}

function resetReportsFilters() {
    $('#reportStatusFilter').val('PENDING');
    $('#reportDateFilter').val('all');
    selectedReportStatus = 'PENDING';
    selectedReportDateRange = 'all';
    currentReportsPage = 0;
    loadReports();
}

function renderPosts(posts) {
    const grid = $('#postsGrid');
    grid.empty();

    if (!posts.length) {
        grid.append('<p class="text-center">No posts available.</p>');
        return;
    }

    posts.forEach(post => grid.append(createPostCard(post)));
}

function createPostCard(post) {
    const statusClass = `status-${post.status}`;
    const formattedPrice = formatPrice(post.price);
    const categoryName = getCategoryName(post.category);

    const actions = post.status?.toLowerCase() === 'pending' ? `
        <button class="btn btn-success btn-sm" onclick="approvePostDirect(${post.post_id})">
            <i class="bi bi-check me-1"></i>Approve
        </button>
        <button class="btn btn-danger btn-sm reject-btn" data-id="${post.post_id}">
            <i class="bi bi-x me-1"></i>Reject
        </button>
    ` : '';

    return $(`
        <div class="post-card" data-aos="flip-left" data-aos-delay="200">
            <img src="${post.images?.[0]?.image_url || '/assets/placeholder.png'}"
                 alt="${post.title}"
                 class="post-image">
            <div class="post-content">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <span class="post-category">${categoryName.replace('-', ' ').toUpperCase()}</span>
                    <span class="status-badge ${statusClass}">${post.status?.toUpperCase()}</span>
                </div>
                <h3 class="post-title">${post.title}</h3>
                <div class="post-meta">
                    <span><i class="bi bi-person me-1"></i>${post.user.name}</span>
                    <span><i class="bi bi-geo-alt me-1"></i>${post.location.address}</span>
                    <span><i class="bi bi-calendar me-1"></i>${new Date(post.createdAt).toLocaleDateString()}</span>
                </div>
                <div class="post-price">${formattedPrice}</div>
                <div class="post-actions">
                    <button class="btn btn-outline-primary btn-sm" onclick="viewPost(${post.post_id})">
                        <i class="bi bi-eye me-1"></i>View
                    </button>
                    ${actions}
                </div>
            </div>
        </div>
    `);
}

// function viewPost(postId) {
//     currentPost = filteredPosts.find(post => post.post_id === postId);
//     if (!currentPost) return;
//
//     const formattedPrice = formatPrice(currentPost.price);
//     const categoryName = getCategoryName(currentPost.category);
//     const subcategoryName = getSubcategoryName(currentPost.subcategory);
//
//     const modalContent = `
//         <div class="row">
//             <div class="col-md-6">
//                 <img src="${currentPost.images?.[0]?.image_url || '/assets/placeholder.png'}"
//                      alt="${currentPost.title}"
//                      class="img-fluid rounded">
//             </div>
//             <div class="col-md-6">
//                 ${createDetailGroup("Title", currentPost.title)}
//                 ${createDetailGroup("Price", `<div class="h4 text-success">${formattedPrice}</div>`)}
//                 ${createDetailGroup("Category", `${categoryName}${subcategoryName ? " > " + subcategoryName : ""}`)}
//                 ${createDetailGroup("Condition", currentPost.condition?.replace('-', ' '))}
//                 ${createDetailGroup("Location", `${currentPost.location.address}, ${currentPost.location.city}, ${currentPost.location.district}`)}
//                 ${createDetailGroup("Seller", `${currentPost.user.name} (${currentPost.user.email})`)}
//                 ${createDetailGroup("Posted", new Date(currentPost.createdAt).toLocaleString())}
//             </div>
//         </div>
//         <div class="mt-3">
//             ${createDetailGroup("Description", currentPost.description)}
//         </div>
//     `;
//
//     $('#modalBody').html(modalContent);
//     new bootstrap.Modal($('#postDetailModal')[0]).show();
// }

function viewPost(postId) {
    currentPost = filteredPosts.find(post => post.post_id === postId);
    if (!currentPost) return;

    const formattedPrice = formatPrice(currentPost.price);
    const categoryName = getCategoryName(currentPost.category);
    const subcategoryName = getSubcategoryName(currentPost.subcategory);

    const bankSlipLink = currentPost.payment?.payment_type === "BANK_TRANSFER" && currentPost.payment?.slip_url
        ? `<a href="${currentPost.payment.slip_url}" 
             target="_blank" 
             class="btn btn-outline-primary btn-sm ms-2">
             <i class="bi bi-receipt-cutoff me-1"></i>View Bank Slip
           </a>`
        : `<span class="text-muted">No Slip</span>`;

    const modalContent = `
        <div class="row">
            <div class="col-md-6">
                <img src="${currentPost.images?.[0]?.image_url || '/assets/placeholder.png'}" 
                     alt="${currentPost.title}" 
                     class="img-fluid rounded">
            </div>
            <div class="col-md-6">
                ${createDetailGroup("Title", currentPost.title)}
                ${createDetailGroup("Price", `<div class="h4 text-success">${formattedPrice}</div>`)}
                ${createDetailGroup("Category", `${categoryName}${subcategoryName ? " > " + subcategoryName : ""}`)}
                ${createDetailGroup("Condition", currentPost.condition?.replace('-', ' '))}
                ${createDetailGroup("Location", `${currentPost.location.address}, ${currentPost.location.city}, ${currentPost.location.district}`)}
                ${createDetailGroup("Seller", `${currentPost.user.name} (${currentPost.user.email})`)}
                ${createDetailGroup("Posted", new Date(currentPost.createdAt).toLocaleString())}
                ${createDetailGroup("Bank Slip", bankSlipLink)}
            </div>
        </div>
        <div class="mt-3">
            ${createDetailGroup("Description", currentPost.description)}
        </div>
    `;

    $('#modalBody').html(modalContent);
    new bootstrap.Modal($('#postDetailModal')[0]).show();
}

function createDetailGroup(label, value) {
    return `
        <div class="detail-group">
            <div class="detail-label">${label}</div>
            <div class="detail-value">${value || ''}</div>
        </div>
    `;
}

function renderPagination(current, total) {
    const container = $('#pagination');
    container.empty();

    for (let i = 0; i < total; i++) {
        const activeClass = i === current ? 'active' : '';
        container.append(`<li class="page-item ${activeClass}"><a class="page-link" href="#" onclick="loadPosts(${i})">${i + 1}</a></li>`);
    }
}

function resetFilters() {
    $('#statusFilter').val('PENDING');
    $('#dateFilter').val('all');

    $('.category-item').removeClass('active');
    $('.category-item[data-category="all"]').addClass('active');
    $('#selectedCategory').html('<i class="bi bi-grid me-2"></i>All Categories');

    $('#searchFilter').val('');

    selectedStatus = 'PENDING';
    selectedCategory = '';
    selectedDateRange = 'all';
    currentPage = 0;

    loadPosts();
}

function updateCategoryCounts() {

}

function loadDashboardStats() {
    loadPendingPostsCount();
    loadApprovedPostsCount();
    loadRejectedPostsCount();
    loadTotalPostsCount();
}

function logout() {
    document.cookie = "token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/";
    window.location.href = "../index.html";
}

function approvePostDirect(postId) {
    $.ajax({
        url: `http://localhost:8080/api/posts/${postId}/status/APPROVED`,
        method: 'PUT',
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            const card = $(`.post-card:has(button[onclick="approvePostDirect(${postId})"])`);
            card.find('.status-badge')
                .text('APPROVED')
                .removeClass()
                .addClass('status-badge status-approved');

            card.find('.post-actions button.btn-success, .post-actions button.btn-danger').remove();

            showToast('Post approved successfully!');
        },
        error: function (xhr) {
            console.error("Failed to approve post:", xhr);
            showToast('Failed to approve post.', 'error');
        }
    });
}

$(document).on('click', '.reject-btn', function () {
    const postId = $(this).data('id');
    rejectPostDirect(postId, this);
});

function rejectPostDirect(postId, btn) {
    $.ajax({
        url: `http://localhost:8080/api/posts/${postId}/status/REJECTED`,
        method: 'PUT',
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            const card = $(btn).closest('.post-card');
            card.find('.status-badge')
                .text('REJECTED')
                .removeClass()
                .addClass('status-badge status-rejected');

            card.find('.post-actions button.btn-success, .post-actions button.btn-danger').remove();

            showToast('Post rejected successfully!');
        },
        error: function (xhr) {
            console.error("Failed to reject post:", xhr);
            showToast('Failed to reject post.', 'error');
        }
    });
}

function showToast(message, type = 'success') {
    if ($('#toastContainer').length === 0) {
        $('body').append('<div id="toastContainer" style="position: fixed; top: 20px; right: 20px; z-index: 9999;"></div>');
    }

    const bgColor = type === 'error' ? '#dc3545' : '#28a745';
    const toast = $(`
        <div class="toast-message" style="
            background-color: ${bgColor};
            color: #fff;
            padding: 10px 15px;
            margin-top: 10px;
            border-radius: 5px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
            opacity: 0;
            transition: opacity 0.3s;
        ">${message}</div>
    `);

    $('#toastContainer').append(toast);
    setTimeout(() => toast.css('opacity', 1), 10);
    setTimeout(() => toast.css('opacity', 0), 3000);
    setTimeout(() => toast.remove(), 3500);
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

function updateReportsStats(reports) {
    const totalCount = reports.length;
    const pendingCount = reports.filter(r => r.status === 'PENDING').length;

    $('#totalReportsCount').text(totalCount);
    $('#pendingReportsCount').text(pendingCount);
}

function markAsReviewed(reportId) {
    const report = reportsList.find(r => (r.id || r.report_id) == reportId);
    if (!report) {
        showToast('Report not found', 'error');
        return;
    }

    if (!confirm('This will mark the report as "REVIEWED" and the reported post as "REPORTED". Are you sure?')) {
        return;
    }

    const postId = report.postId;

    $.ajax({
        url: `http://localhost:8080/api/reports/${reportId}/status/APPROVED`,
        method: "PUT",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            $.ajax({
                url: `http://localhost:8080/api/posts/${postId}/status/REPORTED`,
                method: "PUT",
                headers: {"Authorization": "Bearer " + getCookie("token")},
                success: function (response) {
                    reportsList = reportsList.map(r =>
                        (r.id || r.report_id) == reportId ? {...r, status: 'REVIEWED'} : r
                    );

                    const row = $(`button[onclick*="markAsReviewed(${reportId})"]`).closest('tr');
                    row.fadeOut(300, function () {
                        $(this).remove();
                        const remainingReports = reportsList.filter(r => r.status === 'PENDING');
                        updateReportsStats(remainingReports);
                        $('#reportsCountInfo').text(`Pending Reports (${remainingReports.length})`);
                    });

                    showToast('Report marked as REVIEWED and post marked as REPORTED successfully!', 'success');
                },
                error: function (xhr) {
                    console.error("Failed to update post status:", xhr);
                    if (xhr.status === 404) {
                        showToast('Post not found', 'error');
                    } else {
                        showToast('Failed to update post status. Please try again.', 'error');
                    }
                }
            });
        },
        error: function (xhr) {
            console.error("Failed to update report status:", xhr);
            if (xhr.status === 404) {
                showToast('Report not found', 'error');
            } else if (xhr.status === 400) {
                showToast('Invalid report status update', 'error');
            } else {
                showToast('Failed to mark report as reviewed. Please try again.', 'error');
            }
        }
    });
}

function markAsRejected(reportId) {
    if (!confirm('This will permanently delete the report. Are you sure?')) {
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/reports/${reportId}`,
        method: "DELETE",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function () {
            const row = $(`button[onclick*="markAsRejected(${reportId})"]`).closest('tr');
            row.fadeOut(300, function () {
                $(this).remove();
                reportsList = reportsList.filter(r => (r.id || r.report_id) != reportId);
                updateReportsStats(reportsList);
                $('#reportsCountInfo').text(`Pending Reports (${reportsList.length})`);
            });

            showToast('Report deleted successfully!', 'success');
        },
        error: function (xhr) {
            console.error("Failed to delete report:", xhr);
            if (xhr.status === 404) {
                showToast('Report not found', 'error');
            } else {
                showToast('Failed to delete report. Please try again.', 'error');
            }
        }
    });
}

function loadPendingPostsCount() {
    $.ajax({
        url: "http://localhost:8080/api/posts/page?page=0&size=1&status=PENDING",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            const count = data.totalElements || 0;
            $('#pendingCount').text(count.toLocaleString());

            updateStatTrend('pending', count);
        },
        error: function (xhr) {
            console.error("Failed to fetch pending posts count:", xhr);
            $('#pendingCount').text('0');
        }
    });
}

function loadApprovedPostsCount() {
    $.ajax({
        url: "http://localhost:8080/api/posts/page?page=0&size=1&status=APPROVED",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            const count = data.totalElements || 0;
            $('#approvedCount').text(count.toLocaleString());

            updateStatTrend('approved', count);
        },
        error: function (xhr) {
            console.error("Failed to fetch approved posts count:", xhr);
            $('#approvedCount').text('0');
        }
    });
}

function loadRejectedPostsCount() {
    $.ajax({
        url: "http://localhost:8080/api/posts/page?page=0&size=1&status=REJECTED",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            const count = data.totalElements || 0;
            $('#rejectedCount').text(count.toLocaleString());

            updateStatTrend('rejected', count);
        },
        error: function (xhr) {
            console.error("Failed to fetch rejected posts count:", xhr);
            $('#rejectedCount').text('0');
        }
    });
}

function loadTotalPostsCount() {
    $.ajax({
        url: "http://localhost:8080/api/posts/page?page=0&size=1",
        method: "GET",
        headers: {"Authorization": "Bearer " + getCookie("token")},
        success: function (data) {
            const count = data.totalElements || 0;
            $('#totalCount').text(count.toLocaleString());

            updateStatTrend('total', count);
        },
        error: function (xhr) {
            console.error("Failed to fetch total posts count:", xhr);
            $('#totalCount').text('0');
        }
    });
}

function updateStatTrend(type, currentCount) {

    const trends = {
        pending: {icon: 'bi-arrow-down', text: '-5% from last week', class: 'text-success'},
        approved: {icon: 'bi-arrow-up', text: '+8% from last month', class: 'text-success'},
        rejected: {icon: 'bi-arrow-up', text: '+3 new today', class: 'text-warning'},
        total: {icon: 'bi-arrow-up', text: '+12% from last month', class: 'text-success'}
    };

    if (trends[type]) {
        const trendElement = $(`#${type}Count`).siblings('.stat-trend').find('small');
        if (trendElement.length) {
            const trend = trends[type];
            trendElement.html(`<i class="${trend.icon} ${trend.class}"></i> ${trend.text}`);
        }
    }
}

function refreshDashboardStats() {
    loadDashboardStats();
}

function loadDashboardStatsOptimized() {
    const requests = [
        $.ajax({
            url: "http://localhost:8080/api/posts/page?page=0&size=1&status=PENDING",
            method: "GET",
            headers: {"Authorization": "Bearer " + getCookie("token")}
        }),
        $.ajax({
            url: "http://localhost:8080/api/posts/page?page=0&size=1&status=APPROVED",
            method: "GET",
            headers: {"Authorization": "Bearer " + getCookie("token")}
        }),
        $.ajax({
            url: "http://localhost:8080/api/posts/page?page=0&size=1&status=REJECTED",
            method: "GET",
            headers: {"Authorization": "Bearer " + getCookie("token")}
        }),
        $.ajax({
            url: "http://localhost:8080/api/posts/page?page=0&size=1",
            method: "GET",
            headers: {"Authorization": "Bearer " + getCookie("token")}
        })
    ];

    Promise.all(requests)
        .then(results => {
            const pendingCount = results[0].totalElements || 0;
            $('#pendingCount').text(pendingCount.toLocaleString());

            const approvedCount = results[1].totalElements || 0;
            $('#approvedCount').text(approvedCount.toLocaleString());

            const rejectedCount = results[2].totalElements || 0;
            $('#rejectedCount').text(rejectedCount.toLocaleString());

            const totalCount = results[3].totalElements || 0;
            $('#totalCount').text(totalCount.toLocaleString());

            updateStatTrend('pending', pendingCount);
            updateStatTrend('approved', approvedCount);
            updateStatTrend('rejected', rejectedCount);
            updateStatTrend('total', totalCount);

            console.log('Dashboard statistics loaded successfully');
        })
        .catch(error => {
            console.error('Failed to load dashboard statistics:', error);
            $('#pendingCount, #approvedCount, #rejectedCount, #totalCount').text('0');
        });
}