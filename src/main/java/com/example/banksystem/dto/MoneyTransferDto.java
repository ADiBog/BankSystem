package com.example.banksystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MoneyTransferDto {

    /**
     * Данные о владельце счета с которого выполняется перевод.
     */
    @Valid
    @NonNull
    private PersonDto personDto;

    /**
     * Пин код к счету с которого выполняется перевод.
     */
    @NonNull
    private String pinCode;

    /**
     * Номер счета с которого выполняется перевод.
     */
    @NonNull
    private Long outgoingAccountNumber;

    /**
     * Номер счета для пополнения баланса.
     */
    @NonNull
    private Long incomingAccountNumber;

    /**
     * Сумма перевода.
     */
    @NonNull
    @Min(value = 0)
    private BigDecimal price;
}
