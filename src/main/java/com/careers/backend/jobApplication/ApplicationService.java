package com.careers.backend.jobApplication;

import com.careers.backend.auth.User;
import com.careers.backend.auth.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository repository;

    public ApplicationService(UserRepository userRepository, ApplicationRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }


    public ApplicationResponseDto applyForJob(String jobAdId, String candidateEmail, ApplicationRequestDto request) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = new JobApplication(
                null,
                jobAdId,
                candidateEmail,
                candidate.getName(),
                request.coverNote(),
                LocalDateTime.now(),
                ApplicationStatus.SUBMITTED
        );

        JobApplication saved = repository.save(application);

        return new ApplicationResponseDto(
                saved.getId(),
                saved.getJobAdId(),
                saved.getCandidateEmail(),
                saved.getCandidateName(),
                saved.getCoverNote(),
                saved.getAppliedAt(),
                saved.getStatus()
        );
    }
}