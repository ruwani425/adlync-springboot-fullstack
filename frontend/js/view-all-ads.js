// Enhanced view-all-ads.js with backend filter integration

const $ = window.$
const bootstrap = window.bootstrap

const API_BASE = "http://localhost:8080/api/posts/approved/all"
const ADVANCED_FILTER_API = "http://localhost:8080/api/posts/page/advanced" // New advanced filter endpoint
const pageSize = 9
let currentPage = 0
let totalPages = 0
let postsCache = []
let categoryFromURL = null
let isFiltering = false // Track if we're using filters

// Function to get URL parameters
function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// Initialize from URL
function initializeFromURL() {
    const categoryParam = getUrlParameter('category');
    if (categoryParam) {
        categoryFromURL = categoryParam;

        const displayCategoryMap = {
            'VEHICLE': 'vehicles',
            'ANIMAL': 'animals',
            'ELECTRONIC': 'electronics',
            'PROPERTY': 'properties',
            'JOB': 'jobs',
            'SERVICES': 'services',
            'SPORT': 'sports',
            'AGRICULTURE': 'agriculture',
            'KIDS': 'kids',
            'FASHION_AND_BEAUTY': 'fashion',
            'ENTERTAINTMENT': 'entertainment',
            'EDUCATION': 'education',
            'MOBILE': 'mobile',
            'WORK_OVERSEAS': 'overseas',
            'HOME_AND_GARDEN': 'home',
            'ESSENTIALS': 'essentials'
        };

        const displayValue = displayCategoryMap[categoryParam];
        if (displayValue) {
            $("#categoryFilter").val(displayValue);
            updatePageHeader(displayValue);
        }
    }
}

function updatePageHeader(category) {
    const categoryDisplayName = category.charAt(0).toUpperCase() + category.slice(1);
    $(".breadcrumb-item.active").text(`${categoryDisplayName} Ads`);
    $("h1.h3").text(`${categoryDisplayName} Advertisements`);
    $("h1.h3").next("p").text(`Find the best ${category} deals from verified sellers`);
}

$(document).ready(() => {
    if (window.profileImageManager) {
        window.profileImageManager.init();
    }

    checkAuth()
    initializeFilters()
    initializeViewToggle()
    initializeSorting()
    initializeFromURL()
    loadPosts(0)
})

// Main function to load posts with or without filters
function loadPosts(page = 0) {
    if (page < 0) page = 0
    currentPage = page

    // Check if we have any active filters
    const hasActiveFilters = checkForActiveFilters()

    if (hasActiveFilters || categoryFromURL) {
        loadPostsWithFilters(page)
    } else {
        loadPostsDefault(page)
    }
}

// Check for active filters
function checkForActiveFilters() {
    const search = $("#searchInput").val().trim()
    const category = $("#categoryFilter").val()
    const location = $("#locationFilter").val()
    const minPrice = $("#minPrice").val()
    const maxPrice = $("#maxPrice").val()
    const conditions = getSelectedConditions()

    return !!(search || category || location || minPrice || maxPrice || conditions.length > 0)
}

// Get selected condition filters
function getSelectedConditions() {
    const conditions = []
    $('input[type="checkbox"]:checked').each(function() {
        conditions.push($(this).val())
    })
    return conditions
}

