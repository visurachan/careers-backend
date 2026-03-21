package com.careers.backend.jobApplication;

import com.careers.backend.jobAdvert.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/jobAds")
@Tag(name = "Application APIs", description = "Operations related to job applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping(value = "/{id}/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Apply for a job",
            description = "Submit a job application for a specific job ad. **Requires authentication.** " +
                    "Only users with the **CANDIDATE** role can apply. " +
                    "The candidate's details are extracted automatically from the JWT token. " +
                    "First login via `/api/auth/login` to get a token, then use the Authorize button (🔓)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Application submitted successfully",
                    content = @Content(schema = @Schema(implementation = ApplicationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized – JWT token missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden – Only CANDIDATE users can apply for jobs"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job ad not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<ApplicationResponseDto> applyForJob(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestPart("request") ApplicationRequestDto request,
            @RequestPart(value = "cv", required = false) MultipartFile cv){
        String email = jwt.getSubject();
        ApplicationResponseDto response = service.applyForJob(id, email, request,cv);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my/applications")
    @Operation(
            summary = "Get my applications",
            description = "Retrieve all job applications submitted by the authenticated candidate. " +
                    "**Requires authentication.** Only users with the **CANDIDATE** role can access this. " +
                    "First login via `/api/auth/login` to get a token, then use the Authorize button (🔓)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Applications retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized – JWT token missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden – Only CANDIDATE users can view their applications"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public PageResponse<JobApplicationDtoCandidateView> getAllMyApplications(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        String email = jwt.getSubject();
        Page<JobApplicationDtoCandidateView> myApplicationsPage = service.getAllMyApplications(email, page, size);

        return new PageResponse<>(
                myApplicationsPage.getContent(),
                myApplicationsPage.getTotalPages(),
                myApplicationsPage.getTotalElements(),
                myApplicationsPage.getNumber(),
                myApplicationsPage.getSize()
        );
    }

    @GetMapping("/{id}/applications")
    @Operation(
            summary = "View applications for a job ad",
            description = "Retrieve all candidate applications for a specific job ad. " +
                    "**Requires authentication.** Only users with the **COMPANY** role can access this. " +
                    "The company can only view applications for job ads they posted. " +
                    "First login via `/api/auth/login` to get a token, then use the Authorize button (🔓)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Applications retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized – JWT token missing or invalid"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden – Only COMPANY users can view applications, or job ad does not belong to you"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job ad not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public PageResponse<JobApplicationDtoCompanyView> getCandidateApplications(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        String email = jwt.getSubject();
        Page<JobApplicationDtoCompanyView> candidateApplicationsPage = service.getCandidateApplications(id, email, page, size);

        return new PageResponse<>(
                candidateApplicationsPage.getContent(),
                candidateApplicationsPage.getTotalPages(),
                candidateApplicationsPage.getTotalElements(),
                candidateApplicationsPage.getNumber(),
                candidateApplicationsPage.getSize()
        );
    }


    @PatchMapping("/{id}/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDto> changeApplicationStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id,
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateDto status){
        String email = jwt.getSubject();
        ApplicationResponseDto response = service.changeApplicationStatus(email,id,applicationId, status);
        return ResponseEntity.status(HttpStatus.OK).body(response);



    }

}