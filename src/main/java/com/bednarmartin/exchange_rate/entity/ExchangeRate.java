package com.bednarmartin.exchange_rate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "EXCHANGE_RATE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @Column(name = "CURRENCY_CODE", length = 3, nullable = false, unique = true)
    private String currencyCode;

    @Column(name = "RATE", nullable = false)
    private BigDecimal rate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return currencyCode != null && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currencyCode);
    }
}
