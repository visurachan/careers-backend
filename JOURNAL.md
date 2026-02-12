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

