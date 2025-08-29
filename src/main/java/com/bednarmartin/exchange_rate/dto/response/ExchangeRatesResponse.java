package com.bednarmartin.exchange_rate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ExchangeRatesResponse", description = "Response object representing an exchange rate for a given currency.")
public record ExchangeRatesResponse(

        @Schema(description = "The 3-letter ISO currency code.", example = "USD")
        String currency,

        @Schema(description = "The exchange rate of the currency against EUR.", example = "1.1676")
        BigDecimal rate) {}
