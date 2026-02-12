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
- Basic authentication is enabled.

**Credentials:**
- Username: `test`
- Password: `test`

---

## 🚀 Current Status

- Live backend deployed
- Connected to AWS RDS PostgreSQL (production database)
- Dockerized and deployed via GitHub
- One fully implemented controller (Job Advert – view by ID)
- Global exception handling and API documentation completed
- CI/CD setup in progress (GitHub Actions)

The system is designed to expand incrementally with additional features.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3
- Maven
- PostgreSQL (AWS RDS)
- Docker
- Swagger / OpenAPI
- JUnit & Mockito
- GitHub Actions (CI/CD – in progress)
- Render (Deployment)
- AWS S3 (Planned for CV uploads)

---

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

For detailed development logs and progress tracking, see `Journal.md`.