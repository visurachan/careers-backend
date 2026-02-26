package com.careers.backend.auth;

public record RegisterResponseDto(
        Long id,
        String name,
        String email,
        UserRole role

) {}
