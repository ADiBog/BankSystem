package com.example.banksystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение для выбрасывания в случаее некооректных переданных аргументов.
 * Возвращает статус 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BankSystemIllegalArgumentException extends RuntimeException {
    public BankSystemIllegalArgumentException(String message) {
        super(message);
    }
}
