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

        if (file.size > 5 * 1024 * 1024) {
            alert('File size must be less than 5MB');
            $(input).val('');
            $('#filePreview').hide();
            return;
        }

        $('#fileName').text(`${fileName} (${fileSize} MB)`);
        $('#filePreview').show();
    }
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
    savePost();
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
        // form.submit(); // Uncomment for real implementation
    }, 2000);
}

function processBankTransfer() {
    savePost();
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

    setTimeout(() => {
        const formData = new FormData();
        formData.append('bank_slip', fileInput.files[0]);
        formData.append('bank_name', bankSelect.val());
        formData.append('amount', 750.00);
        formData.append('payment_method', 'bank_transfer');

        console.log('Bank transfer data:', {
            bank: bankSelect.val(),
            file: fileInput.files[0].name,
            amount: 750.00
        });

        $('#loadingOverlay').hide();
        $('.card-body').hide();
        $('#successMessage').show();
    }, 3000);
}

function updatePaymentAmount() {
    const baseAmount = 500;
    const featuredFee = 250;
    const totalAmount = baseAmount + featuredFee;

    $('#feeAmount').text(`LKR ${totalAmount.toFixed(2)}`);
}

$(document).ready(function () {
    updatePaymentAmount();
});

function savePost() {
    let storedData = localStorage.getItem("adFormData");
    if (!storedData) {
        alert("No form data found!");
        return;
    }
    let adData = JSON.parse(storedData);
    // let category = adData.category;
    let category=localStorage.getItem("selectedCategory")
    console.log(category)

    const endpoints = {
        agriculture: 'http://localhost:8080/api/agricultures',
        electronics: 'http://localhost:8080/api/electronics',
        fashion: 'http://localhost:8080/api/fashion',
        sports: 'http://localhost:8080/api/sports',
        vehicles: 'http://localhost:8080/api/posts/create-vehicle',
        animals: 'http://localhost:8080/api/posts/create-animal',
        properties: 'http://localhost:8080/api/properties',
        jobs: 'http://localhost:8080/api/jobs',
        services: 'http://localhost:8080/api/services',
        kids: 'http://localhost:8080/api/kids',
        entertainment: 'http://localhost:8080/api/entertainment',
        education: 'http://localhost:8080/api/education',
        mobile: 'http://localhost:8080/api/mobile',
        work_overseas: 'http://localhost:8080/api/work_overseas',
        home_garden: 'http://localhost:8080/api/home_garden',
        essentials: 'http://localhost:8080/api/essentials'
    };

    const apiUrl = endpoints[category];
    if (!apiUrl) {
        alert("Invalid category selected!");
        return;
    }
    var token = getCookie('token');

    if (token == null) {
        alert("Authentication token not found. Please login again.");
        return;
    }
    console.log(token)
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
                title: 'Your advertisement request was sent successfully',
                text: response.message || 'Request sent successfully!',
                showConfirmButton: false,
                timer: 1500
            });
            setTimeout(() => {
                window.location.href = "../index.html";
            }, 1600);
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
