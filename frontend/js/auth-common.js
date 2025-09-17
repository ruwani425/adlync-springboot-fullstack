function checkAuth() {
    const token = getCookie("token")

    if (token) {
        $("#signInBtn").hide()
        $("#profileDropdown").show()
    } else {
        $("#signInBtn").show()
        $("#profileDropdown").hide()
    }
}

function logout() {
    deleteCookie("token")
    window.location.href = "../index.html"
}

$(document).ready(() => {
    checkAuth()
})
