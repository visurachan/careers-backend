package com.careers.backend.jobApplication;

import java.time.LocalDateTime;

public record JobApplicationDtoCandidateView(

        Long id,
        String jobAdId,
        String jobTitle,
        String companyName,
        String coverNote,
        LocalDateTime appliedAt,
        ApplicationStatus status


) { }
