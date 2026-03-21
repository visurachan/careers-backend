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


# 21/02/2026

### Main Work Done

Implemented POST /api/jobAds endpoint using TDD approach. Encountered and resolved a critical data persistence bug in production despite all tests passing.

### Work Done

#### TDD Implementation
- Wrote integration test first as failing acceptance criteria (outside-in TDD)
- Wrote controller test with mocked service layer using `@WebMvcTest`
- Wrote service test with mocked repository using `@ExtendWith(MockitoExtension.class)`
- Implemented each layer to make tests green: controller → service → repository
- Integration test went green last confirming full stack wired correctly
- All tests passing locally with H2 in-memory database

#### CI/CD & Deployment
- Pushed to GitHub triggering GitHub Actions workflow
- All tests passed automatically in CI pipeline
- Render deployment triggered automatically after tests passed
- Endpoint live at https://careers-backend-5enq.onrender.com/swagger-ui/index.html

#### Production Bug — Data Not Persisting Despite 201 Response

Although all tests passed and the endpoint was successfully deployed, a critical bug was discovered in production: POST requests were returning a correct `201 Created` response with the full response body, but the data was never actually being written to the AWS RDS PostgreSQL database.

**Why the response body was still returning correctly:**

The service was building the response from the in-memory entity object created in the service layer — not from the database. So `repository.save()` appeared to succeed and returned the same object back, which was then mapped to the DTO and returned as the response. The data only existed in memory for the duration of the request and disappeared immediately after.

**Debugging steps taken:**

- Checked Render logs after POST requests — no logs appearing at all initially
- Confirmed GET endpoints were working and showing logs correctly
- Suspected Cloudflare caching POST responses
- Tested via curl from local terminal directly to the Render URL
- Got a `500 Internal Server Error` response confirming the request was hitting the server
- Added `System.out.println` log lines at controller and service level to trace execution
- Confirmed request was reaching controller and service with `isNew: true`
- Checked AWS RDS directly via CloudShell — record not present in database
- Finally identified the root causes

**Root Causes Found:**

1. **Missing `@Enumerated(EnumType.STRING)`** on the `jobAdStatus` field in the `JobAdvert` entity. Without this Hibernate defaults to storing enums as numbers (`smallint`), causing a type mismatch when trying to save the String value `"LIVE"` to the database.

2. **Missing `@Transactional`** on the `createNewJob()` service method. Without this the save operation executed but the transaction was never committed to the database — it silently rolled back at the end of the method.

**Fix Applied:**

```java
// JobAdvert entity
@Enumerated(EnumType.STRING)
private JobAdStatus jobAdStatus;

// JobAdvertService
@Transactional
public JobAdDtoAllFields createNewJob(JobAdDTO request) { ... }
```

Dropped the existing `job_advert` table in AWS RDS (it had incorrect column types from the previous misconfiguration), restarted Render to let Spring Boot recreate the table with correct schema, and verified data was persisting correctly.

### Things Learned & Reflection

#### The Tests Passed But Production Was Still Broken

This is a key lesson — **passing tests do not guarantee correct production behaviour**. The integration test used H2 in-memory database which is more lenient than PostgreSQL. Specifically:

- H2 handled the enum without `@Enumerated` differently to PostgreSQL
- H2 may have been auto-committing transactions where PostgreSQL required explicit `@Transactional`

**What this reveals is a gap in the test strategy.** The integration tests were running against H2 but production runs against PostgreSQL. These two databases behave differently in subtle ways meaning some bugs only surface in production.

#### What Could Be Done to Prevent This

- **Use TestContainers** for integration tests — spin up a real PostgreSQL container during testing so tests run against the exact same database engine as production. This would have caught both the `@Enumerated` and `@Transactional` issues before deployment.
- **Add a smoke test** after deployment — a simple automated check that actually POSTs data and then GETs it back to verify the round trip works end to end in production.
- **Always verify database writes** after implementing any new write endpoint — don't trust the response body alone as proof of persistence.

### Status
- Tests: passing ✅
- POST /api/jobAds: live and persisting correctly to AWS RDS ✅
- Data verified in AWS RDS PostgreSQL ✅


