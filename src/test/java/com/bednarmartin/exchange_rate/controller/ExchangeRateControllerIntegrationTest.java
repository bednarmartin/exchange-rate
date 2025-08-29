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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExchangeRateControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final ExchangeRateRepository exchangeRateRepository;

    private final ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        exchangeRateRepository.deleteAll();
        exchangeRateRepository.save(new ExchangeRate("USD", BigDecimal.valueOf(1.12)));
        exchangeRateRepository.save(new ExchangeRate("GBP", BigDecimal.valueOf(0.85)));
    }

    @Test
    public void testGetExchangeRate() throws Exception {
        mockMvc.perform(get("/api/exchange/rates")
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
        mockMvc.perform(post("/api/exchange/convert")
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
        mockMvc.perform(post("/api/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
