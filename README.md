# Adlync – Smart Classified Ads & Marketplace

[![Live Frontend](https://img.shields.io/badge/Frontend-Vercel-black?logo=vercel)](https://adlync-springboot-fullstack-fronten.vercel.app)
[![Live Backend](https://img.shields.io/badge/Backend-Render-46E3B7?logo=render)](https://adlync-springboot-fullstack.onrender.com)
[![Database](https://img.shields.io/badge/Database-TiDB%20Cloud-red?logo=mysql)](https://tidbcloud.com)
[![Swagger Docs](https://img.shields.io/badge/API%20Docs-Swagger%20UI-85EA2D?logo=swagger)](https://adlync-springboot-fullstack.onrender.com/swagger-ui/index.html)

---

## 🌐 Live URLs & Deployment

| Component | Platform | URL |
| :--- | :--- | :--- |
| **Frontend Web App** | **Vercel** | [https://adlync-springboot-fullstack-fronten.vercel.app](https://adlync-springboot-fullstack-fronten.vercel.app) |
| **Backend API** | **Render** | [https://adlync-springboot-fullstack.onrender.com](https://adlync-springboot-fullstack.onrender.com) |
| **API Documentation** | **Swagger UI** | [https://adlync-springboot-fullstack.onrender.com/swagger-ui/index.html](https://adlync-springboot-fullstack.onrender.com/swagger-ui/index.html) |

> **Default Admin Account:**  
> **Username:** `admin`  
> **Password:** `admin@123`

---

## ☁️ Cloud Services & Third-Party Platforms Used

The entire project is architected and deployed on a **100% Free-Tier Cloud Ecosystem** with zero server costs:

| Service / Platform | Role in Project | Description & Free Tier Details |
| :--- | :--- | :--- |
| **[Vercel](https://vercel.com)** | **Frontend Hosting** | Hosts the HTML, CSS, and Vanilla JavaScript frontend on a global edge CDN with automatic GitHub CI/CD deployments and SSL certificates. |
| **[Render](https://render.com)** | **Backend Hosting** | Hosts the containerized Spring Boot backend application using a multi-stage Dockerfile (Eclipse Temurin JRE) with environment variable overrides and dynamic port binding. |
| **[TiDB Cloud](https://tidbcloud.com)** | **Cloud MySQL Database** | Fully-managed serverless MySQL 8-compatible distributed database running on AWS Tokyo (`ap-northeast-1`) with SSL encryption (`sslMode=VERIFY_IDENTITY`), auto-scaling, and continuous backups. |
| **[Firebase Auth](https://firebase.google.com)** | **Authentication** | Google Identity & OAuth2 Sign-In provider for user login, registration, and secure profile management. |
| **[ImgBB](https://imgbb.com)** | **Image Hosting API** | High-speed cloud image hosting API used for ad photo uploads, returning permanent direct image URLs without storage cost constraints. |
| **[cron-job.org](https://cron-job.org)** | **Keep-Alive Monitor** | Automated cron job pinging the backend API every 10 minutes to prevent Render free-tier container spin-down (cold start prevention). |
| **[Docker](https://www.docker.com)** | **Containerization** | Multi-stage Docker container builds packaging the Maven project and Spring Boot executable for seamless cloud portability. |

---

## 📌 Project Description
Adlync is a **web-based classified advertising platform** that enables users to post, browse, and search for various ads such as **vehicles, electronics, jobs, properties, and services**. The system provides a **secure and user-friendly interface** where buyers and sellers can efficiently connect, communicate, and manage listings with ease.

### ✨ Key Features
- **User Registration & Login** – Separate roles for Buyers & Sellers with Google Auth & email verification.
- **Post an Ad** – Choose category, add title, description, price, multi-image upload (ImgBB), and location details.
- **Advanced Listing & Search** – Dynamic multi-filter by category, price, location, and condition.
- **Ad Details Page** – Image preview slideshow, seller contact information, and review section.
- **Admin Panel** – Approve/reject ads, manage registered users, and review moderation reports.
- **Real-time Performance** – Optimized HikariCP database connection pooling and client-side network interception.
- **Additional Enhancements**
    - Ad approval workflow & moderation status
    - Seller ratings & reviews
    - Progressive & responsive mobile design
    - Interactive Swagger REST API documentation

---

## 🖼️ Screenshots & Demo
<img width="3024" height="4638" alt="Home Page" src="https://github.com/user-attachments/assets/fdb6b309-1b51-42a7-9c3e-e947826ea75d" />

<img width="3024" height="1714" alt="Sign In Page" src="https://github.com/user-attachments/assets/e2d28246-f980-450e-8580-a0dd80637280" />

<img width="869" height="473" alt="Admin Dashboard" src="https://github.com/user-attachments/assets/d26ae934-4359-4aae-93b1-fa12a2ddf6c4" />

<img width="1110" height="604" alt="Moderator Dashboard" src="https://github.com/user-attachments/assets/c8034086-eccb-415b-9ce2-448ad34c1a56" />

<img width="1512" height="857" alt="User Profile" src="https://github.com/user-attachments/assets/d03246bb-2c21-48d0-88dc-52b83f63824f" />

<img width="3024" height="4622" alt="Ad Details" src="https://github.com/user-attachments/assets/3ff675f1-e90f-40eb-958f-026fb9de9911" />

<img width="3024" height="3494" alt="Post Ad Page" src="https://github.com/user-attachments/assets/b37b1e26-ec55-4b68-9f8f-87ce48267760" />

🎬 **Watch Demo on YouTube:**  
[Adlync Smart Classified Ads & Marketplace Demo](https://youtu.be/GXPGD9f9S60?si=Xyyz9HLxlfI9qTOV)

---

## ⚙️ Local Development Setup

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/ruwani425/adlync-springboot-fullstack.git
cd adlync-springboot-fullstack
```

### 2️⃣ Run the Backend
Ensure you have **Java 17+** and **Maven** installed:
```bash
cd Backend
./mvnw clean spring-boot:run
```
Backend runs on `http://localhost:8080`.  
Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

### 3️⃣ Run the Frontend
Open `frontend/index.html` directly in your browser or run a local static server:
```bash
cd frontend
# Using Python
python -m http.server 3000
# Or using VS Code "Live Server" extension
```
The application dynamically detects `localhost` and routes API requests to `http://localhost:8080`. In production, it routes requests to the cloud backend on Render.