# 22/02/2026

### Main Work Done

Implemented pagination for GET /api/jobAds endpoint.

### Work Done

- Updated integration, controller and service tests to support paginated response following outside-in TDD
- Implemented pagination in service using Spring Data JPA `Pageable`
- Created custom `PageResponse<T>` record as response wrapper — Spring's `Page<T>` does not serialize cleanly with Jackson out of the box
- Updated Swagger annotations to document `page` and `size` query parameters
- All tests passing, deployed to production via GitHub Actions

### Things Learned

Spring's internal `Page<T>` type does not serialize cleanly to JSON with Jackson — a custom response wrapper was created

### Status
- Tests: all passing ✅
- GET /api/jobAds: paginated response live in production ✅


# 27/02/2026

### Main Work Done

Completed user registration endpoint with exception handling, duplicate email validation and Swagger documentation.

### Work Done

#### User Registration — POST /auth/registerNewUser

Implemented using outside-in TDD approach:

1. Integration test written first as failing acceptance criteria
2. Controller test written using `@WebMvcTest` with `@MockBean`
3. Service test written using `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks`
4. Repository test written for custom `findByEmail` query
5. Each layer implemented to pass its test
6. Integration test green confirmed full stack wired correctly

Password is never stored in plain text — `BCryptPasswordEncoder` hashes the password in the service before saving to the database. The service test verifies this with `verify(passwordEncoder).encode(...)` confirming hashing always happens.

#### Exception Handling & Validation

- Added `ErrorResponseDto` record for consistent error response structure across all endpoints
- Added `GlobalExceptionHandler` with handlers for 400, 401, 403, 404, 409 and 500
- Added `UserAlreadyExistsException` and `UnauthorizedException` custom exceptions
- Added duplicate email check in `AuthService` using `existsByEmail`
- Added `existsByEmail` method to `UserRepository`

#### Security Config Update

- Added `PasswordEncoder` bean (`BCryptPasswordEncoder`) to `SecurityConfig`
- Added `InMemoryUserDetailsManager` with `test/test` user to maintain existing job ad integration tests while real user registration is being built
- Permitted `/auth/registerNewUser` endpoint without authentication

### Things Learned

**`@Mock` vs `@MockBean` — when to use each:**

This became clear while implementing the auth controller test. `@MockBean` is the correct choice for controller tests using `@WebMvcTest` because Spring loads a real web context and needs to wire the mock into the controller as a Spring bean. `@Mock` with `@InjectMocks` is for service and unit tests where no Spring context is involved — pure Mockito, faster and lighter. Using `@Mock` in a `@WebMvcTest` would fail because Spring wouldn't know about the mock. The rule is simple: Spring context present → `@MockBean`, no Spring context → `@Mock`.

### Status
- Tests: all passing ✅
- POST /auth/registerNewUser: live in production ✅
- Global exception handling: live ✅

### Immediate Next Steps
- Implement JWT login (POST /auth/login)
- Replace Basic Auth with JWT token based authentication
- Remove `test/test` InMemoryUserDetailsManager once JWT is implemented


## 07/03/2026

### Main Work Done

Implemented JWT login endpoint, replaced Basic Auth with Bearer token authentication, updated all integration tests to use JWT, and deployed to Render with full end-to-end testing.

### Work Done

#### JWT Login — POST /api/auth/login

Implemented using outside-in TDD approach — integration test first, then controller, service, and repository layers. Login authenticates via `AuthenticationManager` delegating to `DaoAuthenticationProvider` → `UserDetailsService` → BCrypt comparison. On success `JwtService` generates a signed HMAC-SHA256 token returned in the response body.

#### Circular Dependency Resolution

Original design caused a three-way cycle: `AuthService` → `AuthenticationManager` → `UserDetailsService` → `AuthService`. Fixed by moving `authenticate()` out of `AuthService` into `AuthController`. `AuthService` now only handles user lookup and token generation.

#### JWT Security Filter Chain

