# Journal

## 30/01/2026

### Main work done
Implemented service and controller layers for viewing a job by id using Test Driven Development approach

### Work done
- Wrote unit tests for service layer using JUnit and Mockito
- Implemented service logic to fetch job by ID
- Created REST controller and wrote controller tests using MockMvc
- Verified HTTP status codes and JSON response structure
- Introduced DTOs to avoid exposing entities
- Added global exception handling
- Committed and pushed code to GitHub

### Key learning

### Immediate next steps
- Add repository/integration tests using and in memory database (H2) to verify JPA mappings and queries
- Configuring to connect the repository layer to a database (docker) for local development
- Validate end-to-end data flow from repositiry -> service -> controller

## 01/02/2026

### Main work done
Implemented repository layer for viewing a job by id using TDD approach

### Work done
- Implemented repository layer using Test Driven Development
- Tested repository layer using H2 in memory database
- Verified service layer and controller layer remain unchanged due to decoupled design
- Ran application and connected to local PostgreSQL database in Docker
- Viewed job advertisement details for a specific id using Swagger UI

### Immediate next steps
- Learn the API documentation principles and complete the documentation for finished controller
- Prepare the DB using AWS RDS
- Make the app live
  
## 11/02/2026

### Main work done
Error handling and documentation for the controller which returns a job advert with specific id

### Work done
- Global Error Handling: Implemented a @RestControllerAdvice to map specific exceptions to professional HTTP status codes (400, 404, and 500).
- Documentation: Enhanced Swagger UI by adding @ApiResponses to describe all possible success and error scenarios.

### Immediate next steps
- Implement the DB using AWS RDS
- MAke the app live



## 12/02/2026

### Main Work done

Made the project live with a single controller

### Work Done

- Configured AWS RDS PostgreSQL as the production database for the backend
- Connected the Spring Boot application to AWS RDS using environment variables for secure credential management
- Dockerized the backend application for consistent deployment
- Deployed the Dockerized backend via GitHub to the hosting platform
- Verified successful database connectivity in production environment
- Tested the live endpoint using Swagger UI 
- Viewed job advertisement details for a specific id using Swagger UI on the live deployment

Swagger UI (Production):
https://careers-backend-5enq.onrender.com/swagger-ui/index.html

Note: Since the application is hosted on Render free tier, wait until Render initializes the service if it was in sleep mode before testing.

Spring Security credentials for testing:

Username: test
Password: test

### Immediate next steps
- Set up GitHub Actions for CI/CD before implementing additional controllers and features
- Automate build and Docker image creation using GitHub workflows
- Continue implementing remaining controllers following the same TDD approach



# 15/02/2026

### Main Work Done

Successfully implemented CI/CD pipeline with GitHub Actions and established comprehensive test infrastructure using H2 in-memory database.

### Work Done

#### Test Infrastructure Setup
- Attempted TestContainers for PostgreSQL integration tests but encountered Docker connectivity issues on Windows development environment
- Pivoted to H2 in-memory database for testing - industry-standard approach for CI/CD environments
- Created separate test configuration file (`src/test/resources/application.properties`) with H2 database settings
- Configured test-specific Spring Security credentials (username: test, password: test)
- Updated all integration tests to be self-contained by creating their own test data using `@BeforeEach'
- Achieved 100% test success rate: **7/7 tests passing** (2 integration, 2 controller, 1 repository, 2 service)

#### CI/CD Pipeline Implementation
- Created `.github/workflows/ci-cd.yml` for automated build and deployment
- Configured GitHub Actions workflow to:
    - Run all tests automatically on every push to main branch
    - Use Java 21 (Temurin distribution) on Ubuntu runners
    - Execute Maven test suite with H2 database
    - Build application JAR to verify compilation
    - Trigger Render deployment only after successful tests
- Set up Render deploy hook as GitHub repository secret (`RENDER_DEPLOY_HOOK`)
- Configured Render to deploy "After CI tests pass" instead of automatic deployment
- Verified complete pipeline: code push → automated tests → build verification → production deployment

#### Technical Decisions & Rationale
**Test Database Strategy:**
- Development: PostgreSQL (Docker local)
- Testing: H2 (in-memory)
- Production: PostgreSQL (AWS RDS)

This separation ensures:
- Fast test execution without external dependencies
- Platform-independent testing (works on any OS)
- No database setup required in CI/CD environment
- Zero cost for test database infrastructure

**CI/CD Benefits Achieved:**
- Automated quality gate prevents broken code from reaching production
- Can deploy multiple times per day with confidence
- Full deployment history and traceability
- Zero-downtime deployments


### Verification & Results

**Live Pipeline Demonstration:**
- Repository: https://github.com/[username]/careers-backend
- GitHub Actions: Successfully running on every push
- Latest deployment: Triggered automatically after tests passed
- Production URL: https://careers-backend-5enq.onrender.com/swagger-ui/index.html

**Test Execution Metrics:**
- Total tests: 7
- Pass rate: 100%
- Build time: ~17 seconds
- Environment: Ubuntu (GitHub-hosted runner)


### Immediate Next Steps
- Implement GET /api/jobAds endpoint (list all job advertisements) using TDD approach
- Write service test → implement service method → write controller test → implement controller
- Add pagination support in future iteration
- Continue incremental deployment through CI/CD pipeline
- Each feature will automatically test and deploy when pushed to main branch

### Technical Stack Validated
- ✅ Java 21 with Spring Boot 3.4.2
- ✅ JUnit 5 + Mockito for testing
- ✅ H2 for test database
- ✅ PostgreSQL (AWS RDS) for production
- ✅ GitHub Actions for CI/CD
- ✅ Docker for containerization
- ✅ Render for cloud deployment
- ✅ Maven for build automation

---

**Status at End of Day:**
- ✅ CI/CD pipeline fully operational
- ✅ All tests passing in automated environment
- ✅ Production deployment automated
- ✅ Ready to begin TDD development of next feature
- ✅ Professional development workflow established

# 15/02/2026 (Continued)

### Work Done

Successfully implemented GET /api/jobAds endpoint using TDD approach:

- Wrote service test → implemented `getAllJobAdverts()` method
- Wrote controller test → implemented GET endpoint with DTO transformation
- Wrote integration test → verified full HTTP request/response cycle
- All 10 tests passing locally
- Committed and pushed to GitHub
- CI/CD automatically ran tests and deployed successfully
- Feature now live in production

### Additional Updates

- Modified `.github/workflows/ci-cd.yml` to skip CI/CD for markdown file changes
- Added `paths-ignore` for `**.md` files
- Documentation updates no longer trigger builds or deployments

### Status
- Tests: 10/10 passing ✅
- Endpoints live: 2 (GET by ID, GET all)
- CI/CD: Optimized and working