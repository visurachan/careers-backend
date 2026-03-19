package com.careers.backend.jobApplication;

import com.careers.backend.auth.User;
import com.careers.backend.auth.UserRepository;
import com.careers.backend.common.exception.DuplicateApplicationException;
import com.careers.backend.jobAdvert.JobAdNotFoundException;
import com.careers.backend.jobAdvert.JobAdRepository;
import com.careers.backend.jobAdvert.JobAdvert;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    public Page<JobApplicationDtoCompanyView> getCandidateApplications(String jobAdId, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        JobAdvert jobAd = jobAdRepository.findById(jobAdId)
                .orElseThrow(() -> new JobAdNotFoundException(jobAdId));

        if (!jobAd.getPostedBy().equals(email)) {
            throw new AccessDeniedException("You do not own this job ad");
        }

        Page<JobApplication> applications = repository.findByJobAdId(jobAdId, pageable);

        return applications.map(application -> new JobApplicationDtoCompanyView(
                application.getId(),
                jobAd.getTitle(),
                application.getCandidateName(),
                application.getCandidateEmail(),
                application.getCoverNote(),
                application.getAppliedAt(),
                application.getStatus()
        ));
    }

    public ApplicationResponseDto changeApplicationStatus(String email, String id, Long applicationId, ApplicationStatusUpdateDto request) {


        JobAdvert jobAd = jobAdRepository.findById(id)
                .orElseThrow(() -> new JobAdNotFoundException(id));

        if (!jobAd.getPostedBy().equals(email)) {
            throw new AccessDeniedException("You do not own this job ad");
        }
        JobApplication application = repository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Not Found" ));

        if (!application.getJobAdId().equals(id)) {
            throw new AccessDeniedException("Application does not belong to this job ad");
        }
        application.setStatus(request.status());
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