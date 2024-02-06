package com.example.banksystem.controller;

import com.example.banksystem.controller.api.AccountController;
import com.example.banksystem.controller.request.CreateAccountRequest;
import com.example.banksystem.controller.request.DepositRequest;
import com.example.banksystem.controller.request.MoneyTransferRequest;
import com.example.banksystem.controller.request.WithdrawalRequest;
import com.example.banksystem.service.api.AccountService;
import com.example.banksystem.service.dto.*;
import com.example.banksystem.utils.ModelMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер по работе с счетами пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountControllerImpl implements AccountController {

    private AccountService accountService;

    @Autowired
    public AccountControllerImpl(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @PostMapping("create")
    public ResponseEntity<String> create(@RequestBody @Valid CreateAccountRequest request) {
        String accountNumber = accountService.save(ModelMapperUtils.map(request, AccountDto.class));
        return ResponseEntity.ok("Счет создан. Номер счета " + accountNumber);
    }

    @Override
    @PostMapping("withdrawal-money")
    public ResponseEntity<String> withdrawalMoney(@RequestBody @Valid WithdrawalRequest request) {
        String balance = accountService.withdrawalMoney(ModelMapperUtils.map(request, WithdrawalDto.class));
        return ResponseEntity.ok("Успешное снятие. Остаток на счете: " + balance);
    }

    @Override
    @PostMapping("deposit-money")
    public ResponseEntity<String> depositMoney(@RequestBody @Valid DepositRequest request) {
        String balance = accountService.depositMoney(ModelMapperUtils.map(request, DepositDto.class));
        return ResponseEntity.ok("Успешное пополнение. Остаток на счете: " + balance);
    }

    @Override
    @PostMapping("money-transfer")
    public ResponseEntity<String> moneyTransfer(@RequestBody @Valid MoneyTransferRequest request) {
        String balance = accountService.transferMoney(ModelMapperUtils.map(request, MoneyTransferDto.class));
        return ResponseEntity.ok("Успешный перевод. Остаток на счете: " + balance);
    }
}