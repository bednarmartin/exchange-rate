package com.bednarmartin.exchange_rate.mapper;


import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExchangeRateMapperTest {

    private final ExchangeRateMapper exchangeRateMapper = new ExchangeRateMapperImpl();

    @Test
    void testToExchangeRateResponse() {
        String currencyCode = "USD";
        BigDecimal rate = BigDecimal.valueOf(1.10);
        ExchangeRate exchangeRate = new ExchangeRate(currencyCode, rate);

        ExchangeRatesResponse exchangeRatesResponse = exchangeRateMapper.toExchangeRateResponse(exchangeRate);

        assertNotNull(exchangeRatesResponse);
        assertEquals(currencyCode, exchangeRatesResponse.currency());
        assertEquals(rate, exchangeRatesResponse.rate());
    }

}
