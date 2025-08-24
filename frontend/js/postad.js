const categories = [
    { id: 'vehicles', name: 'Vehicles', icon: 'bi bi-car-front', color: 'bg-soft-blue', description: 'Cars, Bikes, Trucks, Auto Parts' },
    { id: 'animals', name: 'Animals', icon: 'fa-solid fa-paw', color: 'bg-soft-pink', description: 'Pets, Livestock, Pet Accessories' },
    { id: 'electronics', name: 'Electronics', icon: 'bi bi-phone', color: 'bg-soft-purple', description: 'Phones, Laptops, TVs, Gadgets' },
    { id: 'properties', name: 'Properties', icon: 'bi bi-house-door', color: 'bg-soft-orange', description: 'Houses, Apartments, Land, Commercial' },
    { id: 'jobs', name: 'Jobs', icon: 'bi bi-briefcase', color: 'bg-soft-green', description: 'Full-time, Part-time, Freelance' },
    { id: 'services', name: 'Services', icon: 'bi bi-wrench-adjustable-circle', color: 'bg-soft-rose', description: 'Home Services, Repair, Professional' },
    { id: 'sports', name: 'Sports', icon: 'bi bi-trophy', color: 'bg-soft-indigo', description: 'Equipment, Accessories, Fitness' },
    { id: 'agriculture', name: 'Agriculture', icon: 'bi bi-tree', color: 'bg-soft-amber', description: 'Farming, Seeds, Equipment, Produce' },
    { id: 'kids', name: 'Kids', icon: 'bi bi-basket2', color: 'bg-soft-teal', description: 'Toys, Clothing, Baby Items, Education' },
    { id: 'fashion', name: 'Fashion & Beauty', icon: 'bi bi-bag', color: 'bg-soft-cyan', description: 'Clothing, Accessories, Cosmetics' },
    { id: 'entertainment', name: 'Entertainment', icon: 'bi bi-universal-access', color: 'bg-soft-violet', description: 'Movies, Music, Games, Events' },
    { id: 'education', name: 'Education', icon: 'bi bi-mortarboard', color: 'bg-soft-sky', description: 'Books, Courses, Tutoring, Supplies' },
    { id: 'mobile', name: 'Mobile', icon: 'bi bi-phone', color: 'bg-soft-lime', description: 'Phones, Accessories, Plans, Repair' },
    { id: 'work_overseas', name: 'Work Overseas', icon: 'bi bi-globe', color: 'bg-soft-red', description: 'International Jobs, Visa Services' },
    { id: 'home_garden', name: 'Home & Garden', icon: 'bi bi-house-door', color: 'bg-soft-yellow', description: 'Furniture, Plants, Tools, Decor' },
    { id: 'essentials', name: 'Essentials', icon: 'bi bi-basket', color: 'bg-soft-gray', description: 'Food, Health, Daily Needs' }
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
    $.each(currentCategories, function(_, category) {
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

    $(".category-card").on("click", function() {
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

$(document).ready(function() {
    initializeCategories();

    $("#prevPage").on("click", function() {
        if (currentPage > 1) {
            currentPage--;
            renderCategories();
            updatePaginationControls();
            resetSelection();
        }
    });

    $("#nextPage").on("click", function() {
        const totalPages = Math.ceil(categories.length / itemsPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            renderCategories();
            updatePaginationControls();
            resetSelection();
        }
    });

    $("#continueBtn").on("click", function() {
        if (selectedCategory) {
            $("#categorySection").hide();
            $("#formSection").addClass("show");

            $("#step1").removeClass("active").addClass("completed");
            $("#step2").addClass("active");

            generateDynamicForm(selectedCategory);
        }
    });

    $("#backBtn").on("click", function() {
        $("#formSection").removeClass("show");
        $("#categorySection").show();

        $("#step1").addClass("active").removeClass("completed");
        $("#step2").removeClass("active");
    });

    $("#submitBtn").on("click", function() {
        const form = $("#dynamicForm")[0];
        const formData = new FormData(form);

        let isValid = true;
        $(form).find("[required]").each(function() {
            if (!$(this).val().trim()) {
                $(this).addClass("is-invalid");
                isValid = false;
            } else {
                $(this).removeClass("is-invalid");
            }
        });

        if (isValid) {
            alert("Advertisement posted successfully!");
            console.log("Form data:", Object.fromEntries(formData));

            $("#step2").removeClass("active").addClass("completed");
            $("#step3").addClass("active");
        } else {
            alert("Please fill in all required fields.");
        }
    });
});
