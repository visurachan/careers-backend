package com.careers.backend.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registerNewUser")
    public ResponseEntity<RegisterResponseDto> register(
            @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = service.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}