// Load posts with advanced filtering
function loadPostsWithFilters(page) {
    isFiltering = true

    const params = new URLSearchParams({
        page: page.toString(),
        size: pageSize.toString(),
        status: 'APPROVED'
    })

    // Add search parameter
    const search = $("#searchInput").val().trim()
    if (search) {
        params.append('search', search)
    }

    // Add category parameter
    const category = $("#categoryFilter").val() || (categoryFromURL ? getBackendCategoryEnum(categoryFromURL) : null)
    if (category) {
        const backendCategory = getFrontendToBackendCategory(category)
        if (backendCategory) {
            params.append('category', backendCategory)
        }
    }

    // Add location parameter
    const location = $("#locationFilter").val()
    if (location) {
        params.append('location', location)
    }

    // Add price parameters
    const minPrice = $("#minPrice").val()
    const maxPrice = $("#maxPrice").val()
    if (minPrice) {
        params.append('minPrice', minPrice)
    }
    if (maxPrice) {
        params.append('maxPrice', maxPrice)
    }

    // Add condition parameter (take first selected condition for backend)
    const conditions = getSelectedConditions()
    if (conditions.length > 0) {
        params.append('condition', conditions[0]) // Backend handles one condition
    }

    // Add sorting parameter
    const sortBy = $("#sortBy").val()
    if (sortBy) {
        params.append('sortBy', sortBy)
    }

    const url = `${ADVANCED_FILTER_API}?${params.toString()}`
    console.log('Loading filtered posts:', url)

    $.ajax({
        url,
        method: "GET",
        success: handlePostsSuccess,
        error: handlePostsError
    })
}

// Load posts without filters (default)
function loadPostsDefault(page) {
    isFiltering = false
    const url = `${API_BASE}?page=${page}&size=${pageSize}`

    console.log('Loading default posts:', url)

    $.ajax({
        url,
        method: "GET",
        success: handlePostsSuccess,
        error: handlePostsError
    })
}

// Handle successful posts response
function handlePostsSuccess(data) {
    postsCache = data.content || []
    totalPages = data.totalPages || Math.ceil((data.totalElements || 0) / pageSize) || 1

    renderPosts(postsCache)
    renderPagination(data.pageNumber !== undefined ? data.pageNumber : currentPage, totalPages)

    $("#resultCount").text(data.totalElements ? data.totalElements.toLocaleString() : "0")
    $("#searchTime").text((Math.random() * 0.5 + 0.1).toFixed(2))

    console.log(`Loaded ${postsCache.length} posts, total: ${data.totalElements}`)
}

// Handle posts loading error
function handlePostsError(xhr) {
    console.error("Failed to load posts:", xhr)
    $("#adsContainer").html("<p class='text-danger'>Failed to load advertisements. Please try again.</p>")
    $("#resultCount").text("0")
    renderPagination(0, 1)
}

// Category mapping functions
function getFrontendToBackendCategory(frontendCategory) {
    const categoryMap = {
        'vehicles': 'VEHICLE',
        'animals': 'ANIMAL',
        'electronics': 'ELECTRONIC',
        'properties': 'PROPERTY',
        'jobs': 'JOB',
        'services': 'SERVICES',
        'sports': 'SPORT',
        'agriculture': 'AGRICULTURE',
        'kids': 'KIDS',
        'fashion': 'FASHION_AND_BEAUTY',
        'entertainment': 'ENTERTAINTMENT',
        'education': 'EDUCATION',
        'mobile': 'MOBILE',
        'overseas': 'WORK_OVERSEAS',
        'home': 'HOME_AND_GARDEN',
        'essentials': 'ESSENTIALS'
    }
    return categoryMap[frontendCategory.toLowerCase()]
}

function getBackendCategoryEnum(backendEnum) {
    const displayCategoryMap = {
        'VEHICLE': 'vehicles',
        'ANIMAL': 'animals',
        'ELECTRONIC': 'electronics',
        'PROPERTY': 'properties',
        'JOB': 'jobs',
        'SERVICES': 'services',
        'SPORT': 'sports',
        'AGRICULTURE': 'agriculture',
        'KIDS': 'kids',
        'FASHION_AND_BEAUTY': 'fashion',
        'ENTERTAINTMENT': 'entertainment',
        'EDUCATION': 'education',
        'MOBILE': 'mobile',
        'WORK_OVERSEAS': 'overseas',
        'HOME_AND_GARDEN': 'home',
        'ESSENTIALS': 'essentials'
    }
    return displayCategoryMap[backendEnum]
}

