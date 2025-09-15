let filteredPosts = [];
let currentPost = null;
let currentPage = 0;
const pageSize = 10;
let selectedStatus = 'PENDING';
let selectedCategory = '';
let selectedDateRange = 'all';

$(document).ready(function () {
    loadPosts();

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

    // Create query string
    const queryString = new URLSearchParams(params).toString();

    $.ajax({
        url: `http://localhost:8080/api/posts/page?${queryString}`, // <-- query string appended here
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
        <div class="post-card">
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

function viewPost(postId) {
    currentPost = filteredPosts.find(post => post.post_id === postId);
    if (!currentPost) return;

    const formattedPrice = formatPrice(currentPost.price);
    const categoryName = getCategoryName(currentPost.category);
    const subcategoryName = getSubcategoryName(currentPost.subcategory);

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
    // This function would update the counts in the category dropdown
    // You can implement this based on your API response
    // For now, we'll keep the placeholder counts
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