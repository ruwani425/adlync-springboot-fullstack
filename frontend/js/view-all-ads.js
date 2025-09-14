const $ = window.$
const bootstrap = window.bootstrap

const API_BASE = "http://localhost:8080/api/posts/approved/all"
const pageSize = 9
let currentPage = 0
let totalPages = 0
let postsCache = []
const selectedStatus = "APPROVED"

$(document).ready(() => {
    initializeFilters()
    initializeViewToggle()
    initializeSorting()
    checkAuth()

    loadPosts(0)
})

function loadPosts(page = 0) {
    if (page < 0) {
        page = 0
    }

    currentPage = page
    const url = `${API_BASE}?page=${page}&size=${pageSize}`

    $.ajax({
        url,
        method: "GET",
        success: (data) => {
            postsCache = data.content || []
            console.log(data.totalPages + " " + data.totalElements)
            console.log(data)
            totalPages = data.totalPages || Math.ceil((data.totalElements || 0) / pageSize) || 1
            renderPosts(postsCache)
            renderPagination(data.pageNumber !== undefined ? data.pageNumber : page, totalPages)
            applyFilters()
        },
        error: (xhr) => {
            console.error("Failed to load posts:", xhr)
            $("#adsContainer").html("<p class='text-danger'>Failed to load advertisements.</p>")
            $("#resultCount").text("0")
            renderPagination(0, 1)
        },
    })
}

function renderPosts(posts) {
    const $container = $("#adsContainer")
    $container.empty()

    if (!posts || posts.length === 0) {
        $container.html("<p class='text-muted'>No advertisements found.</p>")
        $("#resultCount").text("0")
        return
    }

    posts.forEach((post) => {
        const postId = post.post_id || post.id || postIdFrom(post)
        const title = escapeHtml(post.title || "No title")
        const price = Number(post.price || 0)
        const categoryName = (post.category && (post.category.name || post.category)) || post.category_name || "unknown"
        const locationName = (post.location && (post.location.address || post.location)) || post.locationName || "Unknown"
        const seller = (post.user && (post.user.name || post.user.username)) || post.seller || "Unknown"
        const rating = post.rating ? post.rating : post.ratingValue || "N/A"
        let imageUrl = "https://picsum.photos/seed/default/400/250"
        if (Array.isArray(post.images) && post.images.length > 0) {
            const first = post.images[0]
            imageUrl = typeof first === "string" ? first : first.image_url || first.url || imageUrl
        } else if (post.imageUrl) {
            imageUrl = post.imageUrl
        } else if (post.images && post.images.image_url) {
            imageUrl = post.images.image_url
        }

        const dataCategory = String(categoryName).toLowerCase()
        const dataLocation = String(locationName).toLowerCase()
        const dataPrice = Number.isFinite(price) ? price : 0

        const adCard = `
      <div class="col-md-6 col-xl-4 ad-item" 
           data-post-id="${postId}"
           data-category="${dataCategory}" 
           data-location="${dataLocation}" 
           data-price="${dataPrice}">
        <div class="card ad-card h-100 overflow-hidden">
          <div class="position-relative">
            <img alt="${title}" class="w-100 object-cover" src="${imageUrl}" style="height:200px; object-fit:cover;" />
            <span class="badge category-badge position-absolute top-0 start-0 m-2">${escapeHtml(capitalize(categoryName))}</span>
            <button class="btn btn-sm btn-light position-absolute top-0 end-0 m-2" title="Add to Favorites">
              <i class="bi bi-heart"></i>
            </button>
          </div>
          <div class="card-body d-flex flex-column">
            <h6 class="card-title">${title}</h6>
            <div class="price-badge badge fs-6 mb-2">Rs. ${Number(dataPrice).toLocaleString()}</div>
            <div class="d-flex justify-content-between text-muted small mb-2">
              <span><i class="bi bi-geo-alt me-1"></i>${escapeHtml(locationName)}</span>
              <span><i class="bi bi-star-fill text-warning me-1"></i>${escapeHtml(String(rating))}</span>
            </div>
            <div class="mt-auto d-flex justify-content-between align-items-center">
              <small class="text-muted">by ${escapeHtml(seller)}</small>
              <a class="btn btn-emerald btn-sm view-details-btn" href="advertisement-details.html?id=${postId}">
                View Details
              </a>
            </div>
          </div>
        </div>
      </div>
    `
        $container.append(adCard)
    })
}

