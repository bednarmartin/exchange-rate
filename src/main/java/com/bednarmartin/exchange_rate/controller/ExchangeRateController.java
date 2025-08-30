package com.bednarmartin.exchange_rate.controller;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.dto.response.ConversionResponse;
import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.service.IExchangeRatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
@Tag(name = "Exchange Rates", description = "Operations related to exchange rate conversions")
public class
ExchangeRateController {

    private final IExchangeRatesService exchangeRatesService;

    @GetMapping("/rates")
    @Operation(summary = "Get all exchange rates", description = "Returns a list of supported exchange rates.")
    public ResponseEntity<List<ExchangeRatesResponse>> getExchangeRates() {
        List<ExchangeRatesResponse> exchangeRatesResponse = exchangeRatesService.getExchangeRates();
        return ResponseEntity.ok().body(exchangeRatesResponse);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert currency", description = "Converts an amount from one currency to another.")
    public ResponseEntity<ConversionResponse> convert(@Valid @RequestBody ConversionRequest conversionRequest) {
        ConversionResponse conversionResponse = exchangeRatesService.convert(conversionRequest);
        return ResponseEntity.ok().body(conversionResponse);


    }
}
