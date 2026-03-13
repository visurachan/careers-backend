package com.careers.backend.jobAdvert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/jobAds")
@Tag(name="Job advert APIs", description = "Operations related to job advertisements")
public class JobAdController {

    private final JobAdService service;


    public JobAdController(JobAdService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a job advert by id",
            description = "Fetches a job advert using its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job advert found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobAdDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID format supplied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job advert not found",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    public JobAdDtoAllFields getJobById(
            @Parameter(
                    description = "Unique identifier of the job advert",
                    example = "job1git "
            )
            @PathVariable String id){
        JobAdvert jobAd = service.getJobAdById(id);
        return new JobAdDtoAllFields(jobAd.getId(), jobAd.getTitle(), jobAd.getDescription(), jobAd.getLocation(), jobAd.getExpiryDate(), jobAd.getPostedDateTime(),jobAd.getJobAdStatus(), jobAd.getPostedBy());
    }

    @GetMapping
    @Operation(
            summary = "Get all job adverts",
            description = "Retrieves a paginated list of all job advertisements"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated list of job adverts",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public PageResponse<JobAdDtoAllFields> getAllJobs(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filter by company email")
            @RequestParam(required = false) String postedBy) {

        Page<JobAdvert> jobPage = service.getAllJobAds(page, size, postedBy);

        List<JobAdDtoAllFields> content = jobPage.getContent()
                .stream()
                .map(job -> new JobAdDtoAllFields(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getLocation(),
                        job.getExpiryDate(),
                        job.getPostedDateTime(),
                        job.getJobAdStatus(),
                        job.getPostedBy()
                ))
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                jobPage.getTotalPages(),
                jobPage.getTotalElements(),
                jobPage.getNumber(),
                jobPage.getSize()
        );
    }
    @PostMapping
    @Operation(
            summary = "Create a new job advertisement",
            description = "Creates a new job ad. **Requires authentication.** " +
                    "First login via `/api/auth/login` to get a JWT token, " +
                    "then click the **Authorize** button (🔓) at the top of this page and paste the token. " +
                    "Status is automatically set to LIVE and posted date/time is set by the server."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Job advertisement successfully created",
                    content = @Content(schema = @Schema(implementation = JobAdDtoAllFields.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized – JWT token missing or invalid. Login via /api/auth/login to get a token."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<JobAdDtoAllFields> createNewJobAd(
            @RequestBody JobAdDTO newJobAdRequest,
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        JobAdDtoAllFields createdJob = service.createNewJob(newJobAdRequest, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

}

