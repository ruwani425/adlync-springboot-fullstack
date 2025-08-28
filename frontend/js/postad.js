const categories = [
    {
        id: 'vehicles',
        name: 'Vehicles',
        icon: 'bi bi-car-front',
        color: 'bg-soft-blue',
        description: 'Cars, Bikes, Trucks, Auto Parts'
    },
    {
        id: 'animals',
        name: 'Animals',
        icon: 'fa-solid fa-paw',
        color: 'bg-soft-pink',
        description: 'Pets, Livestock, Pet Accessories'
    },
    {
        id: 'electronics',
        name: 'Electronics',
        icon: 'bi bi-phone',
        color: 'bg-soft-purple',
        description: 'Phones, Laptops, TVs, Gadgets'
    },
    {
        id: 'properties',
        name: 'Properties',
        icon: 'bi bi-house-door',
        color: 'bg-soft-orange',
        description: 'Houses, Apartments, Land, Commercial'
    },
    {
        id: 'jobs',
        name: 'Jobs',
        icon: 'bi bi-briefcase',
        color: 'bg-soft-green',
        description: 'Full-time, Part-time, Freelance'
    },
    {
        id: 'services',
        name: 'Services',
        icon: 'bi bi-wrench-adjustable-circle',
        color: 'bg-soft-rose',
        description: 'Home Services, Repair, Professional'
    },
    {
        id: 'sports',
        name: 'Sports',
        icon: 'bi bi-trophy',
        color: 'bg-soft-indigo',
        description: 'Equipment, Accessories, Fitness'
    },
    {
        id: 'agriculture',
        name: 'Agriculture',
        icon: 'bi bi-tree',
        color: 'bg-soft-amber',
        description: 'Farming, Seeds, Equipment, Produce'
    },
    {
        id: 'kids',
        name: 'Kids',
        icon: 'bi bi-basket2',
        color: 'bg-soft-teal',
        description: 'Toys, Clothing, Baby Items, Education'
    },
    {
        id: 'fashion',
        name: 'Fashion & Beauty',
        icon: 'bi bi-bag',
        color: 'bg-soft-cyan',
        description: 'Clothing, Accessories, Cosmetics'
    },
    {
        id: 'entertainment',
        name: 'Entertainment',
        icon: 'bi bi-universal-access',
        color: 'bg-soft-violet',
        description: 'Movies, Music, Games, Events'
    },
    {
        id: 'education',
        name: 'Education',
        icon: 'bi bi-mortarboard',
        color: 'bg-soft-sky',
        description: 'Books, Courses, Tutoring, Supplies'
    },
    {
        id: 'mobile',
        name: 'Mobile',
        icon: 'bi bi-phone',
        color: 'bg-soft-lime',
        description: 'Phones, Accessories, Plans, Repair'
    },
    {
        id: 'work_overseas',
        name: 'Work Overseas',
        icon: 'bi bi-globe',
        color: 'bg-soft-red',
        description: 'International Jobs, Visa Services'
    },
    {
        id: 'home_garden',
        name: 'Home & Garden',
        icon: 'bi bi-house-door',
        color: 'bg-soft-yellow',
        description: 'Furniture, Plants, Tools, Decor'
    },
    {
        id: 'essentials',
        name: 'Essentials',
        icon: 'bi bi-basket',
        color: 'bg-soft-gray',
        description: 'Food, Health, Daily Needs'
    }
];

let currentPage = 1;
const itemsPerPage = 12;
let selectedCategory = '';

function initializeCategories() {
    renderCategories();
    updatePaginationControls();
}

function renderCategories() {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const currentCategories = categories.slice(startIndex, endIndex);

    let html = `<div class="row g-4">`;
    $.each(currentCategories, function (_, category) {
        html += `
            <div class="col-md-6 col-lg-4 col-xl-3">
                <div class="card card-hover h-100 text-center p-4 category-card" data-category="${category.id}">
                    <div class="icon-pill ${category.color} mx-auto mb-3">
                        <i class="${category.icon}"></i>
                    </div>
                    <h6 class="mb-1">${category.name}</h6>
                    <small class="text-muted">${category.description}</small>
                </div>
            </div>
        `;
    });
    html += `</div>`;

    $("#categoryGrid").html(html);

    $(".category-card").on("click", function () {
        $(".category-card").removeClass("selected");
        $(this).addClass("selected");
        selectedCategory = $(this).data("category");

        $("#continueBtn").prop("disabled", false);
    });
}

function updatePaginationControls() {
    const totalPages = Math.ceil(categories.length / itemsPerPage);

    $("#paginationInfo").text(`Page ${currentPage} of ${totalPages}`);
    $("#prevPage").prop("disabled", currentPage === 1);
    $("#nextPage").prop("disabled", currentPage === totalPages);
}

function resetSelection() {
    selectedCategory = '';
    $("#continueBtn").prop("disabled", true);
}

$(document).ready(function () {
    initializeCategories();

    $("#prevPage").on("click", function () {
        if (currentPage > 1) {
            currentPage--;
            renderCategories();
            updatePaginationControls();
            resetSelection();
        }
    });

    $("#nextPage").on("click", function () {
        const totalPages = Math.ceil(categories.length / itemsPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            renderCategories();
            updatePaginationControls();
            resetSelection();
        }
    });

    $("#continueBtn").on("click", function () {
        if (selectedCategory) {
            $("#categorySection").hide();
            $("#formSection").addClass("show");

            $("#step1").removeClass("active").addClass("completed");
            $("#step2").addClass("active");

            generateDynamicForm(selectedCategory);
        }
    });

    $("#backBtn").on("click", function () {
        $("#formSection").removeClass("show");
        $("#categorySection").show();

        $("#step1").addClass("active").removeClass("completed");
        $("#step2").removeClass("active");
    });

    $("#submitBtn").on("click", function () {
        const form = $("#dynamicForm")[0];
        const formData = new FormData(form);

        let isValid = true;
        $(form).find("[required]").each(function () {
            if (!$(this).val().trim()) {
                $(this).addClass("is-invalid");
                isValid = false;
            } else {
                $(this).removeClass("is-invalid");
            }
        });

        if (isValid) {
            const formDataObj = Object.fromEntries(formData);
            formDataObj["category"] = selectedCategory;
            localStorage.setItem("adFormData", JSON.stringify(formDataObj));
            window.location.href="../pages/paymentform.html"
            console.log("Form data with category:", formDataObj);

            $("#step2").removeClass("active").addClass("completed");
            $("#step3").addClass("active");
        } else {
            alert("Please fill in all required fields.");
        }
    });
});

