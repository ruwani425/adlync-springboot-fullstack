$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryName = urlParams.get("categoryName");
    const postId = urlParams.get("postId");

    if (!categoryName || !postId) return;

    const categoryMap = {
        "agriculture": {
            fields: ["product_type", "quantity", "season", "variety", "production_Date", "certifications", "condition"],
            icon: "bi-seedling"
        },
        "animal": {fields: ["species", "breed", "age", "gender", "vaccination_status"], icon: "bi-heart"},
        "education": {
            fields: ["course_name", "institute", "duration", "qualification_offered", "subject_area", "study_mode", "education_level", "schedule", "requirements"],
            icon: "bi-book"
        },
        "electronic": {fields: ["brand", "type", "model", "warranty", "condition", "accessories"], icon: "bi-tv"},
        "entertainment": {
            fields: ["type", "format", "brand", "genre", "release_year", "rating", "creator", "condition"],
            icon: "bi-music-note-beamed"
        },
        "essential": {
            fields: ["brand", "quantity", "expiry_date", "product_type", "storage_instructions", "condition"],
            icon: "bi-bag"
        },
        "fashion and beauty": {
            fields: ["item_type", "brand", "size", "gender", "condition", "color", "material", "style_note"],
            icon: "bi-gem"
        },
        "home and garden": {
            fields: ["item_type", "material", "dimensions", "condition", "brand", "color", "weight", "assembly_required", "special_features"],
            icon: "bi-house"
        },
        "job": {
            fields: ["position", "company", "salary_min", "salary_max", "industry", "job_type", "requirements", "expiriance_level"],
            icon: "bi-briefcase"
        },
        "kids": {
            fields: ["item_type", "age_rang", "brand", "condition", "size", "gender", "safety_information"],
            icon: "bi-emoji-smile"
        },
        "mobile": {
            fields: ["storage", "condition", "warranty_status", "ram", "brand", "model", "colour", "included_accessories", "additional_information"],
            icon: "bi-phone"
        },
        "property": {fields: ["type", "land_size", "bedroom", "barthroom", "furnished"], icon: "bi-building"},
        "service": {
            fields: ["service_type", "provider_name", "availability", "charges", "service_area", "qualifications"],
            icon: "bi-tools"
        },
        "sport": {
            fields: ["equipment_type", "brand", "condition", "size", "additional_information"],
            icon: "bi-basket"
        },
        "vehicle": {
            fields: ["vehicle_type", "mileage", "year", "brand", "model", "fuel_type", "transmission", "condition"],
            icon: "bi-car-front"
        },
        "work over seas": {
            fields: ["position", "country", "salary", "requirements", "contract_duration", "company_or_agency_name", "visa_status", "accommodation", "additional_benefits"],
            icon: "bi-globe"
        }
    };

    const category = categoryMap[categoryName.toLowerCase()];
    if (!category) return console.warn("Category not found");

    updateCategoryDetailsTitle(category.icon, categoryName);

    $.ajax({
        url: `http://localhost:8080/api/posts/post-detail/${postId}`,
        method: "GET",
        success: function (data) {
            function formatDate(isoString) {
                if (!isoString) return "Posted today";
                const date = new Date(isoString);
                const options = {
                    day: '2-digit',
                    month: 'short',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: true
                };
                return date.toLocaleString('en-US', options);
            }


            let adTitle = data.title || "-";
            const conditionRaw = data.common?.[categoryName]?.condition;
            if (conditionRaw) {
                const conditionText = conditionRaw.toLowerCase(); // convert to lowercase
                adTitle += " - " + conditionText + " condition ";
            }
            $("#adTitle").text(adTitle);

            $("#adPrice").text(`Rs. ${data.price?.toLocaleString() || "-"}`);
            $("#postId").text(data.post_id || "-");
            $("#contactNumber").text(data.contact_number || "-");
            $("#postStatus").text(data.status || "-");
            $("#adDescription").html(`<p>${data.description || "-"}</p>`);

            // $("#adStatus").html(`<i class="bi bi-circle-fill me-1"></i>${data.status || "-"}`);
            $("#adCategory").html(`<i class="bi bi-tag me-1"></i>${categoryName}`);
            $("#adCreatedAt").html(`<i class="bi bi-calendar me-1"></i>${formatDate(data.createdAt)}`);
            $("#adType").html(`<i class="bi bi-bookmark me-1"></i>${(data.advertisement_type.type || "-").toLowerCase()}`);

            if (data.user) {
                const seller = data.user;
                $("#sellerNameValue").text(seller.name);
                $("#sellerEmail").attr("href", `mailto:${seller.email}`).text(seller.email);
                let joinDate = seller.joinDate;

                let onlyDate = joinDate.split("T")[0];

                let d = new Date(joinDate);
                let formattedDate = d.getFullYear() + "-" +
                    String(d.getMonth() + 1).padStart(2, '0') + "-" +
                    String(d.getDate()).padStart(2, '0');

                $("#sellerMemberSince").text(onlyDate);
                $("#sellerRating").text("4.8/5");
            }

            renderCategoryFields(data, category.fields, category.icon, categoryName);

            if (data.images && data.images.length > 0) {
                const $indicators = $("#carouselIndicators");
                const $inner = $("#carouselInner");
                $indicators.empty();
                $inner.empty();

                data.images.forEach((img, index) => {
                    const $indicator = $("<button>").attr({
                        type: "button",
                        "data-bs-target": "#carouselImages",
                        "data-bs-slide-to": index
                    });
                    if (index === 0) $indicator.addClass("active");
                    $indicators.append($indicator);

                    const $item = $("<div>").addClass(`carousel-item ${index === 0 ? "active" : ""}`);
                    $item.html(`<img src="${img.image_url}" class="d-block w-100 object-cover" style="height:480px;width:480px" alt="Image ${index + 1}">`);
                    $inner.append($item);
                });
            }

            if (data.location) {
                const $loc = $("#locationDetails");
                $loc.empty();
                $loc.append(`
                    <div class="col-12 mb-2"><strong>Address:</strong><br><span class="text-muted">${data.location.address}</span></div>
                    <div class="col-12 mb-2"><strong>City:</strong><br><span class="text-muted">${data.location.city}</span></div>
                    <div class="col-12 mb-2"><strong>District:</strong><br><span class="text-muted">${data.location.district}</span></div>
                `);
            }
        },
        error: function (xhr) {
            console.error("Error loading post:", xhr);
        }
    });

    function updateCategoryDetailsTitle(iconClass, categoryName) {
        const $titleElement = $("#categoryDetailsTitle");
        const titleText = getTitleForCategory(categoryName);
        $titleElement.html(`<i class="bi ${iconClass} me-2 text-primary-emerald"></i>${titleText}`);
    }

    function getTitleForCategory(categoryName) {
        const titleMap = {
            "agriculture": "Product Details",
            "animal": "Animal Details",
            "education": "Course Details",
            "electronic": "Device Specifications",
            "entertainment": "Item Details",
            "essential": "Product Information",
            "fashion and beauty": "Item Details",
            "home and garden": "Product Specifications",
            "job": "Job Details",
            "kids": "Item Details",
            "mobile": "Phone Specifications",
            "property": "Property Details",
            "service": "Service Information",
            "sport": "Equipment Details",
            "vehicle": "Vehicle Specifications",
            "work over seas": "Job Details"
        };

        return titleMap[categoryName.toLowerCase()] || "Specifications";
    }

    function renderCategoryFields(data, fields, iconClass, categoryName) {
        const $container = $("#categoryDetailsContent");
        $container.empty();

        const categoryData = data.common?.[categoryName] || {};

        fields.forEach(field => {
            const value = categoryData[field] || "-";
            if (!value || field.toLowerCase() === "id" || field.toLowerCase().endsWith("_id")) return;

            const cardHtml = `
                <div class="col-md-4 mb-3">
                    <div class="card h-100 border-success">
                        <div class="card-body">
                            <h6 class="card-title"><i class="bi ${iconClass} me-2"></i>${field.replace(/_/g, " ")}</h6>
                            <p class="card-text">${value}</p>
                        </div>
                    </div>
                </div>`;
            $container.append(cardHtml);
        });
    }
});