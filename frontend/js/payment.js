$(document).ready(function () {
    checkAuthAndSetup();
    updatePaymentAmount();

    $('#proceedPaymentBtn').on('click', processPayment);

    $('#bankSlipUpload').on('change', function () {
        previewFile(this);
    });

    $('.payment-method-card').on('click', function () {
        const method = $(this).data('method');
        selectPaymentMethod(method);
    });
});

function checkAuthAndSetup() {
    const token = getCookie("token");
    const user = getCookie("user");

    if (token) {
        profileImageManager.init().then((isAuthenticated) => {
            if (isAuthenticated) {
                setupAuthenticatedUser(user);
            } else {
                setupUnauthenticatedUser();
            }
        });
    } else {
        setupUnauthenticatedUser();
    }
}

function setupAuthenticatedUser(user) {
    $("#signInBtn").hide();
    $("#profileDropdown").show();
    $("#mainPaymentContent").show();
    $("#signInPrompt").hide();
    $("#authAlert").addClass('d-none');

    $("#profileLink").on("click", function (e) {
        e.preventDefault();
        location.href = "../pages/user-profile.html";
    });

    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        doLogout("../index.html");
    });
}

function setupUnauthenticatedUser() {
    $("#signInBtn").show();
    $("#profileDropdown").hide();
    $("#mainPaymentContent").hide();
    $("#signInPrompt").show();

    $("#signInBtn").on("click", function () {
        location.href = "signin.html";
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

function selectPaymentMethod(method) {
    $('.payment-method-card').removeClass('selected');
    $(`[data-method="${method}"]`).addClass('selected');
    $(`#${method === 'card' ? 'cardPayment' : 'bankPayment'}`).prop('checked', true);

    if (method === 'card') {
        $('#cardPaymentForm').show();
        $('#bankSlipForm').hide();
        $('#btnText').html('<i class="bi bi-credit-card me-2"></i>Proceed to Payment Gateway');
    } else {
        $('#cardPaymentForm').hide();
        $('#bankSlipForm').show();
        $('#btnText').html('<i class="bi bi-cloud-upload me-2"></i>Submit Bank Transfer');
    }

    $('#proceedPaymentBtn').show();
}

function previewFile(input) {
    if (input.files && input.files[0]) {
        const file = input.files[0];
        const fileName = file.name;
        const fileSize = (file.size / 1024 / 1024).toFixed(2);
        $('#fileName').text(`${fileName} (${fileSize} MB)`);
        $('#filePreview').show();
    }
}

function updatePaymentAmount() {
    const baseAmount = 500;
    const featuredFee = 250;
    const totalAmount = baseAmount + featuredFee;
    $('#feeAmount').text(`LKR ${totalAmount.toFixed(2)}`);
}

function processPayment() {
    const paymentMethod = $('input[name="paymentMethod"]:checked').val();

    if (!paymentMethod) {
        alert('Please select a payment method.');
        return;
    }

    if (paymentMethod === 'card') {
        processCardPayment();
    } else {
        processBankTransfer();
    }
}

function processCardPayment() {
    $('#loadingOverlay').css('display', 'flex');

    // savePost('CARD');

    setTimeout(() => {
        $('#loadingOverlay').hide();
        window.location.href = "https://buy.stripe.com/test_14A3cofcm2Om9LxfdU8Zq00";
    }, 1500);
}


async function processBankTransfer() {
    const bankSelect = $('#selectedBank');
    const fileInput = $('#bankSlipUpload')[0];

    if (!bankSelect.val()) {
        alert('Please select the bank you used for payment.');
        bankSelect.focus();
        return;
    }

    if (!fileInput.files || !fileInput.files[0]) {
        alert('Please upload your payment slip.');
        $('#bankSlipUpload').focus();
        return;
    }

    $('#loadingOverlay').css('display', 'flex');

    try {
        const uploadedUrls = await uploadImagesToFirebase([fileInput.files[0]]);
        const slipUrl = uploadedUrls[0];
        savePostWithBankSlip('BANK_TRANSFER', bankSelect.val(), slipUrl);
    } catch (error) {
        $('#loadingOverlay').hide();
        alert("Bank slip upload failed. Try again!");
    }
}

function savePostWithBankSlip(paymentMethod, bankName, slipUrl) {
    let storedData = localStorage.getItem("adFormData");
    console.log(storedData)
    if (!storedData) {
        alert("No form data found!");
        return;
    }

    let adData = JSON.parse(storedData);
    let category = localStorage.getItem("selectedCategory");

    const endpoints = {
        agriculture: 'http://localhost:8080/api/posts/create-agriculture',
        electronics: 'http://localhost:8080/api/posts/create-electronic',
        fashion: 'http://localhost:8080/api/posts/create-fashion-and-beauty',
        sports: 'http://localhost:8080/api/posts/create-sport',
        vehicles: 'http://localhost:8080/api/posts/create-vehicle',
        animals: 'http://localhost:8080/api/posts/create-animal',
        properties: 'http://localhost:8080/api/posts/create-property',
        jobs: 'http://localhost:8080/api/posts/create-job',
        services: 'http://localhost:8080/api/posts/create-service',
        kids: 'http://localhost:8080/api/posts/create-kids',
        entertainment: 'http://localhost:8080/api/posts/create-entertainment',
        education: 'http://localhost:8080/api/posts/create-education',
        mobile: 'http://localhost:8080/api/posts/create-mobile',
        work_overseas: 'http://localhost:8080/api/posts/create-work-over-sea',
        home_garden: 'http://localhost:8080/api/posts/create-home-and-garden',
        essentials: 'http://localhost:8080/api/posts/create-essential'
    };

    const apiUrl = endpoints[category];
    if (!apiUrl) {
        alert("Invalid category selected!");
        return;
    }

    let token = getCookie('token');
    if (!token) {
        alert("Authentication token not found. Please login again.");
        return;
    }

    let amountText = $('#feeAmount').text();
    let amount = parseFloat(amountText.replace(/[^\d.]/g, ''));

    adData.postRequestDTO.payment_type = paymentMethod;
    adData.postRequestDTO.amount = amount;
    adData.postRequestDTO.payment_status = 'COMPLETED';
    adData.postRequestDTO.bank_name = bankName;
    adData.postRequestDTO.slip_url = slipUrl;

    $.ajax({
        url: apiUrl,
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(adData),
        success: function (response) {
            $('#loadingOverlay').hide();

            Swal.fire({
                icon: 'success',
                title: 'Your bank transfer request was submitted',
                text: response.message || 'Our team will verify your payment slip shortly.',
                showConfirmButton: false,
                timer: 2000
            });
            setTimeout(() => {
                window.location.href = "../index.html";
            }, 2100);
        },
        error: function (xhr) {
            const errorMsg = xhr.responseJSON?.message || 'Request failed. Please try again.';
            $('#loadingOverlay').hide();

            Swal.fire({
                icon: 'error',
                title: 'Request Failed',
                text: errorMsg,
                showConfirmButton: false,
                timer: 2000
            });
        }
    });
}
