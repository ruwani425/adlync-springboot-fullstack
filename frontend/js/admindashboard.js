

$(document).ready(function () {
    const $navLinks = $('.nav-link');
    const $contentSections = $('.content-section');
    const $pageTitle = $('#pageTitle');
    const $sidebarToggle = $('#sidebarToggle');
    const $sidebar = $('#sidebar');
    const $mainContent = $('.main-content');

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
});

// Global variables
let currentPostCard = null;
let currentImageIndex = 0;
let postImages = [];

function showPostDetail(postCard) {
    console.log("[v0] showPostDetail called");
    currentPostCard = postCard;

    try {
        const $postCard = $(postCard);

        const title = $postCard.find('h6').text();
        const userAvatar = $postCard.find('.avatar').attr('src');
        const userName = $postCard.find('.text-muted').text().split('•')[0].trim();
        const postTime = $postCard.find('.text-muted').text().split('•')[1].trim();
        const price = $postCard.find('.text-success').text();
        const category = $postCard.find('.badge').text();
        const status = $postCard.find('.status-badge').text();
        const description = $postCard.find('p').text();
        const postStatus = $postCard.data('status');

        const paymentMethod = $postCard.data('payment-method') || 'card';
        const paymentSlip = $postCard.data('payment-slip') || '';
        const $paymentMethodDiv = $('#modalPaymentMethod');

        if (paymentMethod === 'slip' && paymentSlip) {
            $paymentMethodDiv.html(`
                <i class="bi bi-receipt me-2 text-primary"></i>
                <a href="${paymentSlip}" target="_blank" class="text-decoration-none">
                    <i class="bi bi-eye me-1"></i>View Payment Slip
                </a>
            `);
        } else {
            $paymentMethodDiv.html(`
                <i class="bi bi-credit-card me-2 text-success"></i>
                <span class="text-muted">Card Payment</span>
            `);
        }

        setupImageGallery();

        const $actionButtons = $('#modalActionButtons');
        if (postStatus === 'pending') {
            $actionButtons.html(`
                <button class="btn btn-success" onclick="approvePostFromModal()">
                    <i class="bi bi-check me-1"></i>Approve
                </button>
                <button class="btn btn-danger" onclick="rejectPostFromModal()">
                    <i class="bi bi-x me-1"></i>Reject
                </button>
            `);
        } else if (postStatus === 'approved') {
            $actionButtons.html(`
                <button class="btn btn-secondary" disabled>
                    <i class="bi bi-check me-1"></i>Already Approved
                </button>
            `);
        }

        const $modal = $('#postDetailModal');
        $('body').css('overflow', 'hidden');
        $modal.css('display', 'flex');
        console.log("[v0] Modal displayed");

    } catch (error) {
        console.error("[v0] Error showing post detail:", error);
    }
}

function closePostDetail() {
    $('#postDetailModal').css('display', 'none');
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
