package com.bednarmartin.exchange_rate.exceptions;

public class CurrencyCodeNotFoundException extends RuntimeException {
    public CurrencyCodeNotFoundException(String message) {
        super(message);
    }
}
