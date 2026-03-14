package com.careers.backend.jobApplication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobAds")
@Tag(name = "Application APIs", description = "Operations related to job applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @PostMapping("/{id}/apply")
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
            @RequestBody ApplicationRequestDto request) {
        String email = jwt.getSubject();
        ApplicationResponseDto response = service.applyForJob(id, email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