function postIdFrom(post) {
    if (!post) return ""
    if (post.postId) return post.postId
    if (post._id) return post._id
    return ""
}

function renderPagination(current, total) {
    const $pagination = $(".pagination")
    $pagination.empty()

    if (total <= 0) {
        total = 1
    }
    if (current < 0) {
        current = 0
    }
    if (current >= total) {
        current = total - 1
    }

    const prevDisabled = current <= 0 ? "disabled" : ""
    const prevPage = Math.max(0, current - 1)
    $pagination.append(`<li class="page-item ${prevDisabled}">
      <a class="page-link" href="#" data-page="${prevPage}">Previous</a>
    </li>`)

    const maxButtons = 7
    let start = Math.max(0, current - Math.floor(maxButtons / 2))
    const end = Math.min(total - 1, start + maxButtons - 1)
    if (end - start < maxButtons - 1) start = Math.max(0, end - maxButtons + 1)

    if (start > 0) {
        $pagination.append(`<li class="page-item">
            <a class="page-link" href="#" data-page="0">1</a>
        </li>`)
        if (start > 1) {
            $pagination.append(`<li class="page-item disabled">
                <span class="page-link">...</span>
            </li>`)
        }
    }

    for (let i = start; i <= end; i++) {
        const active = i === current ? "active" : ""
        $pagination.append(`<li class="page-item ${active}">
        <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
      </li>`)
    }

    if (end < total - 1) {
        if (end < total - 2) {
            $pagination.append(`<li class="page-item disabled">
                <span class="page-link">...</span>
            </li>`)
        }
        $pagination.append(`<li class="page-item">
            <a class="page-link" href="#" data-page="${total - 1}">${total}</a>
        </li>`)
    }

    const nextDisabled = current >= total - 1 ? "disabled" : ""
    const nextPage = Math.min(total - 1, current + 1)
    $pagination.append(`<li class="page-item ${nextDisabled}">
      <a class="page-link" href="#" data-page="${nextPage}">Next</a>
    </li>`)

    $pagination.find("a.page-link").on("click", function (e) {
        e.preventDefault()
        const page = Number.parseInt($(this).data("page"))
        if (!isNaN(page) && page >= 0 && page < total) {
            changePage(page)
        }
    })
}

function changePage(page) {
    if (page < 0 || page >= totalPages) {
        console.warn(`Invalid page number: ${page}. Valid range: 0-${totalPages - 1}`)
        return
    }

    if (page === currentPage) {
        return
    }

    loadPosts(page)
}

function initializeFilters() {
    $("#applyFilters").on("click", applyFilters)
    $("#clearFilters").on("click", clearAllFilters)
    $("#searchInput").on("input", debounce(applyFilters, 300))
    $("#categoryFilter, #locationFilter, #sortBy").on("change", applyFilters)
    $("#minPrice, #maxPrice").on("input", debounce(applyFilters, 400))
    $('input[type="checkbox"]').on("change", applyFilters)
}

