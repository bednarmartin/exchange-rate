package com.bednarmartin.exchange_rate.service;

import com.bednarmartin.exchange_rate.constants.CurrencyConstants;
import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import com.bednarmartin.exchange_rate.exceptions.CurrencyCodeNotFoundException;
import com.bednarmartin.exchange_rate.mapper.ExchangeRateMapper;
import com.bednarmartin.exchange_rate.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService implements IExchangeRatesService {

    private final ExchangeRateRepository exchangeRatesRepository;

    private final ExchangeRateMapper exchangeRateMapper;

    @Override
    public List<ExchangeRatesResponse> getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRatesRepository.findAll();
        return exchangeRates.stream().map(exchangeRateMapper::toExchangeRateResponse).toList();
    }

    @Override
    public ConversionResponse convert(ConversionRequest conversionRequest) {
        String from = conversionRequest.fromCurrency();
        String to = conversionRequest.toCurrency();
        BigDecimal amount = conversionRequest.amount();
        BigDecimal convertedAmount;

        if (isEuro(from)) {
            convertedAmount = multiplyByRate(to, amount);
        } else if (isEuro(to)) {
            convertedAmount = divideByRate(from, amount);
        } else {
            convertedAmount = convertViaEuro(from, to, amount);
        }

        return new ConversionResponse(from, to, amount, convertedAmount);
    }

    private boolean isEuro(String currency) {
        return CurrencyConstants.EUR.equalsIgnoreCase(currency);
    }

    private BigDecimal multiplyByRate(String toCurrency, BigDecimal amount) {
        ExchangeRate rate = findRateOrThrow(toCurrency);
        return amount.multiply(rate.getRate()).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal divideByRate(String fromCurrency, BigDecimal amount) {
        ExchangeRate rate = findRateOrThrow(fromCurrency);
        return amount.divide(rate.getRate(), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal convertViaEuro(String fromCurrency, String toCurrency, BigDecimal amount) {
        ExchangeRate fromRate = findRateOrThrow(fromCurrency);
        ExchangeRate toRate = findRateOrThrow(toCurrency);

        BigDecimal eurAmount = amount.divide(fromRate.getRate(), 4, RoundingMode.HALF_EVEN);
        return eurAmount.multiply(toRate.getRate()).setScale(2, RoundingMode.HALF_EVEN);
    }

    private ExchangeRate findRateOrThrow(String currency) {
        return exchangeRatesRepository.findExchangeRateByCurrencyCode(currency).orElseThrow(() -> new CurrencyCodeNotFoundException("Currency code: " + currency + " not found"));
    }
}
