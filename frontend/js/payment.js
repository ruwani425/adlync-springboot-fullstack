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
        //
        // if (file.size > 5) {
        //     alert('File size must be less than 5MB');
        //     $(input).val('');
        //     $('#filePreview').hide();
        //     return;
        // }

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
        processCardPayment(paymentMethod);
    } else {
        processBankTransfer(paymentMethod);
    }
}

function processCardPayment(paymentMethod) {
    savePost('CARD');
    $('#loadingOverlay').css('display', 'flex');

    const paymentGatewayUrl = 'https://your-payment-gateway.com/checkout';
    const paymentData = {
        amount: 750.00,
        currency: 'LKR',
        description: 'Advertisement Posting Fee',
        return_url: window.location.origin + '/payment-success',
        cancel_url: window.location.origin + '/payment-cancel',
        customer_email: 'customer@example.com',
        order_id: 'AD' + Date.now(),
    };

    const form = $('<form>', {method: 'POST', action: paymentGatewayUrl});
    $.each(paymentData, function (key, value) {
        $('<input>', {type: 'hidden', name: key, value: value}).appendTo(form);
    });
    $('body').append(form);

    setTimeout(() => {
        alert('Redirecting to payment gateway...\n\nIn a real implementation, this would redirect to your payment provider (PayHere, Stripe, etc.)');
        $('#loadingOverlay').hide();
        // form.submit(); // Uncomment in real implementation
    }, 2000);
}

async function processBankTransfer(paymentMethod) {
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

        console.log("Uploaded Payment Slip URL:", slipUrl);

        // Save post with slipUrl + bankName
        savePostWithBankSlip('BANK_TRANSFER', bankSelect.val(), slipUrl);

    } catch (error) {
        $('#loadingOverlay').hide();
        alert("Bank slip upload failed. Try again!");
    }
}

function savePostWithBankSlip(paymentMethod, bankName, slipUrl) {
    let storedData = localStorage.getItem("adFormData");
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
            localStorage.removeItem("adFormData");
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


$(document).ready(function () {
    updatePaymentAmount();
});
