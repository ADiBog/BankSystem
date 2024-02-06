package com.example.banksystem.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исклчение при отсутсвия объекта при поиске.
 * Возвращает статус 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankSystemNotFoundException extends RuntimeException {
    public BankSystemNotFoundException(String message) {
        super(message);
    }
}
