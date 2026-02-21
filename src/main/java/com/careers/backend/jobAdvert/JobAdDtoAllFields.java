package com.careers.backend.jobAdvert;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Job advertisement response object")
public record JobAdDtoAllFields(

        @Schema(
                description = "Unique identifier of the job advertisement",
                example = "job-123"
        )
        String id,

        @Schema(
                description = "Title of the job position",
                example = "Senior Java Developer"
        )
        String title,

        @Schema(
                description = "Detailed description of the job position",
                example = "We are looking for a Senior Java Developer with 5+ years of experience"
        )
        String description,

        @Schema(
                description = "Location of the job",
                example = "Berlin, Germany"
        )
        String location,

        @Schema(
                description = "Expiry date of the job advertisement (ISO-8601 format)",
                example = "2026-06-30",
                type = "string",
                format = "date"
        )
        LocalDate expiryDate,

        @Schema(
                description = "Created timestamp",
                example = "2026-06-30T10:15:30",
                implementation = LocalDateTime.class
        )
        LocalDateTime postedDateTime,

        @Schema(
                description = "Current status of the job advertisement",
                example = "LIVE",
                implementation = JobAdStatus.class
        )
        JobAdStatus jobAdStatus



) {}