Replaced Basic Auth with Bearer token authentication. Public endpoints are all GET job ad routes and both auth endpoints. All other endpoints require a valid JWT. `BadCredentialsException` handled explicitly in `GlobalExceptionHandler` returning 401 with a generic message to prevent user enumeration.

#### Integration Test Migration

All job ad integration tests migrated from Basic Auth to Bearer token. Added a `getToken()` helper and `userRepository.deleteAll()` in `@BeforeEach` to prevent 409 conflicts between tests.

#### Swagger UI Authentication

Added `SecurityScheme` to `OpenApiConfig` so the Authorize button appears in Swagger UI. Added documentation to login and POST job ad endpoints guiding users through the register → login → authorize flow.

### Things Learned

**Circular dependency in Spring Security:** When a service implements `UserDetailsService` and also needs `AuthenticationManager`, move the `authenticate()` call to the controller to break the cycle.

**Base64-encoded secrets are mandatory:** `Decoders.BASE64.decode()` crashes at startup with plain text secrets containing hyphens or special characters. Both test and production properties must use properly Base64-encoded values.

**`@WebMvcTest` masks security config bugs:** Controller tests with `addFilters=false` never load `SecurityConfig` so they pass even if the config is broken. Only `@SpringBootTest` catches these issues — another reason integration tests are the real safety net.

**`BadCredentialsException` covers both wrong email and wrong password:** Spring Security throws the same exception for both cases intentionally to prevent user enumeration.

### Status
- Tests: all passing ✅
- POST /api/auth/login: live in production ✅
- Bearer token authentication: live ✅
- Swagger UI authentication: working ✅

### Immediate Next Steps
- Associate job ads with the user who posted them
- Role-based access control — only `COMPANY` role can post job ads
- Update README roadmap


## 09/03/2026

### Main Work Done

Associated job ads with the authenticated user who posted them by adding a `postedBy` field, and migrated `JobAdControllerTest` to `@WebMvcTest` with real security context.

### Work Done

#### postedBy Field — JobAdvert

Added `postedBy` as a plain string field to the `JobAdvert` entity and `JobAdDtoAllFields` response DTO. Updated `createNewJob` service method to accept email as a parameter. Updated `JobAdController` to extract the authenticated user's email from the JWT via `@AuthenticationPrincipal` and pass it to the service. Hibernate's `ddl-auto=update` automatically adds the `posted_by` column to the production database on next deploy.

#### Controller Test Migration

Migrated `JobAdControllerTest` from `standaloneSetup` to `@WebMvcTest` with real `SecurityConfig` loaded. Previous `standaloneSetup` approach didn't support `@AuthenticationPrincipal` injection. Added `@TestPropertySource` with the test JWT secret and `@MockBean AuthService` to satisfy `SecurityConfig` dependencies.

### Things Learned

**`standaloneSetup` doesn't support Spring Security features:** `@AuthenticationPrincipal` injection requires a real security context. Once `SecurityConfig` needs to be loaded in controller tests, `standaloneSetup` must be replaced with `@WebMvcTest`.

**`anonymous()` vs `jwt()` in `@WebMvcTest`:** Public endpoints need `.with(anonymous())` and protected endpoints need `.with(jwt())` when testing with a real security filter chain.

### Status
- Tests: all passing ✅
- postedBy field: live on next deploy ✅

### Immediate Next Steps
- Role-based access control — only `COMPANY` role can post job ads

## 12/03/2026

### Main Work Done

Implemented role-based access control restricting job ad posting to COMPANY users only, and added proper JSON error responses for 403 Forbidden cases.

### Work Done

#### Role-Based Access Control — POST /api/jobAds

Added role claim to JWT token on login. `JwtService.generateToken()` now accepts the user's role as a parameter. `AuthService.login()` fetches the user from the database to retrieve their role and passes it to `JwtService`. Added `JwtAuthenticationConverter` to `SecurityConfig` to extract the `role` claim from the JWT and convert it to a `ROLE_` prefixed Spring Security authority. Updated `SecurityConfig` to restrict `POST /api/jobAds` to `ROLE_COMPANY` only.

#### Custom 403 Response

