package com.example.banksystem.controller.api;

import com.example.banksystem.controller.request.CreateAccountRequest;
import com.example.banksystem.controller.request.DepositRequest;
import com.example.banksystem.controller.request.MoneyTransferRequest;
import com.example.banksystem.controller.request.WithdrawalRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * API для работы со счетами.
 */
@Tag(name = "Контроллер для работы со счетами")
public interface AccountController {

    @Operation(summary = "Создать счет")
    ResponseEntity<String> create(CreateAccountRequest request);

    @Operation(summary = "Снять со счета")
    ResponseEntity<String> withdrawalMoney(WithdrawalRequest request);

    @Operation(summary = "Внести на счет")
    ResponseEntity<String> depositMoney(DepositRequest request);

    @Operation(summary = "Перевести деньги")
    ResponseEntity<String> moneyTransfer(MoneyTransferRequest request);
}
