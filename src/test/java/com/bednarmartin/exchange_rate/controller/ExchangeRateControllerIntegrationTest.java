package com.bednarmartin.exchange_rate.controller;

import com.bednarmartin.exchange_rate.dto.request.ConversionRequest;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import com.bednarmartin.exchange_rate.repository.ExchangeRateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ExchangeRateControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final ExchangeRateRepository exchangeRateRepository;

    private final ObjectMapper objectMapper;

    private final String BASE_URL = "/api/exchange/";

    private final String RATES_URL = "rates";

    private final String CONVERT_URL = "convert";

    @BeforeEach
    void setup() {
        exchangeRateRepository.deleteAll();
        exchangeRateRepository.save(new ExchangeRate("USD", BigDecimal.valueOf(1.12)));
        exchangeRateRepository.save(new ExchangeRate("GBP", BigDecimal.valueOf(0.85)));
    }

    @Test
    void testGetExchangeRate() throws Exception {
        mockMvc.perform(get(BASE_URL + RATES_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].rate").value(1.12))
                .andExpect(jsonPath("$[1].currency").value("GBP"))
                .andExpect(jsonPath("$[1].rate").value(0.85));
    }

    @Test
    void testConvert_OK() throws Exception {
        ConversionRequest request = new ConversionRequest("EUR", "USD", BigDecimal.valueOf(100));
        mockMvc.perform(post(BASE_URL + CONVERT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fromCurrency").value("EUR"))
                .andExpect(jsonPath("$.toCurrency").value("USD"))
                .andExpect(jsonPath("$.originalAmount").value(100))
                .andExpect(jsonPath("$.convertedAmount").value(112));
    }

    @Test
    void testConvert_NotFound() throws Exception {
        ConversionRequest request = new ConversionRequest("CZK", "USD", BigDecimal.valueOf(100));
        mockMvc.perform(post(BASE_URL + CONVERT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testConvert_BadRequest_NegativeAmount() throws Exception {
        ConversionRequest request = new ConversionRequest("CZK", "USD", BigDecimal.valueOf(-100));
        mockMvc.perform(post(BASE_URL + CONVERT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.errorMessages[0]").value("Amount must be greater than 0"));
    }

    @Test
    void testConvert_BadRequest_BlankFromCurrency() throws Exception {
        ConversionRequest request = new ConversionRequest("", "USD", BigDecimal.valueOf(100));
        mockMvc.perform(post(BASE_URL + CONVERT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void testConvert_BadRequest_BadToCurrency() throws Exception {
        ConversionRequest request = new ConversionRequest("USD", "USDc", BigDecimal.valueOf(100));
        mockMvc.perform(post(BASE_URL + CONVERT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void testConvert_BadRequest_BadUrl() throws Exception {
        mockMvc.perform(get(BASE_URL + RATES_URL + "/X"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.errorMessages[0]").value("No endpoint GET " + BASE_URL + RATES_URL + "/X."));
    }}
