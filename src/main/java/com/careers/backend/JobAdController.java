package com.careers.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobads")
public class JobAdController {

    @GetMapping("/{requestedId}")
    public ResponseEntity<JobAdvert> findAdById(@PathVariable String requestedId){
        if (requestedId.equals("99")) {
            JobAdvert jobAdvert = new JobAdvert("99","Developer");
            return ResponseEntity.ok(jobAdvert);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

