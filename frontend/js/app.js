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
});

$(function () {
    $('#postAdBtn').on('click', function () {
        window.location.href = 'pages/signup.html';
    });
});

$(function () {
    $('#signInBtn').on('click', function () {
        window.location.href = 'pages/signin.html';
    });
});