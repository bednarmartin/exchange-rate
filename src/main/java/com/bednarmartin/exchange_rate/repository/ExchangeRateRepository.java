package com.bednarmartin.exchange_rate.repository;

import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {

    Optional<ExchangeRate> findExchangeRateByCurrencyCode(String currencyCode);
}
