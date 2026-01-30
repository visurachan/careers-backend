package com.careers.backend.jobAdvert;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobAdRepository {

    Optional<JobAdvert> findById(String id);
}
