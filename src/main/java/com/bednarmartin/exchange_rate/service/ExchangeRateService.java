package com.bednarmartin.exchange_rate.service;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import com.bednarmartin.exchange_rate.exception.CurrencyCodeNotFoundException;
import com.bednarmartin.exchange_rate.mapper.ExchangeRateMapper;
import com.bednarmartin.exchange_rate.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
class ExchangeRateService implements IExchangeRatesService {

    private final ExchangeRateRepository exchangeRatesRepository;

    private final ExchangeRateMapper exchangeRateMapper;

    @Value("${currency.base}")
    private String baseCurrency;

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

        convertedAmount = getConvertedAmount(from, to, amount);

        return new ConversionResponse(from, to, amount, convertedAmount);
    }

    private BigDecimal getConvertedAmount(String from, String to, BigDecimal amount) {
        if(from.equals(to)) {
            return amount;
        }
        if (isBaseCurrency(from)) {
            return multiplyByRate(to, amount);
        }
        if (isBaseCurrency(to)) {
            return divideByRate(from, amount);
        }
        return convertViaEuro(from, to, amount);
    }

    private boolean isBaseCurrency(String currency) {
        return baseCurrency != null ? baseCurrency.equals(currency) : "EUR".equals(currency);
    }

    private BigDecimal multiplyByRate(String currency, BigDecimal amount) {
        ExchangeRate rate = findRate(currency);
        return amount.multiply(rate.getRate()).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal divideByRate(String currency, BigDecimal amount) {
        ExchangeRate rate = findRate(currency);
        return amount.divide(rate.getRate(), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal convertViaEuro(String fromCurrency, String toCurrency, BigDecimal amount) {
        ExchangeRate fromRate = findRate(fromCurrency);
        ExchangeRate toRate = findRate(toCurrency);

        BigDecimal eurAmount = amount.divide(fromRate.getRate(), 4, RoundingMode.HALF_EVEN);
        return eurAmount.multiply(toRate.getRate()).setScale(2, RoundingMode.HALF_EVEN);
    }

    private ExchangeRate findRate(String currency) {
        return exchangeRatesRepository.findExchangeRateByCurrencyCode(currency)
                .orElseThrow(() -> new CurrencyCodeNotFoundException(currency));
    }
}
