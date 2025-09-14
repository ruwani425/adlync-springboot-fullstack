$(document).ready(function () {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    console.log("Token:", token);
    $("#moderator-sign-up").click(function () {
        const password = $("#moderator-password").val();
        $.ajax({
            url: `http://localhost:8080/api/users/set-moderator-password?token=${token}&password=${password}`,
            type: "PATCH",
            success: function (response) {
                console.log("Password set successfully!", response);
            },
            error: function (xhr) {
                console.error("Error:", xhr.responseText);
            }
        });
    })


});

console.log("hi")