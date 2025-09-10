let postList = [];
let selectedStatus = 'PENDING';

$(document).ready(function () {
    const $navLinks = $('.nav-link');
    const $contentSections = $('.content-section');
    const $pageTitle = $('#pageTitle');
    const $sidebarToggle = $('#sidebarToggle');
    const $sidebar = $('#sidebar');
    const $mainContent = $('.main-content');

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
        if ($section.length) {
            $section.addClass('active');
        }

        const sectionName = $(this).text().trim();
        $pageTitle.text(sectionName);
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

        currentFilters.category = $(this).data('category');
        applyAllFilters();
    });

    $statusFilter.on('change', function () {
        currentFilters.status = $(this).val();
        applyAllFilters();
    });

    $searchPosts.on('input', function () {
        currentFilters.search = $(this).val().toLowerCase();
        applyAllFilters();
    });
    loadPosts(0);
});

let currentPostCard = null;
let postImages = [];
let currentImageIndex = 0;

function showPostDetail(postData) {
    try {
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

        postImages = (postData.images || []).map(img => img.image_url);
        currentImageIndex = 0;
        setupImageGallery();

        currentPostCard = postData;

        const $actionDiv = $("#modalActionButtons").empty();

        if (postData.status === 'PENDING') {
            const approveBtn = $('<button class="btn btn-success">Approve</button>');
            const rejectBtn = $('<button class="btn btn-danger">Reject</button>');

            approveBtn.on('click', approvePostFromModal);
            rejectBtn.on('click', rejectPostFromModal);

            $actionDiv.append(approveBtn, rejectBtn);
        } else if (postData.status === 'APPROVED') {
            const label = $('<span class="text-success fw-bold">Already Approved</span>');
            $actionDiv.append(label);
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
    if (currentPostCard) {
        const $postCard = $(currentPostCard);
        $postCard.data('status', 'approved');
        $postCard.find('.status-badge').text('Approved').removeClass().addClass('status-badge status-approved');

        showToast('Post approved successfully!');
        closePostDetail();

        applyAllFilters();
    }
}

function rejectPostFromModal() {
    if (currentPostCard) {
        if (confirm('Are you sure you want to reject this post?')) {
            $(currentPostCard).remove();
            showToast('Post rejected successfully!');
            closePostDetail();
            applyAllFilters();
        }
    }
}

function showToast(message) {
    $('.toast-notification').remove();

    const $toast = $(`
        <div class="toast-notification position-fixed top-0 end-0 m-3 alert alert-success" style="z-index: 9999;">
            <i class="bi bi-check-circle me-2"></i>${message}
            <button type="button" class="btn-close ms-2"></button>
        </div>
    `);

    $toast.find('.btn-close').on('click', function () {
        $toast.remove();
    });

    $('body').append($toast);

    setTimeout(() => {
        $toast.remove();
    }, 3000);
}

function applyAllFilters() {
    const $posts = $('.post-card');
    let visibleCount = 0;

    $posts.each(function () {
        const $post = $(this);
        let shouldShow = true;

        const postStatus = $post.data('status');
        if (postStatus !== 'pending' && postStatus !== 'approved') {
            shouldShow = false;
        }

        if (shouldShow) {
            $post.show();
            visibleCount++;
        } else {
            $post.hide();
        }
    });

    $('#postsCount').text(visibleCount.toLocaleString());
}

function setupImageGallery() {
    const $container = $('#imageGalleryContainer');
    const $indicators = $('#imageIndicators');

    $container.empty();
    $indicators.empty();

    if (postImages.length === 0) {
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
        const $img = $(`<img src="${src}" alt="Image ${index + 1}" class="post-detail-image ${index === 0 ? 'active' : ''}">`);
        $container.append($img);
    });

    if (postImages.length > 1) {
        postImages.forEach((_, index) => {
            const $indicator = $(`<div class="image-indicator ${index === 0 ? 'active' : ''}"></div>`);
            $indicator.on('click', () => goToImage(index));
            $indicators.append($indicator);
        });
    }
}

function nextImage() {
    if (postImages.length <= 1) return;
    currentImageIndex = (currentImageIndex + 1) % postImages.length;
    updateImageDisplay();
}

function previousImage() {
    if (postImages.length <= 1) return;
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

    $images.each(function (i) {
        $(this).toggleClass('active', i === currentImageIndex);
    });

    $indicators.each(function (i) {
        $(this).toggleClass('active', i === currentImageIndex);
    });
}

function showSection(sectionName) {
    $(`.nav-link[data-section="${sectionName}"]`).click();
}

const pageSize = 5;
let currentPage = 0;

function loadPosts(page = 0) {
    $.ajax({
        url: `http://localhost:8080/api/posts/page?page=${page}&size=${pageSize}&status=${selectedStatus}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + getCookie("token")
        },
        success: function (data) {
            postList = data.content || [];
            renderPosts(postList);
            renderPagination(data.pageNumber, data.totalPages);
        },
        error: function (xhr) {
            console.error("Failed to load posts:", xhr);
        }
    });
}

function renderPosts(posts) {
    const $container = $("#postsContainer");
    $container.empty();

    if (posts.length === 0) {
        $container.html("<p class='text-center text-muted'>No posts found</p>");
        return;
    }

    posts.forEach(post => {
        const createdTime = post.createdAt
            ? new Date(post.createdAt).toLocaleString()
            : "Unknown time";

        const mainImage = post.images && post.images.length > 0 ? post.images[0] : "https://via.placeholder.com/120x80";
        console.log(mainImage.image_url)
        console.log(post.user.name)
        console.log(post.user.email)
        const otherImages = (post.images || [])
            .slice(1)
            .map(img => `<img alt="Image" src="${img}" />`)
            .join("");

        const card = `
            <div class="post-card card mb-3" 
                 data-category="${post.category ? post.category.name : "Unknown"}"
                 data-payment-method="${post.paymentMethod || ""}"
                 data-payment-slip="${post.paymentSlip || ""}"
                 data-status="${post.status || "unknown"}"
                 data-post='${JSON.stringify(post)}'>
                <div class="card-body">
                    <div class="row align-items-center">
                        <!-- Image Column -->
                        <div class="col-md-2 col-3">
                            <img alt="Post Image" class="img-fluid rounded"
                                 src="${mainImage.image_url}">
                            <div class="post-images" style="display: none;">
                                ${otherImages}
                            </div>
                        </div>

                        <div class="col-md-7 col-6">
                            <h6 class="mb-1">${post.title || "No title"}</h6>
                            <div class="d-flex align-items-center mb-1">
                                <img alt="User"
                                     class="avatar me-2 rounded-circle"
                                     src="${post.userAvatar || "https://via.placeholder.com/24"}">
                                <span class="text-muted">${post.user.name || "Unknown"} • ${createdTime}</span>
                            </div>
                            <p class="text-muted mb-2" style="font-size: 0.8rem; line-height: 1.3;">
                                ${post.description || ""}
                            </p>
                            <div class="d-flex gap-2">
                                <span class="badge bg-primary">${post.category ? post.category.name : "No Category"}</span>
                                <span class="status-badge status-${(post.status || "unknown").toLowerCase()}">
                                    ${post.status || "Unknown"}
                                </span>
                            </div>
                        </div>

                        <div class="col-md-3 col-3 text-end">
                            <h6 class="text-success mb-2">${post.price ? "Rs. " + post.price.toLocaleString() : "No Price"}</h6>
                                <button class="btn btn-primary"
                                        onclick="event.stopPropagation(); showPostDetailFromCard(this)"
                                        title="View Details">
                                    <i class="bi bi-eye"></i>
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

function showPostDetailFromCard(button) {
    const $card = $(button).closest('.post-card');
    const postData = JSON.parse($card.attr('data-post'));
    showPostDetail(postData);
}


function renderPagination(current, totalPages) {
    const $pagination = $(".pagination");
    $pagination.empty();

    const prevDisabled = current === 0 ? "disabled" : "";
    $pagination.append(`
        <li class="page-item ${prevDisabled}">
            <a class="page-link" href="#" onclick="changePage(${current - 1})">Previous</a>
        </li>
    `);

    for (let i = 0; i < totalPages; i++) {
        const active = i === current ? "active" : "";
        $pagination.append(`
            <li class="page-item ${active}">
                <a class="page-link" href="#" onclick="changePage(${i})">${i + 1}</a>
            </li>
        `);
    }

    const nextDisabled = current === totalPages - 1 ? "disabled" : "";
    $pagination.append(`
        <li class="page-item ${nextDisabled}">
            <a class="page-link" href="#" onclick="changePage(${current + 1})">Next</a>
        </li>
    `);
}

function changePage(page) {
    if (page < 0) return;
    loadPosts(page);
}
