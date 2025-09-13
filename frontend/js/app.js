$(function () {
    const $grid = $('#catGrid');
    const $prev = $('#prevCat');
    const $next = $('#nextCat');
    const $dots = $('#catDots');

    if (!$grid.length) return;

    const itemWidth = 240;
    const perPage = () => Math.floor($grid.width() / itemWidth) || 1;
    const maxIndex = () => Math.max(0, Math.ceil($grid.children().length - perPage()));

    let index = 0;

    function updateDots() {
        const pages = Math.ceil($grid.children().length / perPage());
        $dots.empty();
        for (let i = 0; i < pages; i++) {
            const $b = $('<button>')
                .addClass('dot' + (i === Math.floor(index) ? ' active' : ''))
                .attr('aria-label', 'Go to page ' + (i + 1))
                .on('click', function () {
                    index = i;
                    scroll();
                });
            $dots.append($b);
        }
    }

    function scroll() {
        const x = index * itemWidth;
        $grid.animate({scrollLeft: x}, 400);
        updateDots();
        $prev.prop('disabled', index <= 0);
        $next.prop('disabled', index >= maxIndex());
    }

    $prev.on('click', function () {
        index = Math.max(0, index - 1);
        scroll();
    });

    $next.on('click', function () {
        index = Math.min(maxIndex(), index + 1);
        scroll();
    });

    $(window).on('resize', function () {
        index = 0;
        updateDots();
    });

    $('<style>')
        .text(`
        #catDots .dot { width:8px; height:8px; border-radius:999px; border:none; margin:0 4px; background:#c7d2fe; }
        #catDots .dot.active { background: var(--emerald-600); }
    `)
        .appendTo('head');

    updateDots();
    checkAuth();
});

function checkAuth() {
    const $authBtn = $('#signInBtn');
    const $postAdBtn = $('#postAdBtn');
    const token = getCookie("token");

    $authBtn.off('click');
    $postAdBtn.off('click');

    if (token) {
        $authBtn.text("Logout")
            .removeClass("btn-outline-primary")
            .addClass("btn-danger")
            .show();

        $authBtn.on("click", function () {
            if ($authBtn.text().trim() === "Logout") {
                doLogout("index.html");
            }
        });

        $postAdBtn.on('click', function () {
            location.href = "pages/postad.html";
        });

    } else {
        $authBtn.text("Sign In")
            .removeClass("btn-danger")
            .addClass("btn-outline-primary")
            .show();

        $authBtn.on('click', function () {
            if ($authBtn.text().trim() === "Sign In") {
                location.href = "pages/signin.html";
            }
        });

        $postAdBtn.on('click', function () {
            location.href = "pages/signup.html";
        });
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

function doLogout(redirectUrl = "index.html") {
    if (confirm("Are you sure you want to logout?")) {
        deleteCookie("token");
        deleteCookie("user");
        window.location.href = redirectUrl;
    }
}

$(document).ready(function () {
    let currentPage = 0;
    let totalPages = 1;
    const cardsPerPage = 3;

    function fetchFeaturedPosts(page = 0) {
        $.ajax({
            url: `http://localhost:8080/api/posts/approved/recent?page=${page}&size=${cardsPerPage}`,
            method: 'GET',
            headers: {
                "Authorization": "Bearer " + getCookie("token")
            },
            success: function (data) {
                // data is a PageResponse object
                renderPage(data.content);
                currentPage = data.pageNumber;
                totalPages = data.totalPages;

                // Enable/disable buttons based on page
                $('#prevPage').prop('disabled', currentPage <= 0);
                $('#nextPage').prop('disabled', currentPage >= totalPages - 1);

                $('#currentPage').text(currentPage + 1);
            },
            error: function () {
                console.error('Failed to fetch featured posts');
            }
        });
    }

    function renderPage(posts) {
        const $container = $('#featuredPostsContainer');
        $container.empty();

        posts.forEach(post => {
            const imageUrl = post.images && post.images.length
                ? post.images[0].image_url
                : 'https://picsum.photos/seed/default/800/480';

            const card = `
                <div class="col-md-6 col-lg-4">
                    <div class="card card-hover h-100 overflow-hidden">
                        <div class="position-relative">
                            <img alt="${post.title}" class="w-100 object-cover" src="${imageUrl}" style="height:220px"/>
                            <span class="badge text-bg-emerald position-absolute top-0 start-0 m-3">
                                ${post.category ? post.category.name : 'Other'}
                            </span>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">${post.title}</h5>
                            <div class="fs-5 fw-bold text-primary-emerald mb-1">Rs. ${post.price}</div>
                            <div class="d-flex justify-content-between text-muted">
                                <small>${post.location ? post.location.city : ''}</small>
                                <small><i class="bi bi-star-fill text-warning me-1"></i>4.5</small>
                            </div>
                            <div class="d-flex justify-content-between align-items-center mt-3">
                                <small class="text-muted">by ${post.user ? post.user.name : 'Unknown'}</small>
                                <a class="btn btn-emerald btn-sm" href="#">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>`;
            $container.append(card);
        });
    }

    $('#prevPage').click(function () {
        if (currentPage > 0) {
            fetchFeaturedPosts(currentPage - 1);
        }
    });

    $('#nextPage').click(function () {
        if (currentPage < totalPages - 1) {
            fetchFeaturedPosts(currentPage + 1);
        }
    });

    fetchFeaturedPosts();

    $("#viewAllBtn").on("click", function () {
        window.location.href = "pages/advertisement.html";
    });
});


