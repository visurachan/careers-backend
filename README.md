# Backend for a Careers Website

This project is a cloud-deployed backend system for a careers platform where recruiters can post jobs and candidates can view and apply for them.

It is built using modern backend engineering practices including **Test-Driven Development (TDD), CI/CD, containerization, and cloud deployment**.

---

## 🎯 What Makes This Project Different

This isn't just another CRUD application. This project demonstrates **real-world engineering practices** used by professional development teams:

### ✨ Live Production System
- **🚀 Deployed and Running:** Every feature is live in production immediately after development
- **🔄 Continuous Deployment:** Code pushed to GitHub automatically tests and deploys to cloud infrastructure
- **📊 Zero Downtime:** Updates happen seamlessly without service interruption

### 🧪 Test-Driven Development (TDD)
- **Tests Written First:** Every feature starts with failing tests, then minimal code to pass
- **Comprehensive Test Suite:** Every endpoint covered by unit tests, controller slice tests, and end-to-end integration tests following strict TDD — tests written before implementation
- **Confidence in Changes:** Comprehensive test suite catches regressions immediately

### 🔁 Professional CI/CD Pipeline
- **Automated Quality Gates:** Tests must pass before any deployment
- **GitHub Actions Integration:** Every push triggers automated testing and deployment
- **Production-Ready Workflow:** Same process used by professional engineering teams

### 🏗️ Incremental Feature Development
- **Feature-by-Feature Deployment:** Each endpoint goes live individually, not all at once
- **Iterative Approach:** Start simple, add complexity incrementally
- **Real-World Simulation:** Mirrors how actual product teams ship features

### 🎓 Learning Through Building
This project serves as a **living demonstration** of:
- How to structure a production backend application
- How to implement CI/CD from day one
- How TDD guides better design decisions
- How to deploy continuously with confidence

## 🌍 Live Application

Swagger UI (Production):  
👉 https://careers-backend-5enq.onrender.com/swagger-ui/index.html

**Note:**
- The service runs on Render (free tier).
- If inactive, please wait ~30–60 seconds for the application to initialize.
- Basic authentication is enabled.

**Authentication:**  
JWT authentication is required for protected endpoints. Register via `/api/auth/registerNewUser`, login via `/api/auth/login` to get a token, then use the Authorize button (🔓) in Swagger UI.

## 📋 API Endpoints

| Method | Endpoint                    | Description                         | Status |
|--------|-----------------------------|-------------------------------------|--------|
| GET    | `/api/jobAds/{id}`          | Retrieve specific job advertisement | ✅ Live |
| GET    | `/api/jobAds`               | List all job advertisements         | ✅ Live |
| POST   | `/api/jobAds`               | Post a new job advertisement        |    ✅ Live    |
| POST   | `/api/auth/registerNewUser` | Register a new User                 |       ✅ Live           |
| POST   | `/api/auth/login`            | Login and receive JWT Token         |        ✅ Live                |


*API expanding incrementally with new features*

---


## 📊 Project Roadmap

### ✅ Completed
- Basic job advertisement CRUD (GET by ID)
- CI/CD pipeline with GitHub Actions
- Docker containerization
- Production deployment
- Comprehensive test suite
- API documentation
- List all job ads with pagination
- Job posting (/api/jobAds)
- User registration (COMPANY/CANDIDATE/ADMIN roles)
- JWT-based authentication
- User login

### 🔄 In Progress

- Associate job ads with posting user
- Role-based access control (COMPANY/CANDIDATE/ADMIN)


### 📋 Planned (Next 2-4 Weeks)

- Job application submission
- Application management dashboard
- CV upload to AWS S3
- Email notifications

---
The system is designed to expand incrementally with additional features.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.4
- Maven
- PostgreSQL (AWS RDS)
- Docker
- Swagger / OpenAPI
- JUnit & Mockito
- GitHub Actions (CI/CD pipe line)
- Render (Deployment)
- AWS S3 (Planned for CV uploads)

---

## 🔄 CI/CD Pipeline

Every code push triggers an automated workflow:
```
Code Push → GitHub Actions
    ↓
Run All Tests (H2)
    ↓
Build Verification
    ↓
Tests Pass? → Deploy to Render
    ↓
Production Updated ✅
```
**Smart skipping: Documentation updates don't trigger builds**

## 🏗 Engineering Approach

This project simulates a real-world backend development workflow:

- **Test-Driven Development (TDD)** for feature implementation
- **Incremental deployment** — features are pushed live step-by-step
- **Cloud-first architecture** with AWS RDS
- **Containerized deployment** using Docker
- **Continuous integration & deployment** via GitHub Actions

The goal is to demonstrate production-ready backend engineering practices rather than just feature implementation.

---

## 📈 Project Vision

Planned features include:

- Recruiter job posting
- Candidate job applications
- Candidate management for recruiters
- Authentication & role-based access
- CV upload via AWS S3

---

## ⚠️ Known Gaps & Limitations

### H2 vs PostgreSQL Testing Gap

Integration tests currently run against an **H2 in-memory database** rather than PostgreSQL.
While this keeps tests fast and CI/CD infrastructure-free, it creates a gap where certain
bugs only surface in production.

This was encountered firsthand during the POST /api/jobAds implementation — all tests passed
locally and in CI, but data was silently not persisting to the AWS RDS PostgreSQL database in
production. The root causes (`@Enumerated` misconfiguration and missing `@Transactional`) were
masked by H2's more lenient behaviour.

For full details on how this was discovered and resolved, see the **21/02/2026** entry in `JOURNAL.md`.

**Planned fix:** Migrate integration tests to **TestContainers** — spinning up a real PostgreSQL
container during testing to match the production database engine exactly.

---

For detailed development logs and progress tracking, see `Journal.md`.