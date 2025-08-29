package com.bednarmartin.exchange_rate.service;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;

import java.util.List;

/**
 * Service interface for handling currency exchange rates and currency conversion operations.
 * <p>
 * Implementations of this interface provide methods to:
 * <ul>
 *     <li>Retrieve the list of all available exchange rates.</li>
 *     <li>Convert a monetary amount from one currency to another.</li>
 * </ul>
 */
public interface IExchangeRatesService {

    /**
     * Retrieves all available exchange rates.
     *
     * @return a list of {@link ExchangeRatesResponse} representing the exchange rates
     *         for all supported currencies.
     */
    List<ExchangeRatesResponse> getExchangeRates();

    /**
     * Converts an amount from one currency to another.
     * <p>
     * The conversion logic is as follows:
     * <ul>
     *     <li>If the source currency is EUR, multiply the amount by the target currency rate.</li>
     *     <li>If the target currency is EUR, divide the amount by the source currency rate.</li>
     *     <li>If neither currency is EUR, first convert the source amount to EUR, then to the target currency.</li>
     * </ul>
     *
     * @param conversionRequest the request containing the source currency, target currency, and amount
     * @return a {@link ConversionResponse} containing the original and converted amount along with the currency codes
     * @throws com.bednarmartin.exchange_rate.exception.CurrencyCodeNotFoundException if either currency code is not found
     */
    ConversionResponse convert(ConversionRequest conversionRequest);
}
