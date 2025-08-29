package com.bednarmartin.exchange_rate.mapper;

import com.bednarmartin.exchange_rate.dto.response.ExchangeRatesResponse;
import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    @Mapping(source = "currencyCode", target = "currency")
    ExchangeRatesResponse toExchangeRateResponse(ExchangeRate exchangeRate);
}
