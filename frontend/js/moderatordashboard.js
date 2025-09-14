const samplePosts = [
    {
        id: 1,
        title: "iPhone 15 Pro Max - Like New Condition",
        description: "Barely used iPhone 15 Pro Max in excellent condition. Comes with original box and accessories.",
        price: 450000,
        category: "electronics",
        subcategory: "mobile-phones",
        condition: "like-new",
        location: "Colombo",
        district: "Colombo",
        province: "Western",
        status: "pending",
        images: ["https://via.placeholder.com/400x300/059669/fff?text=iPhone+15"],
        userId: 1,
        userName: "John Doe",
        userEmail: "john@example.com",
        createdAt: "2024-01-15T10:30:00Z",
        features: ["256GB Storage", "Blue Color", "Face ID", "Wireless Charging"]
    },
    {
        id: 2,
        title: "Honda Civic 2020 - Excellent Condition",
        description: "Well maintained Honda Civic with full service history. Single owner vehicle.",
        price: 8500000,
        category: "vehicles",
        subcategory: "cars",
        condition: "excellent",
        location: "Kandy",
        district: "Kandy",
        province: "Central",
        status: "approved",
        images: ["https://via.placeholder.com/400x300/059669/fff?text=Honda+Civic"],
        userId: 2,
        userName: "Sarah Wilson",
        userEmail: "sarah@example.com",
        createdAt: "2024-01-14T15:45:00Z",
        features: ["Automatic", "Leather Seats", "Sunroof", "Low Mileage"]
    },
    {
        id: 3,
        title: "Gaming Desktop Setup - RTX 4080",
        description: "High-end gaming desktop with RTX 4080, perfect for gaming and content creation.",
        price: 650000,
        category: "electronics",
        subcategory: "computers",
        condition: "good",
        location: "Gampaha",
        district: "Gampaha",
        province: "Western",
        status: "rejected",
        images: ["https://via.placeholder.com/400x300/dc2626/fff?text=Gaming+PC"],
        userId: 3,
        userName: "Mike Johnson",
        userEmail: "mike@example.com",
        createdAt: "2024-01-13T09:20:00Z",
        features: ["RTX 4080", "32GB RAM", "1TB NVMe SSD", "RGB Lighting"]
    },
    {
        id: 4,
        title: "Luxury Apartment in Colombo 7",
        description: "Modern 3-bedroom apartment with sea view in premium location.",
        price: 45000000,
        category: "real-estate",
        subcategory: "apartments",
        condition: "excellent",
        location: "Colombo 7",
        district: "Colombo",
        province: "Western",
        status: "pending",
        images: ["https://via.placeholder.com/400x300/059669/fff?text=Apartment"],
        userId: 4,
        userName: "Priya Fernando",
        userEmail: "priya@example.com",
        createdAt: "2024-01-12T14:10:00Z",
        features: ["3 Bedrooms", "Sea View", "Parking", "24/7 Security"]
    },
    {
        id: 5,
        title: "Designer Handbag Collection",
        description: "Authentic designer handbags in excellent condition. Various brands available.",
        price: 75000,
        category: "fashion",
        subcategory: "accessories",
        condition: "like-new",
        location: "Negombo",
        district: "Gampaha",
        province: "Western",
        status: "approved",
        images: ["https://via.placeholder.com/400x300/059669/fff?text=Handbags"],
        userId: 5,
        userName: "Emma Silva",
        userEmail: "emma@example.com",
        createdAt: "2024-01-11T11:30:00Z",
        features: ["Authentic", "Multiple Brands", "Original Tags", "Certificate"]
    },
    {
        id: 6,
        title: "Mountain Bike - Trek X-Caliber",
        description: "Professional mountain bike in great condition. Perfect for trails and adventures.",
        price: 180000,
        category: "sports",
        subcategory: "bicycles",
        condition: "good",
        location: "Matara",
        district: "Matara",
        province: "Southern",
        status: "pending",
        images: ["https://via.placeholder.com/400x300/059669/fff?text=Mountain+Bike"],
        userId: 6,
        userName: "David Perera",
        userEmail: "david@example.com",
        createdAt: "2024-01-10T16:45:00Z",
        features: ["29 inch wheels", "21 Speed", "Disc Brakes", "Aluminum Frame"]
    }
];

