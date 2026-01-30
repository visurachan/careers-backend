# Backend for a careers website
This backend application facilitates the employers to post job openings, candidates to view and apply for jobs and employers to manage the job applicants.

## Features (Planned)
-Recruites can post jobs  
-Users can apply for jobs  
-Recruiters can manage the candidates 
-Login and authnetication

## Tech stack
- Java 21
- Spring boot 3.4.2
- Maven
- PostgreSQL
- Testing (JUnit/Mockito)
- Swagger UI (API documentation and testing)
- AWS S3 (CV upload)
- Github Actions for CI/CD
- Render Web Service for deployment

  ## 📝 Project Approach

This project is being developed using **modern software engineering practices** to simulate a real-world backend application workflow. The approach focuses on **incremental development, testing, and continuous deployment**.

### 1. Test-Driven Development (TDD)
- Each feature starts with **unit tests** using **JUnit and Mockito**.
- Writing tests first ensures that the implementation meets the expected behavior and reduces bugs.
- Example: before creating the "user registration" feature, a unit test verifies that a user object is saved and retrievable from the database.

### 2. Behavior-Driven Development (BDD)
- High-level **feature scenarios** are defined using **Given–When–Then** style (Cucumber or descriptive scenarios).
- BDD ensures that features meet user expectations and desired business behavior.
- Example: “Given a job exists, when a user applies for it, then the application is stored and a confirmation is sent.”

### 3. Incremental Development & Live Deployment
- Features are developed **small step by small step**.
- Each meaningful feature is **deployed immediately** to a live backend using **Render** (or your chosen platform), ensuring that the project is always functional and testable.
- This approach demonstrates **real-world CI/CD practices**, incremental delivery, and keeps the project constantly updated.

### 4. Continuous Documentation
- Project progress, decisions, and challenges are logged in **Journal.md**.
- The journal provides insight into **development methodology, testing strategy, and deployment updates**.
- This ensures transparency in the workflow and showcases professional engineering practices.

### 5. Overall Workflow
1. **Write TDD unit tests** for the feature.  
2. **Write BDD scenarios** (if applicable) for user flows.  
3. **Implement the feature** in the codebase.  
4. **Run tests locally** to ensure correctness.  
5. **Push to GitHub → CI/CD pipeline builds and deploys**.  
6. **Update Journal.md** with progress, notes, and new features.  

By following this approach, I make to aim the project **live, functional, and continuously evolving**, simulating a professional development environment while demonstrating **TDD, BDD, CI/CD, and cloud deployment skills**.


## Project stage
-Project planning stage

## Progress
-To view more details about the progress please view the Journal .md file
