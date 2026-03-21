# Backend for a Careers Website

This project is a cloud-deployed backend system for a careers platform where recruiters can post jobs and candidates can view and apply for them.

It is built using modern backend engineering practices including **Test-Driven Development (TDD), CI/CD, containerization, and cloud deployment**.

---

## 🌍 Live Application

Swagger UI (Production):  
👉 https://careers-backend-5enq.onrender.com/swagger-ui/index.html

**Note:**
- The service runs on Render (free tier).
- If inactive, please wait ~30–60 seconds for the application to initialize.

**Authentication:**  
JWT authentication is required for protected endpoints. Register via `/api/auth/registerNewUser`, login via `/api/auth/login` to get a token, then use the Authorize button (🔓) in Swagger UI.

## 📋 API Endpoints

| Method | Endpoint                     | Description                                     | Status            |
|--------|------------------------------|-------------------------------------------------|-------------------|
| GET    | `/api/jobAds/{id}`           | Retrieve specific job advertisement             | ✅ Live            |
| GET    | `/api/jobAds`                | List all job advertisements                     | ✅ Live            |
| POST   | `/api/jobAds`                | Post a new job advertisement                    | ✅ Live            |
| POST   | `/api/auth/registerNewUser`  | Register a new User                             | ✅ Live            |
| POST   | `/api/auth/login`            | Login and receive JWT Token                     | ✅ Live            |
| POST   | `/api/jobAds/{id}/apply`     | Submit a job application with optional cv.pdf   | ✅ Live            |
| GET    | `/api/jobAds/my/applications` | Candidate views thier applications              | ✅ Live            |
| GET    | `/api/jobAds/{id}/applications` | Company view job applications                   |            ✅ Live |
| PATCH  | `/api/jobAds/{id}/applications/{applicationId}/status`| Company can update the status of an application |         ✅ Live          |


*API expanding incrementally with new features*

## 🎯 What Makes This Project Different

This isn't just another CRUD application. 

- **Outside-In TDD:** Every feature starts with a failing integration test, working inward through controller and service layers before any implementation
- **CI/CD from day one:** Every push triggers automated testing via GitHub Actions — code only reaches production if all tests pass
- **Incremental delivery:** Each endpoint is developed and deployed independently, mirroring how real product teams ship features
- **Production-grade security:** JWT authentication, role-based access control, private S3 bucket with IAM least-privilege policy and expiring presigned URLs

---


## ✅ Features

- Job advertisement CRUD with pagination and filtering by company
- User registration with roles — `COMPANY` and `CANDIDATE`
- JWT authentication and role-based access control
- Job application submission with duplicate prevention (candidates only)
- Optional CV upload as PDF — stored securely in AWS S3
- Presigned download URLs for CV access (15 minute expiry)
- Candidates can view their own applications
- Companies can view applications for their job ads
- Companies can update application status (`SUBMITTED` → `REVIEWING` → `INTERVIEW` → `ACCEPTED` / `REJECTED`)
- Automated CI/CD pipeline — tests must pass before deployment

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
- AWS S3 (for CV uploads)

---
## ☁️ File Storage

CV uploads are stored securely in **AWS S3** (private bucket, `us-east-1`).  
Files are never publicly accessible — download links are **pre-signed URLs** that expire after 15 minutes.

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


### Input Validation

Request DTOs currently have no validation constraints. Invalid or missing fields (e.g. blank cover note, malformed email) are not rejected at the API layer.

**Planned fix:** Add `@Valid` with Jakarta Bean Validation annotations (`@NotBlank`, `@Email`, `@Size` etc.) to all request DTOs.


---

For detailed development logs and progress tracking, see `Journal.md`.