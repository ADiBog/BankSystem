package com.example.banksystem.controller.api;

import com.example.banksystem.dto.TransactionsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * API для работы с транзакциями.
 */
@Tag(name = "Контроллер для работы с транзакциями.")
public interface TransactionsController {

    @Operation(summary = "Получить все транзакции пользователя")
    List<TransactionsDto> findByUserLogin(String login);
}
