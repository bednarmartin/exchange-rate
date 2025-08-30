package com.bednarmartin.exchange_rate.service;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import com.bednarmartin.exchange_rate.exception.CurrencyCodeNotFoundException;
import com.bednarmartin.exchange_rate.mapper.ExchangeRateMapper;
import com.bednarmartin.exchange_rate.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {
    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private ExchangeRateMapper mapper;

    @InjectMocks
    private ExchangeRateService service;

    @Test
    void testGetExchangeRates() {
        String currencyCode1 = "USD";
        BigDecimal rate1 = BigDecimal.valueOf(1.10);

        String currencyCode2 = "EUR";
        BigDecimal rate2 = BigDecimal.valueOf(1.00);

        ExchangeRate exchangeRate1 = new ExchangeRate(currencyCode1, rate1);
        ExchangeRate exchangeRate2 = new ExchangeRate(currencyCode2, rate2);

        ExchangeRatesResponse response1 = new ExchangeRatesResponse(currencyCode1, rate1);
        ExchangeRatesResponse response2 = new ExchangeRatesResponse(currencyCode2, rate2);

        when(repository.findAll()).thenReturn(List.of(exchangeRate1, exchangeRate2));
        when(mapper.toExchangeRateResponse(exchangeRate1)).thenReturn(response1);
        when(mapper.toExchangeRateResponse(exchangeRate2)).thenReturn(response2);

        List<ExchangeRatesResponse> result = service.getExchangeRates();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(currencyCode1, result.get(0).currency());
        assertEquals(rate1, result.get(0).rate());
        assertEquals(currencyCode2, result.get(1).currency());
        assertEquals(rate2, result.get(1).rate());
        verify(repository).findAll();
    }

    @Test
    void testConvert_fromEUR() {
        String fromCurrencyCode = "EUR";
        String toCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal rate = BigDecimal.valueOf(2);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);
        ExchangeRate exchangeRate = new ExchangeRate(toCurrencyCode, rate);

        when(repository.findExchangeRateByCurrencyCode(toCurrencyCode)).thenReturn(Optional.of(exchangeRate));

        ConversionResponse response = service.convert(request);

        assertNotNull(response);
        assertEquals(fromCurrencyCode, response.fromCurrency());
        assertEquals(toCurrencyCode, response.toCurrency());
        assertEquals(amount, response.originalAmount());
        assertEquals(0, BigDecimal.valueOf(200).compareTo(response.convertedAmount()));
    }

    @Test
    void testConvert_toEUR() {
        String fromCurrencyCode = "USD";
        String toCurrencyCode = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal rate = BigDecimal.valueOf(2);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);
        ExchangeRate exchangeRate = new ExchangeRate(fromCurrencyCode, rate);

        when(repository.findExchangeRateByCurrencyCode(fromCurrencyCode)).thenReturn(Optional.of(exchangeRate));

        ConversionResponse response = service.convert(request);

        assertNotNull(response);
        assertEquals(fromCurrencyCode, response.fromCurrency());
        assertEquals(toCurrencyCode, response.toCurrency());
        assertEquals(amount, response.originalAmount());
        assertEquals(0, BigDecimal.valueOf(50).compareTo(response.convertedAmount()));
    }

    @Test
    void testConvert_foreignCurrencies() {
        String fromCurrencyCode = "USD";
        String toCurrencyCode = "CZK";
        BigDecimal amount = BigDecimal.valueOf(100);

        BigDecimal rateUSD = BigDecimal.valueOf(2);
        BigDecimal rateCZK = BigDecimal.valueOf(3);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);
        ExchangeRate exchangeRateUSD = new ExchangeRate(fromCurrencyCode, rateUSD);
        ExchangeRate exchangeRateCZK = new ExchangeRate(toCurrencyCode, rateCZK);

        when(repository.findExchangeRateByCurrencyCode(fromCurrencyCode)).thenReturn(Optional.of(exchangeRateUSD));
        when(repository.findExchangeRateByCurrencyCode(toCurrencyCode)).thenReturn(Optional.of(exchangeRateCZK));

        ConversionResponse response = service.convert(request);

        assertNotNull(response);
        assertEquals(fromCurrencyCode, response.fromCurrency());
        assertEquals(toCurrencyCode, response.toCurrency());
        assertEquals(amount, response.originalAmount());
        assertEquals(0, BigDecimal.valueOf(150).compareTo(response.convertedAmount()));
    }

    @Test
    void testConvert_sameCurrencies() {
        String fromCurrencyCode = "USD";
        String toCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.valueOf(100);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);

        ConversionResponse response = service.convert(request);

        assertNotNull(response);
        assertEquals(fromCurrencyCode, response.fromCurrency());
        assertEquals(toCurrencyCode, response.toCurrency());
        assertEquals(amount, response.originalAmount());
        assertEquals(0, response.originalAmount().compareTo(response.convertedAmount()));
    }

    @Test
    void testConvert_fromCurrencyNotFound() {
        String fromCurrencyCode = "XXX";
        String toCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.valueOf(100);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);

        when(repository.findExchangeRateByCurrencyCode(fromCurrencyCode)).thenReturn(Optional.empty());

        assertThrows(CurrencyCodeNotFoundException.class, () -> service.convert(request));
    }


    @Test
    void testConvert_toCurrencyNotFound() {
        String fromCurrencyCode = "EUR";
        String toCurrencyCode = "XXX";
        BigDecimal amount = BigDecimal.valueOf(100);

        ConversionRequest request = new ConversionRequest(fromCurrencyCode, toCurrencyCode, amount);

        when(repository.findExchangeRateByCurrencyCode(toCurrencyCode)).thenReturn(Optional.empty());

        assertThrows(CurrencyCodeNotFoundException.class, () -> service.convert(request));
    }


}
