package com.careers.backend.common;




import com.careers.backend.jobAdvert.JobAdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobAdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleJobNotFound() {
    }
}


