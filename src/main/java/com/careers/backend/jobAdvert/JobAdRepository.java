package com.careers.backend.jobAdvert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobAdRepository extends JpaRepository<JobAdvert, String> {
    
}
