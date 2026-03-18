package com.careers.backend.jobApplication;

import java.time.LocalDateTime;

public record JobApplicationDtoCompanyView(

        Long id,
        String jobTitle,
        String candidateName,
        String candidateEmail,
        String coverNote,
        LocalDateTime appliedAt,
        ApplicationStatus status


) { }