function applyFilters() {
    const search = $("#searchInput").val().trim().toLowerCase()
    const category = ($("#categoryFilter").val() || "").toLowerCase()
    const location = ($("#locationFilter").val() || "").toLowerCase()
    const minPrice = Number.parseInt($("#minPrice").val()) || 0
    const maxPrice = Number.parseInt($("#maxPrice").val()) || Number.POSITIVE_INFINITY
    const sortBy = $("#sortBy").val() || "newest"

    let $items = $(".ad-item")

    if (search) {
        $items = $items.filter(function () {
            const t = $(this).find(".card-title").text().toLowerCase()
            return t.includes(search)
        })
    }

    if (category) {
        $items = $items.filter(function () {
            return ($(this).data("category") || "").toString() === category
        })
    }

    if (location) {
        $items = $items.filter(function () {
            return ($(this).data("location") || "").toString() === location
        })
    }

    $items = $items.filter(function () {
        const p = Number($(this).data("price") || 0)
        return p >= minPrice && p <= maxPrice
    })

    $(".ad-item").hide()

    let arr = $items.toArray()
    arr = sortDomArray(arr, sortBy)

    $(arr).show()
    $("#resultCount").text(arr.length.toLocaleString())
    $("#searchTime").text((Math.random() * 0.5 + 0.1).toFixed(2))
}

function sortDomArray(domArray, sortBy) {
    const arr = domArray.slice()
    switch (sortBy) {
        case "price-low":
            return arr.sort((a, b) => Number($(a).data("price") || 0) - Number($(b).data("price") || 0))
        case "price-high":
            return arr.sort((a, b) => Number($(b).data("price") || 0) - Number($(a).data("price") || 0))
        case "oldest":
            return arr.reverse()
        case "popular":
            return arr.sort((a, b) => {
                const ra = Number.parseFloat($(a).find(".bi-star-fill").parent().text()) || 0
                const rb = Number.parseFloat($(b).find(".bi-star-fill").parent().text()) || 0
                return rb - ra
            })
        default:
            return arr
    }
}

function clearAllFilters() {
    $("#searchInput").val("")
    $("#categoryFilter").val("")
    $("#locationFilter").val("")
    $("#minPrice").val("")
    $("#maxPrice").val("")
    $('input[type="checkbox"]').prop("checked", false)
    $("#sortBy").val("newest")
    applyFilters()
}

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

function initializeSorting() {
    $("#sortBy").on("change", applyFilters)
}

function checkAuth() {
    const $authBtn = $("#signInBtn")
    const $postAdBtn = $("#postAdBtn")
    const token = getCookie("token")

    $authBtn.off("click")
    $postAdBtn.off("click")

    if (token) {
        $authBtn.text("Logout").removeClass("btn-outline-secondary").addClass("btn-danger").show()
        $authBtn.on("click", () => {
            if ($authBtn.text().trim() === "Logout") doLogout("/index.html")
        })
        $postAdBtn.on("click", () => {
            location.href = "pages/postad.html"
        })
    } else {
        $authBtn.text("Sign In").removeClass("btn-danger").addClass("btn-outline-secondary").show()
        $authBtn.on("click", () => {
            if ($authBtn.text().trim() === "Sign In") location.href = "pages/signin.html"
        })
        $postAdBtn.on("click", () => {
            location.href = "pages/signup.html"
        })
    }
}

function doLogout(redirectUrl = "/index.html") {
    if (confirm("Are you sure you want to logout?")) {
        deleteCookie("token")
        deleteCookie("user")
        window.location.href = redirectUrl
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`
    const parts = value.split(`; ${name}=`)
    if (parts.length === 2) return parts.pop().split(";").shift()
    return null
}

function deleteCookie(name) {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`
}

function showToast(message, type = "info") {
    const toast = $(`
    <div class="toast align-items-center text-white bg-${type === "success" ? "success" : "info"} border-0" role="alert">
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
      </div>
    </div>`)
    if (!$("#toastContainer").length)
        $("body").append('<div id="toastContainer" class="toast-container position-fixed top-0 end-0 p-3"></div>')
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
    e.preventDefault();
    const $card = $(this).closest(".ad-item")
    const categoryValue = $card.find(".category-badge").text().trim().toLowerCase();
    const postId = $card.data("post-id") || ""
    window.location.href = `../pages/ad-details.html?categoryName=${categoryValue}&postId=${postId}`;
});
