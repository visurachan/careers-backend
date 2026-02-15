package com.careers.backend.jobAdvert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public JobAdDTO getJobById(
            @Parameter(
                    description = "Unique identifier of the job advert",
                    example = "job1git "
            )
            @PathVariable String id){
        JobAdvert jobAd = service.getJobAdById(id);
        return new JobAdDTO(jobAd.getId(), jobAd.getTitle(), jobAd.getDescription(), jobAd.getLocation(), jobAd.getExpiryDate());
    }

    @GetMapping
    @Operation(
            summary = "Get all job adverts",
            description = "Retrieves a list of all job advertisements"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of job adverts",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobAdDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    public List<JobAdDTO> getAllJobs() {
        List<JobAdvert> jobs = service.getAllJobAds();
        return jobs.stream()
                .map(job -> new JobAdDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getDescription(),
                        job.getLocation(),
                        job.getExpiryDate()
                ))
                .collect(Collectors.toList());
    }
}

