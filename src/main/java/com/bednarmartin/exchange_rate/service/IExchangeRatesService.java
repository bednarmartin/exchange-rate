package com.bednarmartin.exchange_rate.service;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;

import java.util.List;

public interface IExchangeRatesService {

    List<ExchangeRatesResponse> getExchangeRates();

    ConversionResponse convert(ConversionRequest conversionRequest);
}
