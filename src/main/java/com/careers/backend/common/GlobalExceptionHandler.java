package com.careers.backend.common;




import com.careers.backend.jobAdvert.JobAdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobAdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleJobNotFound(JobAdNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    // 400 - Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException ex) {
        return Map.of("message", "The request contains invalid data: " + ex.getMessage());
    }

    // 500 - Internal Server Error (The "Catch-All" for unexpected crashes)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralError(Exception ex) {

        return Map.of("message", "An internal error occurred. Please try again later.");
    }
}



