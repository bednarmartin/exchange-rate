package com.bednarmartin.exchange_rate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyCodeNotFoundException.class)
    public ResponseEntity<String> handleCurrencyCodeNotFound(CurrencyCodeNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>("An unexpected exception occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
