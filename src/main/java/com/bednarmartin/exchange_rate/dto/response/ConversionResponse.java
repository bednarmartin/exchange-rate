package com.bednarmartin.exchange_rate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ConversionResponse", description = "Response object containing the result of a currency conversion.")
public record ConversionResponse(
        @Schema(description = "The original currency code provided by the client.", example = "USD")
        String fromCurrency,

        @Schema(description = "The target currency code to which the amount is converted.", example = "EUR")
        String toCurrency,

        @Schema(description = "The original amount requested for conversion.", example = "100.00")
        BigDecimal originalAmount,

        @Schema(description = "The converted amount in the target currency.",example = "85.67")
        BigDecimal convertedAmount) {}
