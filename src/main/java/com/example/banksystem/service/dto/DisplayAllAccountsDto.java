package com.example.banksystem.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Сервисня модель для отображения всех счетов пользователй из БД.
 */
@Data
@Schema(description = "Сервисня модель для отображения всех счетов пользователй из БД.")
public class DisplayAllAccountsDto {

    /**
     * Логин пользователя.
     */
    @Schema(description = "Логин пользователя.")
    private String login;

    /**
     * Номер счета.
     */
    @Schema(description = "Номер счета.")
    private String accountNo;

    /**
     * Баланс счета.
     */
    @Schema(description = "Баланс счета.")
    private BigDecimal balance;
}
