package com.careers.backend.auth;

public record LoginRequestDto(
        String email,
        String password
) {
}