Added `CustomAccessDeniedHandler` in the `auth` package to return a consistent JSON error body when a user with insufficient privileges attempts a protected action. Wired into `SecurityConfig` via `exceptionHandling()` alongside the existing `CustomAuthenticationEntryPoint`.

#### Test Updates

Updated `AuthServiceTest` to stub `generateToken()` with the role parameter. Updated `JobAdControllerTest` to set authorities directly via `.authorities(new SimpleGrantedAuthority("ROLE_COMPANY"))` rather than relying on the JWT converter — the converter does not run in the `@WebMvcTest` context. Role restriction is tested at the integration level where the full security stack is loaded. Added `CustomAccessDeniedHandler` as `@MockBean` in `JobAdControllerTest` to satisfy `SecurityConfig` context loading.

### Things Learned

**Security policy belongs at the integration test level:** Role-based access rules involve the full security filter chain and JWT converter. These don't wire correctly in `@WebMvcTest` when using `.claim()` — use `.authorities()` directly for controller tests, and rely on integration tests for end-to-end security policy verification.

**`@WebMvcTest` mocks break handler behaviour:** `CustomAccessDeniedHandler` and `CustomAuthenticationEntryPoint` must be added as `@MockBean` when importing `SecurityConfig` — but mocking them means their response-writing logic doesn't execute. Security enforcement still works; only the custom response body is bypassed in controller tests.

### Status
- Tests: all passing ✅
- COMPANY-only job posting: live on next deploy ✅
- JSON 403 response: live on next deploy ✅

### Immediate Next Steps
- View job ads posted by a specific company
- Job application submission endpoint


## 07/03/2026

### Main Work Done

Implemented JWT login endpoint, replaced Basic Auth with Bearer token authentication, updated all integration tests to use JWT, and deployed to Render with full end-to-end testing.

### Work Done

#### JWT Login — POST /api/auth/login

Implemented using outside-in TDD approach — integration test first, then controller, service, and repository layers. Login authenticates via `AuthenticationManager` delegating to `DaoAuthenticationProvider` → `UserDetailsService` → BCrypt comparison. On success `JwtService` generates a signed HMAC-SHA256 token returned in the response body.

#### Circular Dependency Resolution

Original design caused a three-way cycle: `AuthService` → `AuthenticationManager` → `UserDetailsService` → `AuthService`. Fixed by moving `authenticate()` out of `AuthService` into `AuthController`. `AuthService` now only handles user lookup and token generation.

#### JWT Security Filter Chain

Replaced Basic Auth with Bearer token authentication. Public endpoints are all GET job ad routes and both auth endpoints. All other endpoints require a valid JWT. `BadCredentialsException` handled explicitly in `GlobalExceptionHandler` returning 401 with a generic message to prevent user enumeration.

#### Integration Test Migration

All job ad integration tests migrated from Basic Auth to Bearer token. Added a `getToken()` helper and `userRepository.deleteAll()` in `@BeforeEach` to prevent 409 conflicts between tests.

#### Swagger UI Authentication

Added `SecurityScheme` to `OpenApiConfig` so the Authorize button appears in Swagger UI. Added documentation to login and POST job ad endpoints guiding users through the register → login → authorize flow.

### Things Learned

**Circular dependency in Spring Security:** When a service implements `UserDetailsService` and also needs `AuthenticationManager`, move the `authenticate()` call to the controller to break the cycle.

**Base64-encoded secrets are mandatory:** `Decoders.BASE64.decode()` crashes at startup with plain text secrets containing hyphens or special characters. Both test and production properties must use properly Base64-encoded values.

**`@WebMvcTest` masks security config bugs:** Controller tests with `addFilters=false` never load `SecurityConfig` so they pass even if the config is broken. Only `@SpringBootTest` catches these issues — another reason integration tests are the real safety net.

**`BadCredentialsException` covers both wrong email and wrong password:** Spring Security throws the same exception for both cases intentionally to prevent user enumeration.

### Status
- Tests: all passing ✅
- POST /api/auth/login: live in production ✅
- Bearer token authentication: live ✅
- Swagger UI authentication: working ✅