// Initialize filters with proper event handlers
function initializeFilters() {
    // Apply filters button
    $("#applyFilters").on("click", function(e) {
        e.preventDefault()
        applyFilters()
    })

    // Clear filters button
    $("#clearFilters").on("click", function(e) {
        e.preventDefault()
        clearAllFilters()
    })

    // Search input with debounce
    $("#searchInput").on("input", debounce(applyFilters, 500))

    // Dropdown filters
    $("#categoryFilter, #locationFilter, #sortBy").on("change", applyFilters)

    // Price inputs with debounce
    $("#minPrice, #maxPrice").on("input", debounce(applyFilters, 600))

    // Condition checkboxes
    $('input[type="checkbox"]').on("change", applyFilters)
}

// Apply filters function
function applyFilters() {
    console.log('Applying filters...')
    loadPosts(0) // Reset to first page when applying filters
}

// Clear all filters
function clearAllFilters() {
    $("#searchInput").val("")
    $("#categoryFilter").val("")
    $("#locationFilter").val("")
    $("#minPrice").val("")
    $("#maxPrice").val("")
    $("#sortBy").val("newest")
    $('input[type="checkbox"]').prop("checked", false)

    // Clear URL category
    clearCategoryFromURL()

    // Reset page header
    $(".breadcrumb-item.active").text("All Ads")
    $("h1.h3").text("All Advertisements")
    $("h1.h3").next("p").text("Discover amazing deals from verified sellers")

    // Load default posts
    loadPosts(0)
}

function clearCategoryFromURL() {
    const url = new URL(window.location);
    url.searchParams.delete('category');
    window.history.replaceState(null, '', url);
    categoryFromURL = null;
}

// Enhanced post rendering
function renderPosts(posts) {
    const $container = $("#adsContainer")
    $container.empty()

    if (!posts || posts.length === 0) {
        $container.html(`
            <div class="col-12">
                <div class="text-center py-5">
                    <i class="bi bi-search fs-1 text-muted mb-3"></i>
                    <h5 class="text-muted">No advertisements found</h5>
                    <p class="text-muted">Try adjusting your filters or search terms</p>
                </div>
            </div>
        `)
        $("#resultCount").text("0")
        return
    }

    posts.forEach((post) => {
        const postId = post.post_id || post.id
        const title = escapeHtml(post.title || "No title")
        const price = Number(post.price || 0)
        const categoryName = getCategoryName(post)
        const locationName = getLocationName(post)
        const seller = getUserName(post)
        const rating = post.rating || "4.5"
        const condition = getPostCondition(post)
        const imageUrl = getPostImage(post)

        const adCard = createAdCard({
            postId, title, price, categoryName, locationName,
            seller, rating, condition, imageUrl
        })

        $container.append(adCard)
    })
}

// Helper functions for extracting post data
function getCategoryName(post) {
    if (post.category && post.category.name) return post.category.name
    if (post.category) return post.category
    return "unknown"
}

function getLocationName(post) {
    if (post.location) {
        return post.location.city || post.location.address || post.location.district || "Unknown"
    }
    return "Unknown"
}

function getUserName(post) {
    if (post.user) {
        return post.user.name || post.user.username || "Unknown"
    }
    return "Unknown"
}

function getPostCondition(post) {
    if (!post.common) return ""

    const entities = [
        post.common.vehicle, post.common.electronic, post.common.mobile,
        post.common.agriculture, post.common.entertainment, post.common.essentials,
        post.common.fashion_and_beauty, post.common.home_and_garden,
        post.common.kids, post.common.sport
    ]

    for (const entity of entities) {
        if (entity && entity.condition) {
            return entity.condition
        }
    }
    return ""
}

function getPostImage(post) {
    if (Array.isArray(post.images) && post.images.length > 0) {
        const first = post.images[0]
        return typeof first === "string" ? first : first.image_url || first.url
    }
    return "https://picsum.photos/seed/default/400/250"
}

