package com.careers.backend.jobAdvert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobAdService {

    private final JobAdRepository repository;

    public JobAdService(JobAdRepository repository) {
        this.repository = repository;
    }


    public JobAdvert getJobAdById(String id) {
        // 1. Trigger the 400 Bad Request Handler

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be null or empty");
        }

        // 2. Trigger the 404 Not Found Handler

        JobAdvert jobAd = repository.findById(id)
                .orElseThrow(() -> new JobAdNotFoundException(id));


        return jobAd;
    }

    public List<JobAdvert> getAllJobAds() {
        return repository.findAll();
    }

    public JobAdDtoAllFields createNewJob(JobAdDTO request) {
        System.out.println("Service createNewJob hit - id: " + request.id());

        JobAdvert entity = new JobAdvert(
                request.id(),
                request.title(),
                request.description(),
                request.location(),
                request.expiryDate(),
                LocalDateTime.now(),
                JobAdStatus.LIVE
        );

        System.out.println("Entity isNew: " + entity.isNew());

        JobAdvert saved = repository.save(entity);

        System.out.println("Save completed - id: " + saved.getId());

        return new JobAdDtoAllFields(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getLocation(),
                saved.getExpiryDate(),
                saved.getPostedDateTime(),
                saved.getJobAdStatus()
        );
    }



}
