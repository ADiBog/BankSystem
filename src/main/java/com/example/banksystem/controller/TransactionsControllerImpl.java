package com.example.banksystem.controller;

import com.example.banksystem.api.TransactionsService;
import com.example.banksystem.controller.api.TransactionsController;
import com.example.banksystem.dto.TransactionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Работа с транзакциями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("transactions")
public class TransactionsControllerImpl  implements TransactionsController {

    private final TransactionsService transactionsService;

    @Override
    @GetMapping("login/{login}")
    public List<TransactionsDto> findByUserLogin(@PathVariable @NonNull String login) {
        return transactionsService.findByUserLogin(login);
    }
}
