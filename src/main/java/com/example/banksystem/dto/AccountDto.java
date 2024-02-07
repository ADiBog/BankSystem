package com.example.banksystem.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Сервсианая модель с данными о счете пользователя.
 */
@Data
public class AccountDto {

    /**
     * Идентификатор счета.
     */
    private Long accountId;

    /**
     * Номер счета.
     */
    private Long accountNumber;

    /**
     * Баланс счета.
     */
    private BigDecimal balance;

    /**
     * Пин код счета.
     */
    private String pinCode;

    /**
     *  Данные о владельце счета.
     */
    private PersonDto personDto;
}
