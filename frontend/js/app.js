const $ = window.$

$(() => {
    const $grid = $("#catGrid")
    const $prev = $("#prevCat")
    const $next = $("#nextCat")
    const $dots = $("#catDots")

    if (!$grid.length) return

    const itemWidth = 240
    const perPage = () => Math.floor($grid.width() / itemWidth) || 1
    const maxIndex = () => Math.max(0, Math.ceil($grid.children().length - perPage()))

    let index = 0

    function updateDots() {
        const pages = Math.ceil($grid.children().length / perPage())
        $dots.empty()
        for (let i = 0; i < pages; i++) {
            const $b = $("<button>")
                .addClass("dot" + (i === Math.floor(index) ? " active" : ""))
                .attr("aria-label", "Go to page " + (i + 1))
                .on("click", () => {
                    index = i
                    scroll()
                })
            $dots.append($b)
        }
    }

    function scroll() {
        const x = index * itemWidth
        $grid.animate({scrollLeft: x}, 400)
        updateDots()
        $prev.prop("disabled", index <= 0)
        $next.prop("disabled", index >= maxIndex())
    }

    $prev.on("click", () => {
        index = Math.max(0, index - 1)
        scroll()
    })

    $next.on("click", () => {
        index = Math.min(maxIndex(), index + 1)
        scroll()
    })

    $(window).on("resize", () => {
        index = 0
        updateDots()
    })

    $("<style>")
        .text(`
        #catDots .dot { width:8px; height:8px; border-radius:999px; border:none; margin:0 4px; background:#c7d2fe; }
        #catDots .dot.active { background: var(--emerald-600); }
    `)
        .appendTo("head")

    updateDots()
    checkAuth()
})
function checkAuth() {
    const $authBtn = $("#signInBtn")
    const $postAdBtn = $("#postAdBtn")
    const $profileDropdown = $("#profileDropdown")
    const token = getCookie("token")

    $authBtn.off("click")
    $postAdBtn.off("click")

    if (token) {
        $authBtn.hide()
        $profileDropdown.show()

        fetchUserProfile(token)

        $("#profileLink").on("click", (e) => {
            e.preventDefault()
            window.location.href = "pages/user-profile.html"
        })

        function doLogout(redirectUrl) {
            deleteCookie("token");

            Swal.fire({
                title: 'Logged out!',
                text: 'You have been successfully logged out.',
                icon: 'success',
                timer: 1500,
                showConfirmButton: false
            }).then(() => {
                // Update CTA after logout
                updateCTASection()
                window.location.href = redirectUrl;
            });
        }

        $("#logoutLink").on("click", (e) => {
            e.preventDefault();
            doLogout("index.html");
        });

        $postAdBtn.on("click", () => {
            window.location.href = "pages/postad.html"
        })
    } else {
        $authBtn.show()
        $profileDropdown.hide()

        $authBtn.on("click", () => {
            location.href = "pages/signin.html"
        })

        $postAdBtn.on("click", () => {
            location.href = "pages/signup.html"
        })
    }

    // Always update CTA section after auth check
    updateCTASection()
}
function fetchUserProfile(token) {
    $.ajax({
        url: "http://localhost:8080/api/users/getUserByToken",
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: function (response) {
            console.log("User data loaded for navbar:", response);
            console.log("Profile Image URL:", response.profileImageUrl);

            const $navProfileImg = $("#profileDropdown img");

            let profileUrl = response.profileImageUrl;

            if (!profileUrl || profileUrl.trim() === '') {
                profileUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(response.name)}&background=10b981&color=fff&size=40&rounded=true`;
                console.log("Using fallback avatar for navbar:", profileUrl);
            } else {
                profileUrl = profileUrl.trim();
                console.log("Using database profile photo for navbar:", profileUrl);
            }

            $navProfileImg.attr('src', profileUrl);
            $navProfileImg.attr('alt', response.name + "'s profile");
        },
        error: function (xhr, status, error) {
            console.error("Error fetching user profile for navbar:", error);
        }
    });
}

function getCookie(name) {
    const value = `; ${document.cookie}`
    const parts = value.split(`; ${name}=`)
    if (parts.length === 2) return parts.pop().split(";").shift()
    return null
}

function deleteCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
}

function doLogout(redirectUrl = "index.html") {
    if (confirm("Are you sure you want to logout?")) {
        deleteCookie("token")
        deleteCookie("user")
        window.location.href = redirectUrl
    }
}

$(document).ready(() => {
    let currentPage = 0
    let totalPages = 1
    const cardsPerPage = 3

    function fetchFeaturedPosts(page = 0) {
        $.ajax({
            url: `http://localhost:8080/api/posts/approved/recent?page=${page}&size=${cardsPerPage}`,
            method: "GET",
            success: (data) => {
                console.log("API Response:", data);

                renderPage(data.content || []);

                currentPage = data.pageNumber !== undefined ? data.pageNumber : 0;
                totalPages = data.totalPages !== undefined ? data.totalPages : 1;

                console.log("Current Page:", currentPage, "Total Pages:", totalPages);

                $("#prevPage").prop("disabled", currentPage <= 0);
                $("#nextPage").prop("disabled", currentPage >= totalPages - 1);
                $("#currentPage").text(currentPage + 1);

                if ($("#totalPages").length) {
                    $("#totalPages").text(totalPages);
                }
            },
            error: (xhr, status, error) => {
                console.error("Failed to fetch featured posts:", error);
                $("#featuredPostsContainer").html("<p class='text-danger'>Failed to load featured posts.</p>");
            },
        })
    }

    function renderPage(posts) {
        const $container = $("#featuredPostsContainer")
        $container.empty()

        posts.forEach((post) => {
            const imageUrl =
                post.images && post.images.length ? post.images[0].image_url : "https://picsum.photos/seed/default/800/480"

            const card = `
                <div class="col-md-6 col-lg-4 ad-item" data-aos="flip-left" data-aos-delay="200" data-post-id="${post.post_id}">
                    <div class="card card-hover h-100 overflow-hidden">
                        <div class="position-relative">
                            <img alt="${post.title}" class="w-100 object-cover" src="${imageUrl}" style="height:220px"/>
                            <span class="badge text-bg-emerald position-absolute top-0 start-0 m-3 category-badge">
                                ${post.category ? post.category.name : "Other"}
                            </span>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">${post.title}</h5>
                            <div class="fs-5 fw-bold text-primary-emerald mb-1">Rs. ${post.price}</div>
                            <div class="d-flex justify-content-between text-muted">
                                <small>${post.location ? post.location.city : ""}</small>
                                <small><i class="bi bi-star-fill text-warning me-1"></i>4.5</small>
                            </div>
                            <div class="d-flex justify-content-between align-items-center mt-3">
                                <small class="text-muted">by ${post.user ? post.user.name : "Unknown"}</small>
                                <a class="btn btn-emerald btn-sm view-details-btn" href="#">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>`
            $container.append(card)
        })
    }

    $(".nav-link").filter(function () {
        return $(this).text().trim() === "Browse";
    }).on("click", function (e) {
        e.preventDefault();
        window.location.href = "pages/advertisement.html";
    });

    $("#prevPage").click(() => {
        if (currentPage > 0) {
            fetchFeaturedPosts(currentPage - 1)
        }
    })

    $("#nextPage").click(() => {
        if (currentPage < totalPages - 1) {
            fetchFeaturedPosts(currentPage + 1)
        }
    })

    fetchFeaturedPosts()

    $("#viewAllBtn").on("click", () => {
        window.location.href = "pages/advertisement.html"
    })

    $(document).on("click", ".view-details-btn", function (e) {
        e.preventDefault();
        const $card = $(this).closest(".ad-item");
        const categoryValue = $card.find(".category-badge").text().trim().toLowerCase();
        const postId = $card.data("post-id") || "";
        window.location.href = `pages/ad-details.html?categoryName=${encodeURIComponent(categoryValue)}&postId=${postId}`;
    });
    updateCTASection()
})

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
    'fashion and beauty': 'FASHION_AND_BEAUTY',
    'entertainment': 'ENTERTAINTMENT',
    'education': 'EDUCATION',
    'mobile': 'MOBILE',
    'work overseas': 'WORK_OVERSEAS',
    'home and garden': 'HOME_AND_GARDEN',
    'essentials': 'ESSENTIALS'
};