// Create ad card HTML
function createAdCard({ postId, title, price, categoryName, locationName, seller, rating, condition, imageUrl }) {
    const conditionDisplay = condition ? `
        <div class="small text-muted mb-2">
            <i class="bi bi-check-circle me-1"></i>Condition: ${escapeHtml(capitalize(condition))}
        </div>
    ` : ''

    return `
        <div class="col-md-6 col-xl-4 ad-item" data-aos="flip-left" data-aos-delay="200"
             data-post-id="${postId}"
             data-category="${categoryName.toLowerCase()}" 
             data-location="${locationName.toLowerCase()}" 
             data-price="${price}"
             data-condition="${condition.toLowerCase()}">
            <div class="card ad-card h-100 overflow-hidden">
                <div class="position-relative">
                    <img alt="${title}" class="w-100 object-cover" 
                         src="${imageUrl}" 
                         style="height:200px; object-fit:cover;" 
                         onerror="this.src='https://picsum.photos/seed/default/400/250'" />
                    <span class="badge category-badge position-absolute top-0 start-0 m-2">
                        ${escapeHtml(capitalize(categoryName))}
                    </span>
                    <button class="btn btn-sm btn-light position-absolute top-0 end-0 m-2" title="Add to Favorites">
                        <i class="bi bi-heart"></i>
                    </button>
                </div>
                <div class="card-body d-flex flex-column">
                    <h6 class="card-title mb-2">${title}</h6>
                    <div class="price-badge badge fs-6 mb-2">Rs. ${price.toLocaleString()}</div>
                    <div class="d-flex justify-content-between text-muted small mb-2">
                        <span><i class="bi bi-geo-alt me-1"></i>${escapeHtml(locationName)}</span>
                        <span><i class="bi bi-star-fill text-warning me-1"></i>${rating}</span>
                    </div>
                    ${conditionDisplay}
                    <div class="mt-auto d-flex justify-content-between align-items-center">
                        <small class="text-muted">by ${escapeHtml(seller)}</small>
                        <a class="btn btn-emerald btn-sm view-details-btn" 
                           href="advertisement-details.html?id=${postId}">
                            View Details
                        </a>
                    </div>
                </div>
            </div>
        </div>
    `
}

// Enhanced pagination
function renderPagination(current, total) {
    const $pagination = $(".pagination")
    $pagination.empty()

    if (total <= 0) total = 1
    if (current < 0) current = 0
    if (current >= total) current = total - 1

    // Previous button
    const prevDisabled = current <= 0 ? "disabled" : ""
    const prevPage = Math.max(0, current - 1)
    $pagination.append(`
        <li class="page-item ${prevDisabled}">
            <a class="page-link" href="#" data-page="${prevPage}" tabindex="-1">
                <i class="bi bi-chevron-left"></i> Previous
            </a>
        </li>
    `)

    // Page numbers
    const maxButtons = 5
    let start = Math.max(0, current - Math.floor(maxButtons / 2))
    let end = Math.min(total - 1, start + maxButtons - 1)
    if (end - start < maxButtons - 1) {
        start = Math.max(0, end - maxButtons + 1)
    }

    if (start > 0) {
        $pagination.append(`<li class="page-item"><a class="page-link" href="#" data-page="0">1</a></li>`)
        if (start > 1) {
            $pagination.append(`<li class="page-item disabled"><span class="page-link">...</span></li>`)
        }
    }

    for (let i = start; i <= end; i++) {
        const active = i === current ? "active" : ""
        $pagination.append(`
            <li class="page-item ${active}">
                <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
            </li>
        `)
    }

    if (end < total - 1) {
        if (end < total - 2) {
            $pagination.append(`<li class="page-item disabled"><span class="page-link">...</span></li>`)
        }
        $pagination.append(`
            <li class="page-item">
                <a class="page-link" href="#" data-page="${total - 1}">${total}</a>
            </li>
        `)
    }

    // Next button
    const nextDisabled = current >= total - 1 ? "disabled" : ""
    const nextPage = Math.min(total - 1, current + 1)
    $pagination.append(`
        <li class="page-item ${nextDisabled}">
            <a class="page-link" href="#" data-page="${nextPage}">
                Next <i class="bi bi-chevron-right"></i>
            </a>
        </li>
    `)

    // Attach click handlers
    $pagination.find("a.page-link[data-page]").on("click", function (e) {
        e.preventDefault()
        const page = parseInt($(this).data("page"))
        if (!isNaN(page) && page >= 0 && page < total && page !== current) {
            loadPosts(page)
        }
    })
}

