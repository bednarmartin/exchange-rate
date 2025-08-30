package com.bednarmartin.exchange_rate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ConversionRequest(

        @Schema(description = "Currency code you want to convert from", example = "USD")
        @NotBlank(message = "From currency must not be blank")
        @Pattern(regexp = "^[A-Z]{3}$", message = "From currency must be a valid 3-letter ISO code (e.g., USD)")
        String fromCurrency,

        @Schema(description = "Currency code you want to convert to", example = "EUR")
        @NotBlank(message = "To currency must not be blank")
        @Pattern(regexp = "^[A-Z]{3}$", message = "To currency must be a valid 3-letter ISO code (e.g., EUR)")
        String toCurrency,

        @Schema(description = "Amount to convert", example = "100.50")
        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be greater than 0")
        @Digits(integer = 18, fraction = 2, message = "Amount must have at most 2 decimal places")
        BigDecimal amount) {}
