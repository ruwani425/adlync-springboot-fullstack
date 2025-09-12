$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryName = urlParams.get("categoryName");
    const postId = urlParams.get("postId");

    console.log("Category:", categoryName);
    console.log("Post ID:", postId);

    if (!categoryName || !postId) return;

    let fields = [];
    let iconClass = "bi-card-text";

    switch (categoryName) {
        case "agriculture":
            fields = ["product_type", "quantity", "season", "variety", "production_Date", "certifications", "condition"];
            iconClass = "bi-seedling";
            break;
        case "animal":
            fields = ["species", "breed", "age", "gender", "vaccination_status"];
            iconClass = "bi-paw";
            break;
        case "education":
            fields = ["course_name", "institute", "duration", "qualification_offered", "subject_area", "study_mode", "education_level", "schedule", "requirements"];
            iconClass = "bi-book";
            break;
        case "electronic":
            fields = ["brand", "type", "model", "warranty", "condition", "accessories"];
            iconClass = "bi-tv";
            break;
        case "entertainment":
            fields = ["type", "format", "brand", "genre", "release_year", "rating", "creator", "condition"];
            iconClass = "bi-music-note-beamed";
            break;
        case "essential":
            fields = ["brand", "quantity", "expiry_date", "product_type", "storage_instructions", "condition"];
            iconClass = "bi-bag";
            break;
        case "fashion and beauty":
            fields = ["item_type", "brand", "size", "gender", "condition", "color", "material", "style_note"];
            iconClass = "bi-gem";
            break;
        case "home and garden":
            fields = ["item_type", "material", "dimensions", "condition", "brand", "color", "weight", "assembly_required", "special_features"];
            iconClass = "bi-house";
            break;
        case "job":
            fields = ["position", "company", "salary_min", "salary_max", "industry", "job_type", "requirements", "expiriance_level"];
            iconClass = "bi-briefcase";
            break;
        case "kids":
            fields = ["item_type", "age_rang", "brand", "condition", "size", "gender", "safety_information"];
            iconClass = "bi-emoji-smile";
            break;
        case "mobile":
            fields = ["storage", "condition", "warranty_status", "ram", "brand", "model", "colour", "included_accessories", "additional_information"];
            iconClass = "bi-phone";
            break;
        case "property":
            fields = ["type", "land_size", "bedroom", "barthroom", "furnished"];
            iconClass = "bi-building";
            break;
        case "service":
            fields = ["service_type", "provider_name", "availability", "charges", "service_area", "qualifications"];
            iconClass = "bi-tools";
            break;
        case "sport":
            fields = ["equipment_type", "brand", "condition", "size", "additional_information"];
            iconClass = "bi-basket";
            break;
        case "vehicle":
            fields = ["vehicle_type", "mileage", "year", "brand", "model", "fuel_type", "transmission", "condition"];
            iconClass = "bi-car-front";
            break;
        case "work over seas":
            fields = ["position", "country", "salary", "requirements", "contract_duration", "company_or_agency_name", "visa_status", "accommodation", "additional_benefits"];
            iconClass = "bi-globe";
            break;
        default:
            console.warn("Category not found");
    }

    $.ajax({
        url: `http://localhost:8080/api/posts/${postId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + getCookie("token")
        },
        success: function (data) {
            console.log("Post data:", data);

            $("#adTitle").text(data.title || "-");
            $("#adPrice").text(`Rs. ${data.price?.toLocaleString() || "-"}`);
            $("#postId").text(data.post_id || "-");
            $("#contactNumber").text(data.contact_number || "-");
            $("#postStatus").text(data.status || "-");
            $("#adDescription").html(`<p>${data.description || "-"}</p>`);

            $("#adStatus").html(`<i class="bi bi-circle-fill me-1"></i>${data.status || "-"}`);
            $("#adCategory").html(`<i class="bi bi-tag me-1"></i>${categoryName}`);
            $("#adCreatedAt").html(`<i class="bi bi-calendar me-1"></i>${data.createdAt || "Posted today"}`);
            $("#adType").html(`<i class="bi bi-bookmark me-1"></i>${data.advertisement_type.type}`);
            if (data.user) {
                const seller = data.user;

                $("#sellerNameValue").text(seller.name);
                $("#sellerEmail").attr("href", `mailto:${seller.email}`).text(seller.email);
                $("#sellerMemberSince").text("2020");
                $("#sellerRating").text("4.8/5");
            }


            if (fields.length > 0) {
                $("#categoryDetailsContent").empty();
                fields.forEach(field => {
                    const value = data[field] || "-";
                    const cardHtml = `
                        <div class="col-md-4">
                            <div class="card h-100 border-success">
                                <div class="card-body">
                                    <h6 class="card-title"><i class="bi ${iconClass} me-2"></i>${field.replace(/_/g, " ")}</h6>
                                    <p class="card-text">${value}</p>
                                </div>
                            </div>
                        </div>`;
                    $("#categoryDetailsContent").append(cardHtml);
                });
            }

            if (data.images && data.images.length > 0) {
                const $indicators = $("#carouselIndicators");
                const $inner = $("#carouselInner");
                $indicators.empty();
                $inner.empty();

                data.images.forEach((imageObj, index) => {
                    const imageUrl = imageObj.image_url;

                    const $indicator = $("<button>")
                        .attr("type", "button")
                        .attr("data-bs-target", "#carouselImages")
                        .attr("data-bs-slide-to", index);
                    if (index === 0) $indicator.addClass("active");
                    $indicators.append($indicator);

                    const $item = $("<div>").addClass(`carousel-item ${index === 0 ? "active" : ""}`);
                    $item.html(`<img src="${imageUrl}" class="d-block w-100 object-cover" style="height:400px" alt="Image ${index + 1}">`);
                    $inner.append($item);
                });
            }
            if (data.location) {
                const $locationContainer = $("#locationDetails");
                $locationContainer.empty();

                const locationHtml = `
                    <div class="col-12 mb-2">
                        <strong>Address:</strong><br>
                        <span class="text-muted">${data.location.address}</span>
                    </div>
                    <div class="col-12 mb-2">
                        <strong>City:</strong><br>
                        <span class="text-muted">${data.location.city}</span>
                    </div>
                    <div class="col-12 mb-2">
                        <strong>District:</strong><br>
                        <span class="text-muted">${data.location.district}</span>
                    </div>
                `;

                $locationContainer.append(locationHtml);
            }
            renderPostData(data, "#categoryDetailsContent");

        },
        error: function (xhr) {
            console.error("Error loading post:", xhr);
        }
    });
});
function renderPostData(data, containerSelector) {
    const $container = $(containerSelector);
    $container.empty();

    if (!data.common) return;

    $.each(data.common, function(category, value) {
        if (!value) return;

        $.each(value, function(field, fieldValue) {
            if (!fieldValue || field === "postResponseDTO") return;
            if (!fieldValue || field === "postResponseDTO" || field.toLowerCase() === "id" || field.toLowerCase().endsWith("_id")) return;

            const displayValue = fieldValue || "-";
            const $card = $(`
                <div class="col-md-4 mb-3">
                    <div class="card h-100 border-success">
                        <div class="card-body">
                            <h6 class="card-title">${field.replace(/_/g, " ")}</h6>
                            <p class="card-text">${displayValue}</p>
                        </div>
                    </div>
                </div>
            `);
            $container.append($card);
        });
    });
}