$(document).on('click', '.category-col .card', function (e) {
    e.preventDefault();

    const categoryDisplay = $(this).find('h6').text().trim().toLowerCase();
    console.log('Clicked category:', categoryDisplay);

    const categoryEnum = categoryMap[categoryDisplay];

    if (categoryEnum) {
        window.location.href = `pages/advertisement.html?category=${categoryEnum}`;
    } else {
        console.warn('Category not found in mapping:', categoryDisplay);
        window.location.href = 'pages/advertisement.html';
    }
});

$(document).on('click', '.category-col .card a', function (e) {
    e.preventDefault();
    $(this).closest('.card').trigger('click');
});
function updateCTASection() {
    const token = getCookie("token")
    const $ctaTitle = $("#ctaTitle")
    const $ctaDescription = $("#ctaDescription")
    const $ctaMainBtn = $("#ctaMainBtn")

    $ctaMainBtn.off("click")

    if (token) {
        $ctaTitle.text("Ready to Post an Ad?")
        $ctaDescription.text("Create your advertisement and reach thousands of potential buyers")
        $ctaMainBtn.text("Post Ad")
        $ctaMainBtn.removeClass("btn-light").addClass("btn-light")

        $ctaMainBtn.on("click", (e) => {
            e.preventDefault()
            window.location.href = "pages/postad.html"
        })

        console.log("CTA updated for authenticated user")
    } else {
        $ctaTitle.text("Ready to Start Selling?")
        $ctaDescription.text("Join thousands of sellers and reach millions of buyers")
        $ctaMainBtn.text("Post Your First Ad")
        $ctaMainBtn.removeClass("btn-light").addClass("btn-light")

        $ctaMainBtn.on("click", (e) => {
            e.preventDefault()
            window.location.href = "pages/signup.html"
        })

        console.log("CTA updated for guest user")
    }
}