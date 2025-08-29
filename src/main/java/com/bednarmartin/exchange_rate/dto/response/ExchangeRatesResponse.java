package com.bednarmartin.exchange_rate.dto.response;

public record ExchangeRatesResponse(
        String currency,
        String rate)
{}
