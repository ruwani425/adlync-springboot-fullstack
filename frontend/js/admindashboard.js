// Sidebar toggle for mobile
$('#sidebarToggle').on('click', function() {
    $('#sidebar').toggleClass('show');
});

// Section navigation
function showSection(section) {
    $('.content-section').addClass('d-none');
    $('#' + section + '-section').removeClass('d-none');

    $('#pageTitle').text(
        $('.nav-link[data-section="' + section + '"]').text().trim()
    );

    $('.nav-link').removeClass('active');
    $('.nav-link[data-section="' + section + '"]').addClass('active');
}

// Sidebar navigation click
$('.sidebar-nav .nav-link').on('click', function(e) {
    e.preventDefault();
    showSection($(this).data('section'));
});
