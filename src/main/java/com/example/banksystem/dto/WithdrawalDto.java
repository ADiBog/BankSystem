package com.example.banksystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Сервисная модель на снятие денежных средств.
 */
@Data
@NoArgsConstructor
public class WithdrawalDto {

    /**
     * Данные о владельце счета.
     */
    private PersonDto personDto;

    /**
     * Пин код к счету.
     */
    private String pinCode;

    /**
     * Номер счета.
     */
    private Long accountNumber;

    /**
     * Сумма снятия со счета.
     */
    private BigDecimal price;
}