// View toggle functionality
function initializeViewToggle() {
    $("#gridView").on("click", function () {
        $(this).addClass("active")
        $("#listView").removeClass("active")
        $("#adsContainer").removeClass("list-view").addClass("row g-4")
        $(".ad-item").removeClass("col-12").addClass("col-md-6 col-xl-4")
    })

    $("#listView").on("click", function () {
        $(this).addClass("active")
        $("#gridView").removeClass("active")
        $("#adsContainer").removeClass("row g-4").addClass("list-view")
        $(".ad-item").removeClass("col-md-6 col-xl-4").addClass("col-12")
    })
}

function initializeSorting() {
    $("#sortBy").on("change", applyFilters)
}

// Favorites functionality
$(document).on("click", '.btn[title="Add to Favorites"]', function (e) {
    e.preventDefault()
    const $btn = $(this)
    const $icon = $btn.find("i")

    if ($icon.hasClass("bi-heart")) {
        $icon.removeClass("bi-heart").addClass("bi-heart-fill text-danger")
        $btn.attr("title", "Remove from Favorites")
        showToast("Added to favorites!", "success")
    } else {
        $icon.removeClass("bi-heart-fill text-danger").addClass("bi-heart")
        $btn.attr("title", "Add to Favorites")
        showToast("Removed from favorites!", "info")
    }
})

// Keep existing helper functions
function checkAuth() {
    const token = getCookie("token")
    if (!token) {
        if (window.profileImageManager) {
            window.profileImageManager.hideProfileElements();
        } else {
            $("#signInBtn").show().removeClass("btn-danger").addClass("btn-outline-secondary")
                .off("click").on("click", () => location.href = "signin.html")
            $("#profileDropdown").hide()
            $("#postAdBtn").off("click").on("click", () => location.href = "signup.html")
        }
        return;
    }
    $("#postAdBtn").off("click").on("click", () => location.href = "postad.html")
}

function getCookie(name) {
    const value = `; ${document.cookie}`
    const parts = value.split(`; ${name}=`)
    if (parts.length === 2) return parts.pop().split(";").shift()
    return null
}

function showToast(message, type = "info") {
    const toast = $(`
        <div class="toast align-items-center text-white bg-${type === "success" ? "success" : "info"} border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `)

    if (!$("#toastContainer").length) {
        $("body").append('<div id="toastContainer" class="toast-container position-fixed top-0 end-0 p-3"></div>')
    }

    $("#toastContainer").append(toast)
    const bsToast = new bootstrap.Toast(toast[0])
    bsToast.show()

    toast.on("hidden.bs.toast", function () {
        $(this).remove()
    })
}

function debounce(func, wait) {
    let timeout
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout)
            func(...args)
        }
        clearTimeout(timeout)
        timeout = setTimeout(later, wait)
    }
}

function capitalize(s) {
    if (!s) return ""
    return s.charAt(0).toUpperCase() + s.slice(1)
}

function escapeHtml(unsafe) {
    if (unsafe === null || unsafe === undefined) return ""
    return String(unsafe)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;")
}

$(document).on("click", ".view-details-btn", function (e) {
    e.preventDefault()
    const $card = $(this).closest(".ad-item")
    const categoryValue = $card.find(".category-badge").text().trim().toLowerCase()
    const postId = $card.data("post-id") || ""
    window.location.href = `../pages/ad-details.html?categoryName=${categoryValue}&postId=${postId}`
})