package com.bednarmartin.exchange_rate.repository;

import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExchangeRateRepositoryTest {

    private final ExchangeRateRepository exchangeRateRepository;


    @Test
    void testFindExchangeRateByCurrencyCode() {
        String currencyCode = "USD";
        BigDecimal rate = BigDecimal.TEN;

        ExchangeRate exchangeRate = new ExchangeRate(currencyCode, rate);
        exchangeRateRepository.save(exchangeRate);

        Optional<ExchangeRate> found = exchangeRateRepository.findExchangeRateByCurrencyCode(currencyCode);
        assertTrue(found.isPresent());
        assertEquals(rate, found.get().getRate());
    }
}