let filteredPosts = [...samplePosts];
let currentPost = null;


$(document).ready(function () {
    loadPosts();
    updateStats();

    // Sidebar navigation click
    $('.nav-link').on('click', function (e) {
        e.preventDefault();
        showSection($(this).attr('onclick').match(/'(\w+)'/)[1], this);
    });

    // Filters
    $('#statusFilter, #categoryFilter').on('change', filterPosts);
    $('#searchFilter').on('keyup', filterPosts);
    $('.btn-outline-secondary').on('click', resetFilters);
});

function showSection(sectionName, btn) {
    $('.section').removeClass('active');
    $('#' + sectionName).addClass('active');
    $('.nav-link').removeClass('active');
    $(btn).addClass('active');
    if (sectionName === 'posts') loadPosts();
}

function loadPosts() {
    const grid = $('#postsGrid');
    grid.empty();
    filteredPosts.forEach(post => {
        grid.append(createPostCard(post));
    });
}

function createPostCard(post) {
    const statusClass = `status-${post.status}`;
    const formattedPrice = new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR'
    }).format(post.price);

    const actions = post.status === 'pending' ? `
      <button class="btn btn-success btn-sm" onclick="approvePostDirect(${post.id})">
        <i class="bi bi-check me-1"></i>Approve
      </button>
      <button class="btn btn-danger btn-sm" onclick="rejectPostDirect(${post.id})">
        <i class="bi bi-x me-1"></i>Reject
      </button>
    ` : '';

    return $(`
      <div class="post-card">
        <img src="${post.images[0]}" alt="${post.title}" class="post-image">
        <div class="post-content">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <span class="post-category">${post.category.replace('-', ' ').toUpperCase()}</span>
            <span class="status-badge ${statusClass}">${post.status.toUpperCase()}</span>
          </div>
          <h3 class="post-title">${post.title}</h3>
          <div class="post-meta">
            <span><i class="bi bi-person me-1"></i>${post.userName}</span>
            <span><i class="bi bi-geo-alt me-1"></i>${post.location}</span>
            <span><i class="bi bi-calendar me-1"></i>${new Date(post.createdAt).toLocaleDateString()}</span>
          </div>
          <div class="post-price">${formattedPrice}</div>
          <div class="post-actions">
            <button class="btn btn-outline-primary btn-sm" onclick="viewPost(${post.id})">
              <i class="bi bi-eye me-1"></i>View
            </button>
            ${actions}
          </div>
        </div>
      </div>
    `);
}

function viewPost(postId) {
    currentPost = samplePosts.find(post => post.id === postId);
    if (!currentPost) return;

    const formattedPrice = new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR'
    }).format(currentPost.price);

    $('#modalBody').html(`
      <div class="row">
        <div class="col-md-6">
          <img src="${currentPost.images[0]}" alt="${currentPost.title}" class="img-fluid rounded">
        </div>
        <div class="col-md-6">
          <div class="detail-group">
            <div class="detail-label">Title</div>
            <div class="detail-value">${currentPost.title}</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Price</div>
            <div class="detail-value h4 text-success">${formattedPrice}</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Category</div>
            <div class="detail-value">${currentPost.category} > ${currentPost.subcategory}</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Condition</div>
            <div class="detail-value text-capitalize">${currentPost.condition.replace('-', ' ')}</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Location</div>
            <div class="detail-value">${currentPost.location}, ${currentPost.district}, ${currentPost.province}</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Seller</div>
            <div class="detail-value">${currentPost.userName} (${currentPost.userEmail})</div>
          </div>
          <div class="detail-group">
            <div class="detail-label">Posted</div>
            <div class="detail-value">${new Date(currentPost.createdAt).toLocaleString()}</div>
          </div>
        </div>
      </div>
      <div class="mt-3">
        <div class="detail-label">Description</div>
        <div class="detail-value">${currentPost.description}</div>
      </div>
      <div class="mt-3">
        <div class="detail-label">Features</div>
        <div class="detail-value">
          ${currentPost.features.map(feature => `<span class="badge bg-light text-dark me-1">${feature}</span>`).join('')}
        </div>
      </div>
    `);

    const modal = new bootstrap.Modal($('#postDetailModal')[0]);
    modal.show();
}