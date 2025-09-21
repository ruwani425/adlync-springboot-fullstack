function checkAuth() {
    const $authBtn = $("#signInBtn");
    const $postAdBtn = $("#postAdBtn");
    const $profileDropdown = $("#profileDropdown");
    const token = getCookie("token");

    $authBtn.off("click");
    $postAdBtn.off("click");

    if (token) {
        $authBtn.hide();
        $profileDropdown.show();

        fetchUserProfile(token);

        $("#profileLink").on("click", (e) => {
            e.preventDefault();
            window.location.href = "../pages/user-profile.html";
        });

        $("#logoutLink").on("click", (e) => {
            e.preventDefault();
            doLogout("../index.html");
        });

        $postAdBtn.on("click", () => {
            window.location.href = "../pages/postad.html";
        });
    } else {
        $authBtn.show();
        $profileDropdown.hide();

        $authBtn.on("click", () => {
            location.href = "../pages/signin.html";
        });

        $postAdBtn.on("click", () => {
            location.href = "../pages/signup.html";
        });
    }
}

function fetchUserProfile(token) {
    $.ajax({
        url: "http://localhost:8080/api/users/getUserByToken",
        method: "GET",
        headers: {"Authorization": "Bearer " + token},
        success: function (response) {
            const $navProfileImg = $("#profileDropdown img");
            let profileUrl = response.profileImageUrl;

            if (!profileUrl || profileUrl.trim() === '') {
                profileUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(response.name)}&background=10b981&color=fff&size=40&rounded=true`;
            } else {
                profileUrl = profileUrl.trim();
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
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
}

function deleteCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function doLogout(redirectUrl = "../index.html") {
    if (confirm("Are you sure you want to logout?")) {
        deleteCookie("token");
        deleteCookie("user");
        window.location.href = redirectUrl;
    }
}

$(document).ready(function() {
    checkAuth();
});