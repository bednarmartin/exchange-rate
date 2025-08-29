package com.bednarmartin.exchange_rate.dto.request;

import java.math.BigDecimal;

public record ConversionRequest(
        String fromCurrency,
        String toCurrency,
        BigDecimal amount
) {}
