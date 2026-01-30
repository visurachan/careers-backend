package com.careers.backend.jobAdvert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobAds")
public class JobAdController {

    private final JobAdService service;


    public JobAdController(JobAdService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public JobAdDTO getJobById(@PathVariable String id){
        JobAdvert jobAd = service.getJobAdById(id);
        return new JobAdDTO(jobAd.getId(), jobAd.getTitle(), jobAd.getDescription(), jobAd.getLocation(), jobAd.getExpiryDate());
    }
}

