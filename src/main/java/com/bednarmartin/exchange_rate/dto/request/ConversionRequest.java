package com.bednarmartin.exchange_rate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ConversionRequest(
        @Schema(description = "Currency code you want to convert from", example = "USD")
        String fromCurrency,

        @Schema(description = "Currency code you want to convert to", example = "EUR")
        String toCurrency,

        @Schema(description = "Amount to convert", example = "100.50")
        BigDecimal amount) {}
