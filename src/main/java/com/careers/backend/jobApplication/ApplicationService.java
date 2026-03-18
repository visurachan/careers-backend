package com.careers.backend.jobApplication;

import com.careers.backend.auth.User;
import com.careers.backend.auth.UserRepository;
import com.careers.backend.common.exception.DuplicateApplicationException;
import com.careers.backend.jobAdvert.JobAdNotFoundException;
import com.careers.backend.jobAdvert.JobAdRepository;
import com.careers.backend.jobAdvert.JobAdvert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository repository;
    private final JobAdRepository jobAdRepository;

    public ApplicationService(UserRepository userRepository, ApplicationRepository repository, JobAdRepository jobAdRepository) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.jobAdRepository = jobAdRepository;
    }


    public ApplicationResponseDto applyForJob(String jobAdId, String candidateEmail, ApplicationRequestDto request) {
        User candidate = userRepository.findByEmail(candidateEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (repository.existsByJobAdIdAndCandidateEmail(jobAdId, candidateEmail)) {
            throw new DuplicateApplicationException();
        }
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


    public Page<JobApplicationDtoCandidateView> getAllMyApplications(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<JobApplication> applications = repository.findByCandidateEmail(email,pageable);

        return applications.map(application -> {
            JobAdvert jobAd = jobAdRepository.findById(application.getJobAdId())
                    .orElseThrow(() -> new JobAdNotFoundException(application.getJobAdId()));

            User company = userRepository.findByEmail(jobAd.getPostedBy())
                    .orElseThrow();

            return new JobApplicationDtoCandidateView(
                    application.getId(),
                    application.getJobAdId(),
                    jobAd.getTitle(),
                    company.getName(),
                    application.getCoverNote(),
                    application.getAppliedAt(),
                    application.getStatus()
            );

        });


    }
}