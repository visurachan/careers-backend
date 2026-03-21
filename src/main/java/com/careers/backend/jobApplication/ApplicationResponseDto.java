package com.careers.backend.jobApplication;

import java.time.LocalDateTime;

public record ApplicationResponseDto (
    Long id,
    String jobAdId,
    String candidateEmail,
    String candidateName,
    String coverNote,
    LocalDateTime appliedAt,
    ApplicationStatus status,
    String cvDownloadUrl

    ){}