### Immediate Next Steps
- Associate job ads with the user who posted them
- Role-based access control — only `COMPANY` role can post job ads
- Update README roadmap


## 09/03/2026

### Main Work Done

Associated job ads with the authenticated user who posted them by adding a `postedBy` field, and migrated `JobAdControllerTest` to `@WebMvcTest` with real security context.

### Work Done

#### postedBy Field — JobAdvert

Added `postedBy` as a plain string field to the `JobAdvert` entity and `JobAdDtoAllFields` response DTO. Updated `createNewJob` service method to accept email as a parameter. Updated `JobAdController` to extract the authenticated user's email from the JWT via `@AuthenticationPrincipal` and pass it to the service. Hibernate's `ddl-auto=update` automatically adds the `posted_by` column to the production database on next deploy.

#### Controller Test Migration

Migrated `JobAdControllerTest` from `standaloneSetup` to `@WebMvcTest` with real `SecurityConfig` loaded. Previous `standaloneSetup` approach didn't support `@AuthenticationPrincipal` injection. Added `@TestPropertySource` with the test JWT secret and `@MockBean AuthService` to satisfy `SecurityConfig` dependencies.

### Things Learned

**`standaloneSetup` doesn't support Spring Security features:** `@AuthenticationPrincipal` injection requires a real security context. Once `SecurityConfig` needs to be loaded in controller tests, `standaloneSetup` must be replaced with `@WebMvcTest`.

**`anonymous()` vs `jwt()` in `@WebMvcTest`:** Public endpoints need `.with(anonymous())` and protected endpoints need `.with(jwt())` when testing with a real security filter chain.

### Status
- Tests: all passing ✅
- postedBy field: live on next deploy ✅

### Immediate Next Steps
- Role-based access control — only `COMPANY` role can post job ads


## 12/03/2026

### Main Work Done

Implemented role-based access control restricting job ad posting to COMPANY users only, and added proper JSON error responses for 403 Forbidden cases.

### Work Done

#### Role-Based Access Control — POST /api/jobAds

Added role claim to JWT token on login. `JwtService.generateToken()` now accepts the user's role as a parameter. `AuthService.login()` fetches the user from the database to retrieve their role and passes it to `JwtService`. Added `JwtAuthenticationConverter` to `SecurityConfig` to extract the `role` claim from the JWT and convert it to a `ROLE_` prefixed Spring Security authority. Updated `SecurityConfig` to restrict `POST /api/jobAds` to `ROLE_COMPANY` only.

#### Custom 403 Response

Added `CustomAccessDeniedHandler` in the `auth` package to return a consistent JSON error body when a user with insufficient privileges attempts a protected action. Wired into `SecurityConfig` via `exceptionHandling()` alongside the existing `CustomAuthenticationEntryPoint`.

#### Test Updates

Updated `AuthServiceTest` to stub `generateToken()` with the role parameter. Updated `JobAdControllerTest` to set authorities directly via `.authorities(new SimpleGrantedAuthority("ROLE_COMPANY"))` rather than relying on the JWT converter — the converter does not run in the `@WebMvcTest` context. Role restriction is tested at the integration level where the full security stack is loaded. Added `CustomAccessDeniedHandler` as `@MockBean` in `JobAdControllerTest` to satisfy `SecurityConfig` context loading.

### Things Learned

**Security policy belongs at the integration test level:** Role-based access rules involve the full security filter chain and JWT converter. These don't wire correctly in `@WebMvcTest` when using `.claim()` — use `.authorities()` directly for controller tests, and rely on integration tests for end-to-end security policy verification.

**`@WebMvcTest` mocks break handler behaviour:** `CustomAccessDeniedHandler` and `CustomAuthenticationEntryPoint` must be added as `@MockBean` when importing `SecurityConfig` — but mocking them means their response-writing logic doesn't execute. Security enforcement still works; only the custom response body is bypassed in controller tests.

### Status
- Tests: all passing ✅
- COMPANY-only job posting: live on next deploy ✅
- JSON 403 response: live on next deploy ✅

### Immediate Next Steps
- View job ads posted by a specific company
- Job application submission endpoint

## 13/03/2026

