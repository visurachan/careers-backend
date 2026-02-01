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
  
