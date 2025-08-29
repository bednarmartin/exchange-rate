package com.bednarmartin.exchange_rate.dto.response;

import java.math.BigDecimal;

public record ConversionResponse(
        String fromCurrency,
        String toCurrency,
        BigDecimal originalAmount,
        BigDecimal convertedAmount)
{}
