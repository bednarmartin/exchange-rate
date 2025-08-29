package com.bednarmartin.exchange_rate.repository;

import com.bednarmartin.exchange_rate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for accessing {@link ExchangeRate} entities from the database.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 * Contains custom query methods for retrieving exchange rates by currency code.
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {

    /**
     * Finds an exchange rate by its currency code.
     *
     * @param currencyCode the ISO currency code (e.g., "USD", "EUR") of the exchange rate
     * @return an {@link Optional} containing the {@link ExchangeRate} if found, or empty if not found
     */
    Optional<ExchangeRate> findExchangeRateByCurrencyCode(String currencyCode);
}
