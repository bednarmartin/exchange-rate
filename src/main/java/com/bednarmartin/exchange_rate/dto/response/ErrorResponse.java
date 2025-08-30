package com.bednarmartin.exchange_rate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Represents an error response returned by the API")
@Builder
public record ErrorResponse(

        @Schema(description = "Timestamp when the error occurred", example = "2025-08-30T12:34:56.789")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Short HTTP status description", example = "Bad Request")
        String error,

        @Schema(description = "List of validation errors, if any")
        List<String> errorMessages
) {}