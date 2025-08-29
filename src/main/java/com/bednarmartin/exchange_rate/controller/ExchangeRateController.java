package com.bednarmartin.exchange_rate.controller;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.service.IExchangeRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final IExchangeRatesService exchangeRatesService;

    @GetMapping("/rates")
    public List<ExchangeRatesResponse> getExchangeRates() {
        return exchangeRatesService.getExchangeRates();
    }

    @PostMapping("/convert")
    public ConversionResponse convert(@RequestBody ConversionRequest conversionRequest) {
        return exchangeRatesService.convert(conversionRequest);

    }
}
