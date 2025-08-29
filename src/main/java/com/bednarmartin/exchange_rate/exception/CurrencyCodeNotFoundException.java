package com.bednarmartin.exchange_rate.exception;

public class CurrencyCodeNotFoundException extends RuntimeException {
    public CurrencyCodeNotFoundException(String currencyCode) {
        super("Currency code: " + currencyCode + " not found");
    }
}
