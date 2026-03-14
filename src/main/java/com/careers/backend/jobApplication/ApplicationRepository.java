package com.careers.backend.jobApplication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<JobApplication,Long> {
}
