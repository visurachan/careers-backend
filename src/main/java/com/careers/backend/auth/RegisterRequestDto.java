package com.careers.backend.auth;

public record RegisterRequestDto(
        String name,
        String email,
        String password,
        UserRole role
){}
