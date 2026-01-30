package com.careers.backend.jobAdvert;

public class JobAdNotFoundException extends RuntimeException{

     public JobAdNotFoundException(String id) {
            super("Job not found with id: " + id);
        }
    }


