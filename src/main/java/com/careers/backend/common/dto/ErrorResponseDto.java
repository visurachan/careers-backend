package com.careers.backend.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard error response structure")
public class ErrorResponseDto {

    @Schema(description = "HTTP status code", example = "409")
    private int status;

    @Schema(description = "Short error identifier", example = "USER_ALREADY_EXISTS")
    private String error;

    @Schema(description = "Human-readable error message", example = "A user with this email already exists.")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/auth/registerNewUser")
    private String path;

    @Schema(description = "Timestamp of the error", example = "2024-11-15T10:30:00Z")
    private String timestamp;
}