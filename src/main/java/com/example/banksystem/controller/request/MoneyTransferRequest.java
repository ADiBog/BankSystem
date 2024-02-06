package com.example.banksystem.controller.request;

import com.example.banksystem.service.dto.PersonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Перевод средств между счетами.
 */

@Data
@NoArgsConstructor
@Schema(description = "Перевод средств между счетами")
public class MoneyTransferRequest {
    /**
     * Данные владельца счета с которого выполняется перевод
     */
    @Valid
    @NonNull
    @Schema(description = "Данные владельца счета с которого выполняется перевод")
    private PersonDto personDto;

    /**
     * Пин код счета
     */
    @NonNull
    @Schema(description = "Пин код счета")
    private String pinCode;

    /**
     * Номер счета с которого выполняется перевод.
     */
    @NonNull
    @Schema(description = "Номер счета с которого выполняется перевод.")
    private Long outgoingAccountNumber;

    /**
     * Номер счета для пополнения баланса.
     */
    @NonNull
    @Schema(description = "Номер счета для пополнения баланса.")
    private Long incomingAccountNumber;

    /**
     * Сумма перевода.
     */
    @NonNull
    @Min(value = 0)
    @Schema(description = "Сумма перевода.")
    private BigDecimal price;
}