function generateDynamicForm(categoryId) {
    const categoryData = categories.find(cat => cat.id === categoryId);

    // Update the category badge
    $("#selectedCategoryBadge").html(`
        <div class="category-badge">
            <i class="${categoryData.icon}"></i>
            ${categoryData.name}
        </div>
    `);

    let formHTML = '';

    // Common fields for all categories
    const commonFields = `
        <div class="row mb-3">
            <div class="col-md-8">
                <label class="form-label" for="title">Title *</label>
                <input class="form-control" id="title" name="title" placeholder="Enter a descriptive title for your ad" required 
                       type="text">
            </div>
            <div class="col-md-4">
                <label class="form-label" for="price">Price (LKR) *</label>
                <input class="form-control" id="price" min="0" name="price" placeholder="0.00" 
                       required step="0.01" type="number">
            </div>
        </div>
        
        <div class="mb-3">
            <label class="form-label" for="description">Description *</label>
            <textarea class="form-control" id="description" name="description" placeholder="Provide detailed information about your item" required
                      rows="4"></textarea>
        </div>
        
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="location">District *</label>
                <input class="form-control" id="location" name="location" placeholder="e.g .,Galle" required 
                       type="text">
            </div>
             <div class="col-md-6">
                <label class="form-label" for="location">City *</label>
                <input class="form-control" id="location" name="location" placeholder="e.g .,Wanduramba" required 
                       type="text">
            </div>
        </div>
        
        <div class="row mb-3">
         <div class="col-md-6">
                <label class="form-label" for="location">Address *</label>
                <input class="form-control" id="location" name="location" placeholder="enter your address here" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="contact">Contact Number *</label>
                <input class="form-control" id="contact" name="contact" placeholder="+94xxxxxxxxx" required 
                       type="tel">
            </div>
        </div>
        
        <div class="mb-3">
            <label class="form-label" for="images">Images</label>
            <input accept="image/*" class="form-control" id="images" multiple name="images" type="file">
            <small class="text-muted">Upload up to 5 images (Max 5MB each)</small>
        </div>
    `;

    // Category-specific fields
    switch (categoryId) {
        case 'vehicles':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Vehicle Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="vehicle_type">Vehicle Type *</label>
                        <select class="form-select" id="vehicle_type" name="vehicle_type" required>
                            <option value="">Select Vehicle Type</option>
                            <option value="car">Car</option>
                            <option value="bike">Bike</option>
                            <option value="van">Van</option>
                            <option value="truck">Truck</option>
                            <option value="bus">Bus</option>
                            <option value="three_wheeler">Three Wheeler</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="make">Make/Brand *</label>
                        <input type="text" class="form-control" id="make" name="make" required 
                               placeholder="e.g., Toyota, Honda">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="model">Model *</label>
                        <input type="text" class="form-control" id="model" name="model" required 
                               placeholder="e.g., Corolla, Civic">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="year">Year *</label>
                        <input type="number" class="form-control" id="year" name="year" required 
                               min="1900" max="2025" placeholder="2020">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="mileage">Mileage (km)</label>
                        <input type="number" class="form-control" id="mileage" name="mileage" 
                               placeholder="Enter mileage">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="fuel_type">Fuel Type *</label>
                        <select class="form-select" id="fuel_type" name="fuel_type" required>
                            <option value="">Select Fuel Type</option>
                            <option value="petrol">Petrol</option>
                            <option value="diesel">Diesel</option>
                            <option value="electric">Electric</option>
                            <option value="hybrid">Hybrid</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="transmission">Transmission *</label>
                        <select class="form-select" id="transmission" name="transmission" required>
                            <option value="">Select Transmission</option>
                            <option value="manual">Manual</option>
                            <option value="automatic">Automatic</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="condition">Condition *</label>
                        <select class="form-select" id="condition" name="condition" required>
                            <option value="">Select Condition</option>
                            <option value="brand_new">Brand New</option>
                            <option value="excellent">Excellent</option>
                            <option value="good">Good</option>
                            <option value="fair">Fair</option>
                            <option value="needs_work">Needs Work</option>
                        </select>
                    </div>
                </div>
            `;
            break;


        case 'agriculture':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Agriculture Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="product_type">Product Type *</label>
                        <select class="form-select" id="product_type" name="product_type" required>
                            <option value="">Select Product Type</option>
                            <option value="seeds">Seeds</option>
                            <option value="fertilizer">Fertilizer</option>
                            <option value="equipment">Equipment</option>
                            <option value="produce">Produce</option>
                            <option value="plants">Plants</option>
                            <option value="pesticides">Pesticides</option>
                            <option value="tools">Tools</option>
                            <option value="livestock_feed">Livestock Feed</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="quantity">Quantity *</label>
                        <input type="text" class="form-control" id="quantity" name="quantity" required 
                               placeholder="e.g., 50kg, 100 pieces, 10 liters">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="season">Season *</label>
                        <select class="form-select" id="season" name="season" required>
                            <option value="">Select Season</option>
                            <option value="yala">Yala Season (May-September)</option>
                            <option value="maha">Maha Season (October-March)</option>
                            <option value="all_year">All Year Round</option>
                            <option value="dry_season">Dry Season</option>
                            <option value="wet_season">Wet Season</option>
                            <option value="monsoon">Monsoon Season</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="condition">Condition *</label>
                        <select class="form-select" id="condition" name="condition" required>
                            <option value="">Select Condition</option>
                            <option value="fresh">Fresh</option>
                            <option value="dry">Dry</option>
                            <option value="frozen">Frozen</option>
                            <option value="processed">Processed</option>
                            <option value="raw">Raw</option>
                            <option value="organic">Organic</option>
                            <option value="in_storage">In Storage</option>
                            <option value="ready_to_sell">Ready to Sell</option>
                            <option value="brand_new">Brand New</option>
                            <option value="used_good">Used (Good Condition)</option>
                            <option value="refurbished">Refurbished</option>
                            <option value="needs_repair">Needs Repair</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="variety">Variety/Brand</label>
                        <input type="text" class="form-control" id="variety" name="variety" 
                               placeholder="e.g., BG 300, Red Lady, Basmati">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="harvest_date">Harvest/Production Date</label>
                        <input type="date" class="form-control" id="harvest_date" name="harvest_date">
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="certifications">Certifications</label>
                    <input type="text" class="form-control" id="certifications" name="certifications" 
                           placeholder="e.g., Organic Certified, GAP Certified">
                </div>
            `;
            break;

        case 'mobile':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Mobile Phone Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand *</label>
                <input class="form-control" id="brand" name="brand" placeholder="e.g., Apple, Samsung, Huawei, Xiaomi" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="model">Model</label>
                <input class="form-control" id="model" name="model" placeholder="e.g., iPhone 14, Galaxy S23, P40 Pro" 
                       type="text">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label" for="storage">Storage *</label>
                <select class="form-select" id="storage" name="storage" required>
                    <option value="">Select Storage</option>
                    <option value="16GB">16GB</option>
                    <option value="32GB">32GB</option>
                    <option value="64GB">64GB</option>
                    <option value="128GB">128GB</option>
                    <option value="256GB">256GB</option>
                    <option value="512GB">512GB</option>
                    <option value="1TB">1TB</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="ram">RAM *</label>
                <select class="form-select" id="ram" name="ram" required>
                    <option value="">Select RAM</option>
                    <option value="1GB">1GB</option>
                    <option value="2GB">2GB</option>
                    <option value="3GB">3GB</option>
                    <option value="4GB">4GB</option>
                    <option value="6GB">6GB</option>
                    <option value="8GB">8GB</option>
                    <option value="12GB">12GB</option>
                    <option value="16GB">16GB</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="brand_new">Brand New</option>
                    <option value="like_new">Like New</option>
                    <option value="excellent">Excellent</option>
                    <option value="good">Good</option>
                    <option value="fair">Fair</option>
                    <option value="poor">Poor</option>
                    <option value="damaged">Damaged</option>
                    <option value="for_parts">For Parts</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="warranty">Warranty Status *</label>
                <select class="form-select" id="warranty" name="warranty" required>
                    <option value="">Select Warranty Status</option>
                    <option value="under_warranty">Under Warranty</option>
                    <option value="warranty_expired">Warranty Expired</option>
                    <option value="no_warranty">No Warranty</option>
                    <option value="extended_warranty">Extended Warranty</option>
                    <option value="international_warranty">International Warranty</option>
                    <option value="local_warranty">Local Warranty</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="color">Color</label>
                <input class="form-control" id="color" name="color" placeholder="e.g., Black, White, Gold, Blue" 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="accessories">Included Accessories</label>
                <input class="form-control" id="accessories" name="accessories" placeholder="e.g., Charger, Box, Earphones, Case" 
                       type="text">
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="additional_info">Additional Information</label>
            <textarea class="form-control" id="additional_info" name="additional_info" placeholder="Any defects, special features, or additional details"
                      rows="2"></textarea>
        </div>
    `;
            break;

        case 'work_overseas':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Overseas Work Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="position">Position *</label>
                <input type="text" class="form-control" id="position" name="position" required 
                       placeholder="e.g., Software Engineer, Nurse, Construction Worker">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="country">Country *</label>
                <select class="form-select" id="country" name="country" required>
                    <option value="">Select Country</option>
                    <option value="saudi_arabia">Saudi Arabia</option>
                    <option value="uae">United Arab Emirates</option>
                    <option value="qatar">Qatar</option>
                    <option value="kuwait">Kuwait</option>
                    <option value="oman">Oman</option>
                    <option value="bahrain">Bahrain</option>
                    <option value="singapore">Singapore</option>
                    <option value="malaysia">Malaysia</option>
                    <option value="australia">Australia</option>
                    <option value="new_zealand">New Zealand</option>
                    <option value="canada">Canada</option>
                    <option value="uk">United Kingdom</option>
                    <option value="usa">United States</option>
                    <option value="germany">Germany</option>
                    <option value="italy">Italy</option>
                    <option value="south_korea">South Korea</option>
                    <option value="japan">Japan</option>
                    <option value="maldives">Maldives</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="salary">Salary *</label>
                <input type="text" class="form-control" id="salary" name="salary" required 
                       placeholder="e.g., $2000/month, SAR 5000/month, Negotiable">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="contract_duration">Contract Duration *</label>
                <select class="form-select" id="contract_duration" name="contract_duration" required>
                    <option value="">Select Duration</option>
                    <option value="6_months">6 Months</option>
                    <option value="1_year">1 Year</option>
                    <option value="2_years">2 Years</option>
                    <option value="3_years">3 Years</option>
                    <option value="permanent">Permanent</option>
                    <option value="renewable">Renewable Contract</option>
                    <option value="project_based">Project Based</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="requirements">Requirements *</label>
            <textarea class="form-control" id="requirements" name="requirements" rows="4" required
                      placeholder="List qualifications, experience, skills, language requirements, certifications needed"></textarea>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="company_name">Company/Agency Name</label>
                <input type="text" class="form-control" id="company_name" name="company_name" 
                       placeholder="Name of hiring company or recruitment agency">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="visa_status">Visa Status</label>
                <select class="form-select" id="visa_status" name="visa_status">
                    <option value="">Select Visa Status</option>
                    <option value="company_sponsored">Company Sponsored</option>
                    <option value="own_visa">Own Visa Required</option>
                    <option value="work_permit_provided">Work Permit Provided</option>
                    <option value="free_visa">Free Visa</option>
                    <option value="visa_on_arrival">Visa on Arrival</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="accommodation">Accommodation</label>
                <select class="form-select" id="accommodation" name="accommodation">
                    <option value="">Select Accommodation</option>
                    <option value="provided">Provided by Company</option>
                    <option value="allowance">Accommodation Allowance</option>
                    <option value="own_arrangement">Own Arrangement</option>
                    <option value="shared">Shared Accommodation</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="benefits">Additional Benefits</label>
                <input type="text" class="form-control" id="benefits" name="benefits" 
                       placeholder="e.g., Medical, Transport, Food, Annual Leave">
            </div>
        </div>
    `;
            break;

        case 'essentials':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Essentials Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand *</label>
                <input type="text" class="form-control" id="brand" name="brand" required 
                       placeholder="e.g., Nestlé, Unilever, Keells">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="quantity">Quantity *</label>
                <input type="text" class="form-control" id="quantity" name="quantity" required 
                       placeholder="e.g., 1kg, 500ml, 12 pieces">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="expiry_date">Expiry Date</label>
                <input type="date" class="form-control" id="expiry_date" name="expiry_date">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="fresh">Fresh</option>
                    <option value="sealed">Sealed/Unopened</option>
                    <option value="opened">Opened</option>
                    <option value="near_expiry">Near Expiry</option>
                    <option value="frozen">Frozen</option>
                    <option value="dry">Dry Goods</option>
                    <option value="organic">Organic</option>
                    <option value="processed">Processed</option>
                    <option value="bulk_pack">Bulk Pack</option>
                    <option value="sample_size">Sample Size</option>
                </select>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="product_type">Product Type</label>
            <select class="form-select" id="product_type" name="product_type">
                <option value="">Select Product Type</option>
                <option value="food_beverages">Food & Beverages</option>
                <option value="personal_care">Personal Care</option>
                <option value="household">Household Items</option>
                <option value="health_medicine">Health & Medicine</option>
                <option value="baby_care">Baby Care</option>
                <option value="cleaning_supplies">Cleaning Supplies</option>
                <option value="groceries">Groceries</option>
                <option value="snacks">Snacks</option>
                <option value="dairy">Dairy Products</option>
                <option value="meat_seafood">Meat & Seafood</option>
                <option value="fruits_vegetables">Fruits & Vegetables</option>
                <option value="bakery">Bakery Items</option>
                <option value="other">Other</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label" for="storage_instructions">Storage Instructions</label>
            <textarea class="form-control" id="storage_instructions" name="storage_instructions" rows="2"
                      placeholder="Storage requirements, temperature conditions, special handling instructions"></textarea>
        </div>
    `;
            break;

        case 'home_garden':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Home & Garden Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="item_type">Item Type *</label>
                <select class="form-select" id="item_type" name="item_type" required>
                    <option value="">Select Item Type</option>
                    <option value="furniture">Furniture</option>
                    <option value="appliances">Appliances</option>
                    <option value="kitchen_items">Kitchen Items</option>
                    <option value="bathroom_items">Bathroom Items</option>
                    <option value="decor">Home Decor</option>
                    <option value="lighting">Lighting</option>
                    <option value="bedding">Bedding & Linens</option>
                    <option value="curtains">Curtains & Blinds</option>
                    <option value="carpets">Carpets & Rugs</option>
                    <option value="plants">Plants</option>
                    <option value="garden_tools">Garden Tools</option>
                    <option value="outdoor_furniture">Outdoor Furniture</option>
                    <option value="pots_planters">Pots & Planters</option>
                    <option value="garden_supplies">Garden Supplies</option>
                    <option value="storage">Storage Solutions</option>
                    <option value="hardware">Hardware & Tools</option>
                    <option value="safety_security">Safety & Security</option>
                    <option value="cleaning_supplies">Cleaning Supplies</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="material">Material *</label>
                <select class="form-select" id="material" name="material" required>
                    <option value="">Select Material</option>
                    <option value="wood">Wood</option>
                    <option value="metal">Metal</option>
                    <option value="plastic">Plastic</option>
                    <option value="glass">Glass</option>
                    <option value="fabric">Fabric</option>
                    <option value="leather">Leather</option>
                    <option value="ceramic">Ceramic</option>
                    <option value="stone">Stone</option>
                    <option value="concrete">Concrete</option>
                    <option value="bamboo">Bamboo</option>
                    <option value="rattan">Rattan</option>
                    <option value="stainless_steel">Stainless Steel</option>
                    <option value="aluminum">Aluminum</option>
                    <option value="composite">Composite</option>
                    <option value="mixed_materials">Mixed Materials</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="dimensions">Dimensions *</label>
                <input type="text" class="form-control" id="dimensions" name="dimensions" required 
                       placeholder="e.g., 120cm x 80cm x 75cm, L: 2m W: 1m H: 1.5m">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="brand_new">Brand New</option>
                    <option value="like_new">Like New</option>
                    <option value="excellent">Excellent</option>
                    <option value="good">Good</option>
                    <option value="fair">Fair</option>
                    <option value="worn">Worn</option>
                    <option value="needs_repair">Needs Repair</option>
                    <option value="antique">Antique</option>
                    <option value="vintage">Vintage</option>
                    <option value="refurbished">Refurbished</option>
                    <option value="damaged">Damaged</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand</label>
                <input type="text" class="form-control" id="brand" name="brand" 
                       placeholder="e.g., IKEA, Singer, Damro, Abans">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="color">Color</label>
                <input type="text" class="form-control" id="color" name="color" 
                       placeholder="e.g., Brown, White, Black, Natural">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="weight">Weight (if applicable)</label>
                <input type="text" class="form-control" id="weight" name="weight" 
                       placeholder="e.g., 25kg, Heavy, Light">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="assembly">Assembly Required</label>
                <select class="form-select" id="assembly" name="assembly">
                    <option value="">Select Assembly Status</option>
                    <option value="fully_assembled">Fully Assembled</option>
                    <option value="partial_assembly">Partial Assembly Required</option>
                    <option value="full_assembly">Full Assembly Required</option>
                    <option value="no_assembly">No Assembly Needed</option>
                </select>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="features">Special Features</label>
            <textarea class="form-control" id="features" name="features" rows="2"
                      placeholder="Any special features, functionality, or unique characteristics"></textarea>
        </div>
    `;
            break;

        case 'education':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Education Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="course_name">Course Name *</label>
                <input class="form-control" id="course_name" name="course_name" placeholder="e.g., Mathematics Tutoring, Web Development Course" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="institute">Institute/Provider *</label>
                <input class="form-control" id="institute" name="institute" placeholder="e.g., University of Colombo, ABC Institute" required 
                       type="text">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="duration">Duration *</label>
                <input class="form-control" id="duration" name="duration" placeholder="e.g., 6 months, 2 years, 40 hours" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="qualification_offered">Qualification Offered *</label>
                <select class="form-select" id="qualification_offered" name="qualification_offered" required>
                    <option value="">Select Qualification</option>
                    <option value="certificate">Certificate</option>
                    <option value="diploma">Diploma</option>
                    <option value="degree">Degree</option>
                    <option value="masters">Masters</option>
                    <option value="phd">PhD</option>
                    <option value="professional_certification">Professional Certification</option>
                    <option value="skills_training">Skills Training</option>
                    <option value="tutoring">Tutoring/Coaching</option>
                    <option value="workshop">Workshop</option>
                    <option value="seminar">Seminar</option>
                    <option value="online_course">Online Course</option>
                    <option value="none">No Qualification</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="subject_area">Subject Area</label>
                <select class="form-select" id="subject_area" name="subject_area">
                    <option value="">Select Subject Area</option>
                    <option value="mathematics">Mathematics</option>
                    <option value="science">Science</option>
                    <option value="english">English</option>
                    <option value="sinhala">Sinhala</option>
                    <option value="tamil">Tamil</option>
                    <option value="it_computer">IT & Computer</option>
                    <option value="business">Business</option>
                    <option value="accounting">Accounting</option>
                    <option value="engineering">Engineering</option>
                    <option value="medicine">Medicine</option>
                    <option value="law">Law</option>
                    <option value="arts">Arts</option>
                    <option value="music">Music</option>
                    <option value="languages">Languages</option>
                    <option value="vocational">Vocational Training</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="study_mode">Study Mode</label>
                <select class="form-select" id="study_mode" name="study_mode">
                    <option value="">Select Study Mode</option>
                    <option value="online">Online</option>
                    <option value="physical">Physical Classes</option>
                    <option value="hybrid">Hybrid (Online + Physical)</option>
                    <option value="home_tutoring">Home Tutoring</option>
                    <option value="group_classes">Group Classes</option>
                    <option value="one_on_one">One-on-One</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="level">Education Level</label>
                <select class="form-select" id="level" name="level">
                    <option value="">Select Level</option>
                    <option value="primary">Primary (Grade 1-5)</option>
                    <option value="junior_secondary">Junior Secondary (Grade 6-9)</option>
                    <option value="senior_secondary">Senior Secondary (Grade 10-11)</option>
                    <option value="al">Advanced Level</option>
                    <option value="undergraduate">Undergraduate</option>
                    <option value="postgraduate">Postgraduate</option>
                    <option value="professional">Professional</option>
                    <option value="adult_education">Adult Education</option>
                    <option value="all_levels">All Levels</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="schedule">Schedule</label>
                <input class="form-control" id="schedule" name="schedule" placeholder="e.g., Weekdays 6-8 PM, Saturdays 9-12 PM" 
                       type="text">
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="requirements">Requirements/Prerequisites</label>
            <textarea class="form-control" id="requirements" name="requirements" placeholder="Any prerequisites, required qualifications, or materials needed"
                      rows="2"></textarea>
        </div>
    `;
            break;

        case 'entertainment':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Entertainment Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="type">Entertainment Type *</label>
                <select class="form-select" id="type" name="type" required>
                    <option value="">Select Type</option>
                    <option value="movies">Movies</option>
                    <option value="music">Music</option>
                    <option value="games">Games</option>
                    <option value="books">Books</option>
                    <option value="magazines">Magazines</option>
                    <option value="tv_shows">TV Shows</option>
                    <option value="concerts">Concerts</option>
                    <option value="events">Events</option>
                    <option value="theater">Theater</option>
                    <option value="comedy">Comedy</option>
                    <option value="sports_events">Sports Events</option>
                    <option value="festivals">Festivals</option>
                    <option value="workshops">Workshops</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="format">Format *</label>
                <select class="form-select" id="format" name="format" required>
                    <option value="">Select Format</option>
                    <option value="dvd">DVD</option>
                    <option value="blu_ray">Blu-ray</option>
                    <option value="cd">CD</option>
                    <option value="vinyl">Vinyl</option>
                    <option value="digital">Digital</option>
                    <option value="streaming">Streaming</option>
                    <option value="hardcover">Hardcover</option>
                    <option value="paperback">Paperback</option>
                    <option value="ebook">E-book</option>
                    <option value="board_game">Board Game</option>
                    <option value="card_game">Card Game</option>
                    <option value="video_game">Video Game</option>
                    <option value="live_event">Live Event</option>
                    <option value="online_event">Online Event</option>
                    <option value="ticket">Ticket</option>
                    <option value="subscription">Subscription</option>
                    <option value="other">Other</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand/Publisher *</label>
                <input type="text" class="form-control" id="brand" name="brand" required 
                       placeholder="e.g., Sony, Universal, Nintendo, Marvel">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="genre">Genre</label>
                <input type="text" class="form-control" id="genre" name="genre" 
                       placeholder="e.g., Action, Comedy, Rock, Adventure">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="release_year">Release Year</label>
                <input type="number" class="form-control" id="release_year" name="release_year" 
                       min="1900" max="2025" placeholder="2023">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="rating">Rating</label>
                <select class="form-select" id="rating" name="rating">
                    <option value="">Select Rating</option>
                    <option value="G">G (General)</option>
                    <option value="PG">PG (Parental Guidance)</option>
                    <option value="PG13">PG-13</option>
                    <option value="R">R (Restricted)</option>
                    <option value="NC17">NC-17</option>
                    <option value="E">E (Everyone)</option>
                    <option value="E10">E10+ (Everyone 10+)</option>
                    <option value="T">T (Teen)</option>
                    <option value="M">M (Mature)</option>
                    <option value="AO">AO (Adults Only)</option>
                    <option value="not_rated">Not Rated</option>
                </select>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="artist_author">Artist/Author/Creator</label>
            <input type="text" class="form-control" id="artist_author" name="artist_author" 
                   placeholder="Name of artist, author, director, or creator">
        </div>
    `;
            break;

        case 'fashion':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Fashion & Beauty Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="item_type">Item Type *</label>
                <select class="form-select" id="item_type" name="item_type" required>
                    <option value="">Select Item Type</option>
                    <option value="clothing">Clothing</option>
                    <option value="shoes">Shoes</option>
                    <option value="bags">Bags</option>
                    <option value="accessories">Accessories</option>
                    <option value="jewelry">Jewelry</option>
                    <option value="watches">Watches</option>
                    <option value="cosmetics">Cosmetics</option>
                    <option value="skincare">Skincare</option>
                    <option value="haircare">Hair Care</option>
                    <option value="perfume">Perfume</option>
                    <option value="sunglasses">Sunglasses</option>
                    <option value="belts">Belts</option>
                    <option value="hats">Hats</option>
                    <option value="scarves">Scarves</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand *</label>
                <input type="text" class="form-control" id="brand" name="brand" required 
                       placeholder="e.g., Nike, Zara, L'Oreal, Chanel">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label" for="size">Size *</label>
                <select class="form-select" id="size" name="size" required>
                    <option value="">Select Size</option>
                    <option value="XS">XS</option>
                    <option value="S">S</option>
                    <option value="M">M</option>
                    <option value="L">L</option>
                    <option value="XL">XL</option>
                    <option value="XXL">XXL</option>
                    <option value="XXXL">XXXL</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                    <option value="one_size">One Size</option>
                    <option value="free_size">Free Size</option>
                    <option value="custom">Custom Size</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="gender">Gender *</label>
                <select class="form-select" id="gender" name="gender" required>
                    <option value="">Select Gender</option>
                    <option value="men">Men</option>
                    <option value="women">Women</option>
                    <option value="unisex">Unisex</option>
                    <option value="kids">Kids</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="brand_new">Brand New</option>
                    <option value="new_with_tags">New with Tags</option>
                    <option value="like_new">Like New</option>
                    <option value="excellent">Excellent</option>
                    <option value="good">Good</option>
                    <option value="fair">Fair</option>
                    <option value="vintage">Vintage</option>
                    <option value="damaged">Damaged</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="color">Color</label>
                <input type="text" class="form-control" id="color" name="color" 
                       placeholder="e.g., Black, Red, Multi-color">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="material">Material</label>
                <input type="text" class="form-control" id="material" name="material" 
                       placeholder="e.g., Cotton, Leather, Silk, Synthetic">
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="style_notes">Style Notes</label>
            <textarea class="form-control" id="style_notes" name="style_notes" rows="2"
                      placeholder="Additional style details, fit information, or special features"></textarea>
        </div>
    `;
            break;

        case 'kids':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Kids Item Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="item_type">Item Type *</label>
                <select class="form-select" id="item_type" name="item_type" required>
                    <option value="">Select Item Type</option>
                    <option value="toys">Toys</option>
                    <option value="clothing">Clothing</option>
                    <option value="shoes">Shoes</option>
                    <option value="baby_gear">Baby Gear</option>
                    <option value="stroller">Stroller</option>
                    <option value="car_seat">Car Seat</option>
                    <option value="high_chair">High Chair</option>
                    <option value="crib">Crib</option>
                    <option value="books">Books</option>
                    <option value="educational">Educational Materials</option>
                    <option value="sports_equipment">Sports Equipment</option>
                    <option value="games">Games</option>
                    <option value="electronics">Electronics</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="age_range">Age Range *</label>
                <select class="form-select" id="age_range" name="age_range" required>
                    <option value="">Select Age Range</option>
                    <option value="0-6_months">0-6 Months</option>
                    <option value="6-12_months">6-12 Months</option>
                    <option value="1-2_years">1-2 Years</option>
                    <option value="2-3_years">2-3 Years</option>
                    <option value="3-5_years">3-5 Years</option>
                    <option value="5-8_years">5-8 Years</option>
                    <option value="8-12_years">8-12 Years</option>
                    <option value="12+_years">12+ Years</option>
                    <option value="all_ages">All Ages</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand *</label>
                <input class="form-control" id="brand" name="brand" placeholder="e.g., Fisher-Price, LEGO, Carter's" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="brand_new">Brand New</option>
                    <option value="like_new">Like New</option>
                    <option value="excellent">Excellent</option>
                    <option value="good">Good</option>
                    <option value="fair">Fair</option>
                    <option value="worn">Worn</option>
                    <option value="needs_repair">Needs Repair</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="size">Size (if applicable)</label>
                <input class="form-control" id="size" name="size" placeholder="e.g., 2T, Size 5, Medium" 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="gender">Gender</label>
                <select class="form-select" id="gender" name="gender">
                    <option value="">Select Gender</option>
                    <option value="boys">Boys</option>
                    <option value="girls">Girls</option>
                    <option value="unisex">Unisex</option>
                </select>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="safety_info">Safety Information</label>
            <textarea class="form-control" id="safety_info" name="safety_info" placeholder="Any safety warnings, choking hazards, or age restrictions"
                      rows="2"></textarea>
        </div>
    `;
            break;
        case 'animals':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Animal Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="animal_type">Animal Type *</label>
                        <select class="form-select" id="animal_type" name="animal_type" required>
                            <option value="">Select Animal Type</option>
                            <option value="dog">Dog</option>
                            <option value="cat">Cat</option>
                            <option value="bird">Bird</option>
                            <option value="fish">Fish</option>
                            <option value="livestock">Livestock</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="breed">Breed</label>
                        <input type="text" class="form-control" id="breed" name="breed" 
                               placeholder="Enter breed if known">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="age">Age</label>
                        <input type="text" class="form-control" id="age" name="age" 
                               placeholder="e.g., 2 years, 6 months">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="gender">Gender</label>
                        <select class="form-select" id="gender" name="gender">
                            <option value="">Select Gender</option>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="vaccinated">Vaccination Status</label>
                    <select class="form-select" id="vaccinated" name="vaccinated">
                        <option value="">Select Status</option>
                        <option value="yes">Vaccinated</option>
                        <option value="no">Not Vaccinated</option>
                        <option value="partial">Partially Vaccinated</option>
                    </select>
                </div>
            `;
            break;

        case 'electronics':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Electronic Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="electronic_type">Electronic Type *</label>
                        <select class="form-select" id="electronic_type" name="electronic_type" required>
                            <option value="">Select Type</option>
                            <option value="mobile">Mobile Phone</option>
                            <option value="laptop">Laptop</option>
                            <option value="desktop">Desktop</option>
                            <option value="tablet">Tablet</option>
                            <option value="tv">Television</option>
                            <option value="audio">Audio Equipment</option>
                            <option value="camera">Camera</option>
                            <option value="gaming">Gaming</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="brand">Brand *</label>
                        <input type="text" class="form-control" id="brand" name="brand" required 
                               placeholder="e.g., Apple, Samsung">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="model">Model</label>
                        <input type="text" class="form-control" id="model" name="model" 
                               placeholder="Enter model name/number">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="warranty">Warranty Status</label>
                        <select class="form-select" id="warranty" name="warranty">
                            <option value="">Select Warranty Status</option>
                            <option value="yes">Under Warranty</option>
                            <option value="no">No Warranty</option>
                            <option value="expired">Warranty Expired</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="condition">Condition *</label>
                        <select class="form-select" id="condition" name="condition" required>
                            <option value="">Select Condition</option>
                            <option value="brand_new">Brand New</option>
                            <option value="excellent">Excellent</option>
                            <option value="good">Good</option>
                            <option value="fair">Fair</option>
                            <option value="for_parts">For Parts</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="accessories">Included Accessories</label>
                        <input type="text" class="form-control" id="accessories" name="accessories" 
                               placeholder="e.g., Charger, Box, Earphones">
                    </div>
                </div>
            `;
            break;

        case 'properties':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Property Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="property_type">Property Type *</label>
                        <select class="form-select" id="property_type" name="property_type" required>
                            <option value="">Select Property Type</option>
                            <option value="house">House</option>
                            <option value="apartment">Apartment</option>
                            <option value="land">Land</option>
                            <option value="commercial">Commercial</option>
                            <option value="office">Office Space</option>
                            <option value="warehouse">Warehouse</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="listing_type">Listing Type *</label>
                        <select class="form-select" id="listing_type" name="listing_type" required>
                            <option value="">Select Listing Type</option>
                            <option value="sale">For Sale</option>
                            <option value="rent">For Rent</option>
                            <option value="lease">For Lease</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-4">
                        <label class="form-label" for="bedrooms">Bedrooms</label>
                        <input class="form-control" id="bedrooms" min="0" name="bedrooms" 
                               placeholder="0" type="number">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="bathrooms">Bathrooms</label>
                        <input class="form-control" id="bathrooms" min="0" name="bathrooms" 
                               placeholder="0" type="number">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="area">Area (sq ft)</label>
                        <input class="form-control" id="area" name="area" placeholder="Square feet" 
                               type="number">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="furnishing">Furnishing Status</label>
                        <select class="form-select" id="furnishing" name="furnishing">
                            <option value="">Select Furnishing</option>
                            <option value="furnished">Fully Furnished</option>
                            <option value="semi_furnished">Semi Furnished</option>
                            <option value="unfurnished">Unfurnished</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="parking">Parking</label>
                        <select class="form-select" id="parking" name="parking">
                            <option value="">Select Parking</option>
                            <option value="COVERED">Covered</option>
                            <option value="UNCOVERED">Uncovered</option>
                            <option value="GARAGE">Garage</option>
                            <option value="STREET">Street</option>
                            <option value="VALET">Valet</option>
                            <option value="SECURE_LOT">Secure Lot</option>
                            <option value="NOT_AVAILABLE">Not Available</option>
                        </select>
                    </div>
                </div>
            `;
            break;

        case 'jobs':
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Job Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="job_type">Job Type *</label>
                        <select class="form-select" id="job_type" name="job_type" required>
                            <option value="">Select Job Type</option>
                            <option value="FULL_TIME">Full Time</option>
                            <option value="PART_TIME">Part Time</option>
                            <option value="CONTRACT">Contract</option>
                            <option value="TEMPORARY">Temporary</option>
                            <option value="INTERN">Intern</option>
                            <option value="FREELANCE">Freelance</option>
                            <option value="VOLUNTEER">Volunteer</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="experience_level">Experience Level *</label>
                        <select class="form-select" id="experience_level" name="experience_level" required>
                            <option value="">Select Experience Level</option>
                            <option value="entry">Entry Level</option>
                            <option value="mid">Mid Level</option>
                            <option value="senior">Senior Level</option>
                            <option value="executive">Executive</option>
                        </select>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-12">
                        <label class="form-label" for="position">Position *</label>
                        <input class="form-control" id="position" name="position" placeholder="e.g., Software Engineer, Project Manager" required type="text">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="company">Company Name</label>
                        <input class="form-control" id="company" name="company" placeholder="Enter company name" 
                               type="text">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="industry">Industry</label>
                        <input class="form-control" id="industry" name="industry" placeholder="e.g., IT, Healthcare, Finance" 
                               type="text">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="salary_min">Salary Min (LKR)</label>
                        <input class="form-control" id="salary_min" name="salary_min" placeholder="Minimum salary" 
                               type="number">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="salary_max">Salary Max (LKR)</label>
                        <input class="form-control" id="salary_max" name="salary_max" placeholder="Maximum salary" 
                               type="number">
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="requirements">Requirements</label>
                    <textarea class="form-control" id="requirements" name="requirements" placeholder="List job requirements, skills, qualifications"
                              rows="3"></textarea>
                </div>
            `;
            break;

        case 'services':
            const serviceCommonFields = `
                <div class="mb-3">
                    <label class="form-label" for="title">Service Title *</label>
                    <input class="form-control" id="title" name="title" placeholder="Enter your service title" required 
                           type="text">
                </div>
                
                <div class="mb-3">
                    <label class="form-label" for="description">Service Description *</label>
                    <textarea class="form-control" id="description" name="description" placeholder="Describe your service in detail" required
                              rows="4"></textarea>
                </div>
                
               <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="location">District *</label>
                <input class="form-control" id="location" name="location" placeholder="e.g .,Galle" required 
                       type="text">
            </div>
             <div class="col-md-6">
                <label class="form-label" for="location">City *</label>
                <input class="form-control" id="location" name="location" placeholder="e.g .,Wanduramba" required 
                       type="text">
            </div>
        </div>
        
        <div class="row mb-3">
         <div class="col-md-6">
                <label class="form-label" for="location">Address *</label>
                <input class="form-control" id="location" name="location" placeholder="enter your address here" required 
                       type="text">
            </div>
            <div class="col-md-6">
                <label class="form-label" for="contact">Contact Number *</label>
                <input class="form-control" id="contact" name="contact" placeholder="+94xxxxxxxxx" required 
                       type="tel">
            </div>
        </div>
            `;

            formHTML = serviceCommonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Service Details</h5>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="service_type">Service Type *</label>
                        <select class="form-select" id="service_type" name="service_type" required>
                            <option value="">Select Service Type</option>
                            <option value="home_repair">Home Repair</option>
                            <option value="cleaning">Cleaning</option>
                            <option value="plumbing">Plumbing</option>
                            <option value="electrical">Electrical</option>
                            <option value="gardening">Gardening</option>
                            <option value="tutoring">Tutoring</option>
                            <option value="beauty">Beauty & Wellness</option>
                            <option value="transport">Transport</option>
                            <option value="catering">Catering</option>
                            <option value="photography">Photography</option>
                            <option value="event_planning">Event Planning</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="provider_name">Provider Name *</label>
                        <input type="text" class="form-control" id="provider_name" name="provider_name" required
                               placeholder="Your name or company name">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="charges">Service Charges (LKR) *</label>
                        <input type="text" class="form-control" id="charges" name="charges" required 
                               placeholder="e.g., 5000/hour, 15000/day, Negotiable">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="availability">Availability *</label>
                        <input type="text" class="form-control" id="availability" name="availability" required
                               placeholder="e.g., Mon-Fri 9AM-5PM, Weekends">
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label" for="experience_years">Experience (Years)</label>
                        <input type="number" class="form-control" id="experience_years" name="experience_years" 
                               min="0" placeholder="Years of experience">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="service_area">Service Areas</label>
                        <input type="text" class="form-control" id="service_area" name="service_area" 
                               placeholder="Areas you provide service">
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="qualifications">Qualifications & Certifications</label>
                    <textarea class="form-control" id="qualifications" name="qualifications" rows="2"
                              placeholder="List your relevant qualifications, certifications, or licenses"></textarea>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="images">Service Images</label>
                    <input type="file" class="form-control" id="images" name="images" multiple accept="image/*">
                    <small class="text-muted">Upload images of your work, certificates, or service setup (Max 5MB each)</small>
                </div>
            `;
            break;

        case 'sports':
            formHTML = commonFields + `
        <hr class="my-4">
        <h5 class="mb-3">Sports Equipment Details</h5>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="equipment_type">Equipment Type *</label>
                <select class="form-select" id="equipment_type" name="equipment_type" required>
                    <option value="">Select Equipment Type</option>
                    <option value="fitness">Fitness Equipment</option>
                    <option value="cricket">Cricket Equipment</option>
                    <option value="football">Football Equipment</option>
                    <option value="tennis">Tennis Equipment</option>
                    <option value="badminton">Badminton Equipment</option>
                    <option value="basketball">Basketball Equipment</option>
                    <option value="volleyball">Volleyball Equipment</option>
                    <option value="swimming">Swimming Equipment</option>
                    <option value="cycling">Cycling Equipment</option>
                    <option value="boxing">Boxing Equipment</option>
                    <option value="martial_arts">Martial Arts Equipment</option>
                    <option value="outdoor">Outdoor Sports</option>
                    <option value="gym">Gym Equipment</option>
                    <option value="other">Other Sports Equipment</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="brand">Brand *</label>
                <input type="text" class="form-control" id="brand" name="brand" required 
                       placeholder="e.g., Nike, Adidas, Wilson">
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label" for="condition">Condition *</label>
                <select class="form-select" id="condition" name="condition" required>
                    <option value="">Select Condition</option>
                    <option value="brand_new">Brand New</option>
                    <option value="excellent">Excellent</option>
                    <option value="good">Good</option>
                    <option value="fair">Fair</option>
                    <option value="worn">Worn</option>
                    <option value="needs_repair">Needs Repair</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label" for="size">Size</label>
                <input type="text" class="form-control" id="size" name="size" 
                       placeholder="e.g., L, XL, 42, One Size">
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label" for="additional_info">Additional Information</label>
            <textarea class="form-control" id="additional_info" name="additional_info" rows="2"
                      placeholder="Any additional details about the equipment, usage, included accessories, etc."></textarea>
        </div>
    `;
            break;

        default:
            formHTML = commonFields + `
                <hr class="my-4">
                <h5 class="mb-3">Additional Details</h5>
                <div class="mb-3">
                    <label class="form-label" for="condition">Condition</label>
                    <select class="form-select" id="condition" name="condition">
                        <option value="">Select Condition</option>
                        <option value="brand_new">Brand New</option>
                        <option value="excellent">Excellent</option>
                        <option value="good">Good</option>
                        <option value="fair">Fair</option>
                        <option value="poor">Poor</option>
                        <option value="Fresh">Fresh</option>
                        <option value="dry">Dry</option>
                        <option value="frozen">Frozen</option>
                        <option value="processed">Processed</option>
                        <option value="raw">Raw</option>
                        <option value="organic">Organic</option>
                        <option value="in_storage">In Storage</option>
                        <option value="ready_to_sell">Ready to Sell</option>
                        <option value="new">New</option>
                        <option value="used_good">Used (Good Condition)</option>
                        <option value="refurbished">Refurbished</option>
                        <option value="open_box">Open Box</option>
                        <option value="damaged">Damaged</option>
                        <option value="for_parts">For Parts Only</option>
                        <option value="antique">Antique / Collectible</option>
                        <option value="OPENED">Opened</option>
                        <option value="NEAR_EXPIRY">Near Expiry</option>
                        <option value="EXPIRED">Expired</option>
                    </select>
                </div>
            `;
            break;
    }

    formHTML += `
        <div class="mb-3">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="negotiable" name="negotiable">
                <label class="form-check-label" for="negotiable">
                    Price is negotiable
                </label>
            </div>
        </div>
    `;

    $("#dynamicForm").html(formHTML);
}