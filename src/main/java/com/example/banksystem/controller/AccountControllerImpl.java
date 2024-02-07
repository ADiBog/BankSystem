package com.example.banksystem.controller;

import com.example.banksystem.api.AccountService;
import com.example.banksystem.controller.api.AccountController;
import com.example.banksystem.controller.request.CreateAccountRequest;
import com.example.banksystem.controller.request.DepositRequest;
import com.example.banksystem.controller.request.MoneyTransferRequest;
import com.example.banksystem.controller.request.WithdrawalRequest;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.DepositMapper;
import com.example.banksystem.mapper.MoneyTransferMapper;
import com.example.banksystem.mapper.WithdrawalMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер по работе с счетами пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final DepositMapper depositMapper;
    private final MoneyTransferMapper moneyTransferMapper;

    @Override
    @PostMapping("create")
    public ResponseEntity<String> create(@RequestBody @Valid CreateAccountRequest request) {
        String accountNumber = accountService.save(accountMapper.toAccountDto(request));
        return ResponseEntity.ok("Счет создан. Номер счета " + accountNumber);
    }

    @Override
    @PostMapping("withdrawal-money")
    public ResponseEntity<String> withdrawalMoney(@RequestBody @Valid WithdrawalRequest request) {
        String balance = accountService.withdrawalMoney(withdrawalMapper.toWithdrawalDto(request));
        return ResponseEntity.ok("Успешное снятие. Остаток на счете: " + balance);
    }

    @Override
    @PostMapping("deposit-money")
    public ResponseEntity<String> depositMoney(@RequestBody @Valid DepositRequest request) {
        String balance = accountService.depositMoney(depositMapper.toDepositDto(request));
        return ResponseEntity.ok("Успешное пополнение. Остаток на счете: " + balance);
    }

    @Override
    @PostMapping("money-transfer")
    public ResponseEntity<String> moneyTransfer(@RequestBody @Valid MoneyTransferRequest request) {
        String balance = accountService.transferMoney(moneyTransferMapper.toMoneyTransferDto(request));
        return ResponseEntity.ok("Успешный перевод. Остаток на счете: " + balance);
    }
}