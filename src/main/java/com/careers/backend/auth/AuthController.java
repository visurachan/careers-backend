package com.careers.backend.auth;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name="User APIs", description = "Operations related to Users")
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager authenticationManager;


    public AuthController(AuthService service, AuthenticationManager authenticationManager) {
        this.service = service;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registerNewUser")
    @Operation(
            summary = "Register a New User",
            description = "Register any type of user [COMPANY/CANDIDATE/ADMIN]" + "Returns the created userdetails upon successfull registretion"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request – Invalid or missing fields in the request body"

            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict – A user with this email already exists"

            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable Entity – Validation failed (e.g. weak password, invalid role)"

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error – Unexpected error on the server side"

            )})
    public ResponseEntity<RegisterResponseDto> register(
            @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = service.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        LoginResponseDto response = service.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}