package com.careers.backend.jobApplication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<JobApplication,Long> {

    boolean existsByJobAdIdAndCandidateEmail(String jobAdId, String candidateEmail);
    Page<JobApplication> findByCandidateEmail(String candidateEmail, Pageable pageable);
}
