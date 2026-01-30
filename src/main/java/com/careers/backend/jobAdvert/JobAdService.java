package com.careers.backend.jobAdvert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JobAdService {

    private final JobAdRepository repository;

    public JobAdService(JobAdRepository repository) {
        this.repository = repository;
    }
    public JobAdvert getJobAdById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new JobAdNotFoundException(id));
    }

}
