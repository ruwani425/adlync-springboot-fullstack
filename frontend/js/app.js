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

        $authBtn.on('click', function () {
            if ($authBtn.text().trim() === "Logout") {
                //clear cookie
                if (confirm('Are you sure you want to logout?')) {
                    document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
                    location.href = "index.html";
                }
                // document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
                checkAuth();
                // Swal.fire({
                //     icon: 'success',
                //     title: 'Logged Out',
                //     text: 'You have been successfully logged out.',
                //     showConfirmButton: false,
                //     timer: 1500
                // });
                setTimeout(() => {
                    location.href = "index.html";
                }, 1500);
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