### Main Work Done

Implemented filtering job ads by company email via an optional `postedBy` query parameter.

### Work Done

#### Filter Job Ads by Company — GET /api/jobAds?postedBy=email

Added optional `postedBy` query parameter to the existing `GET /api/jobAds` endpoint. When provided, returns only job ads posted by that company. When omitted, returns all job ads as before — fully backwards compatible. Updated `JobAdService.getAllJobAds()` to accept an optional `postedBy` parameter and conditionally call `repository.findByPostedBy()` or `repository.findAll()`. Added `findByPostedBy()` to `JobAdRepository` — Spring Data JPA derives the query automatically from the method name.

### Things Learned

**One method, optional parameter is cleaner than two methods:** Rather than having separate `getAllJobAds()` and `getJobAdsByCompany()` methods, a single method with a nullable `postedBy` parameter handles both cases with a simple null check.

**Spring Data derived queries:** Adding `findByPostedBy(String postedBy, Pageable pageable)` to the repository interface is all that's needed — no SQL or JPQL required. Spring Data generates the query from the method name automatically.

### Status
- Tests: all passing ✅
- Filter by company email: live on next deploy ✅

### Immediate Next Steps
- Job application submission endpoint

---

## 14/03/2026

### Main Work Done

Implemented job application submission endpoint allowing candidates to apply for job ads.

### Work Done

#### Job Application Submission — POST /api/jobAds/{id}/apply

Created the `application` package with `Application` entity, `ApplicationRepository`, `ApplicationService`, `ApplicationController`, `ApplicationRequestDto`, `ApplicationResponseDto`, and `ApplicationStatus` enum. The candidate only sends a `coverNote` in the request body — all other fields are set server-side. Candidate email is extracted from the JWT via `@AuthenticationPrincipal`. Candidate name is looked up from `UserRepository` using the email. Status is set to `SUBMITTED` automatically. Applied at timestamp is set server-side.

#### ApplicationStatus Enum

Added `SUBMITTED`, `REVIEWING`, `INTERVIEW`, `REJECTED`, `ACCEPTED` states to support the full application lifecycle.

### Things Learned

**Client should never send identity data:** The candidate's email and name come from the JWT and database respectively — never from the request body. This prevents candidates from impersonating others.

**Service layer owns enrichment:** The controller extracts the email from the JWT and passes it to the service. The service looks up the candidate's name. Each layer has a single responsibility.

### Status
- Tests: all passing ✅
- POST /api/jobAds/{id}/apply: live on next deploy ✅

### Immediate Next Steps
- Prevent duplicate applications
- Restrict application submission to CANDIDATE role only

---

## 16/03/2026

### Main Work Done

Prevented duplicate applications and restricted job application submission to CANDIDATE role only.

### Work Done

#### Duplicate Application Prevention

Added `existsByJobAdIdAndCandidateEmail()` to `ApplicationRepository`. Added a duplicate check in `ApplicationService.applyForJob()` before saving — throws `DuplicateApplicationException` if the candidate has already applied for that job. Added handler in `GlobalExceptionHandler` returning 409 Conflict with a clear error message.

#### CANDIDATE-Only Restriction

Added `hasRole("CANDIDATE")` rule to `SecurityConfig` for `POST /api/jobAds/{id}/apply`. Added integration test asserting a COMPANY user receives 403 when attempting to apply. Moved specific security rules above the wildcard `GET /api/jobAds/**` permitAll rule to ensure correct rule ordering — Spring Security processes rules top to bottom, first match wins.

### Things Learned

**Spring Security rule ordering matters:** More specific rules must come before wildcard rules. `GET /api/jobAds/my/applications` was being matched by `GET /api/jobAds/**` before the CANDIDATE restriction could apply — fixed by reordering rules.

**`existsBy` queries in Spring Data:** `existsByJobAdIdAndCandidateEmail()` is a derived query that returns a boolean — cleaner and more efficient than fetching the full entity just to check existence.

### Status
- Tests: all passing ✅
- Duplicate application prevention: live on next deploy ✅
- CANDIDATE-only application restriction: live on next deploy ✅

### Immediate Next Steps
- Candidates view their own applications
- Companies view applications for their job ads

---

## 18/03/2026

### Main Work Done

Implemented two application viewing endpoints — candidates view their own applications and companies view applications for their job ads.

### Work Done

#### Candidate View — GET /api/jobAds/my/applications

Returns a paginated list of all applications submitted by the authenticated candidate. Service enriches each application with `jobTitle` and `companyName` — looked up from `JobAdvert` and `User` repositories respectively since these fields aren't stored on the `Application` entity. Restricted to CANDIDATE role only.

#### Company View — GET /api/jobAds/{id}/applications

Returns a paginated list of all candidate applications for a specific job ad. Service verifies the requesting company owns the job ad before returning results — throws `AccessDeniedException` if the job ad belongs to a different company. Restricted to COMPANY role only.

#### JobApplicationDtoCandidateView and JobApplicationDtoCompanyView

Two separate response DTOs — candidates see job title and company name, companies see candidate name and email. Each view exposes only what's relevant to that role.

### Things Learned

**Ownership verification before data access:** Before returning applications for a job ad, the service checks `jobAd.getPostedBy().equals(companyEmail)`. This prevents companies from viewing applications for job ads they don't own — a simple but important authorization check at the data level beyond just role-based access.

**Look up once, map many:** When enriching a page of applications with job ad data, fetch the `JobAdvert` once outside the `map()` loop rather than once per application — avoids N+1 queries.

### Status
- Tests: all passing ✅
- GET /api/jobAds/my/applications: live on next deploy ✅
- GET /api/jobAds/{id}/applications: live on next deploy ✅

### Immediate Next Steps
- Update application status (PATCH endpoint for company)
- CV upload to AWS S3


## 19/03/2026

### Main Work Done

Implemented application status update endpoint allowing companies to move candidates through the hiring pipeline.

### Work Done

#### Update Application Status — PATCH /api/jobAds/{id}/applications/{applicationId}/status

Added `PATCH` endpoint for companies to update a candidate's application status. Request body contains only the new status. Service performs three validation checks before updating: job ad exists, company owns the job ad, and the application belongs to the specified job ad. Status is updated and the full updated application is returned in the response.

#### ApplicationStatusUpdateDto

Added a simple request DTO containing just the new `ApplicationStatus` value. The client sends only what needs to change — nothing else.


### Status
- Tests: all passing ✅
- PATCH /api/jobAds/{id}/applications/{applicationId}/status: ready to deploy ✅

### Immediate Next Steps
- CV upload to AWS S3


## 21/03/2026

### Main Work Done

Implemented CV upload to AWS S3 when candidates apply for jobs.

### Work Done

#### CV Upload — S3 Integration

Added AWS SDK v2 S3 dependency. Created `S3Config` producing `S3Client` and `S3Presigner` beans, credentials read from environment variables. Created `S3Service` with two responsibilities: `uploadCv()` uploads a PDF to a private S3 bucket using a UUID key (`cvs/{uuid}.pdf`) and returns the key; `generatePresignedUrl()` generates a 15-minute expiring download URL for a given key.

#### JobApplication Entity

Added nullable `cvS3Key` column to store the S3 object key. Raw key is never exposed in responses — presigned URL is generated on demand and returned instead.

#### Apply Endpoint Changes

Updated `POST /api/jobAds/{id}/apply` to accept `multipart/form-data` with a plain text `coverNote` field and an optional `cv` PDF file. CV is uploaded to S3 before saving the application. Response includes a `cvDownloadUrl` presigned link valid for 15 minutes.

#### View Applications

Updated candidate and company application response DTOs to include `cvDownloadUrl`. Presigned URL generated fresh on every read request.

#### AWS Setup

Created private S3 bucket in `us-east-1`. Created dedicated IAM user with least-privilege inline policy scoped to `PutObject` and `GetObject` on the specific bucket only. Credentials added as environment variables on Render.

### Status
- Tests: all passing ✅
- CV upload to S3: live and verified end-to-end ✅

### Immediate Next Steps
- Continue expanding careers portal features