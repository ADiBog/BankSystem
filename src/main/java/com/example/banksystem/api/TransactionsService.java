package com.example.banksystem.api;

import com.example.banksystem.dto.TransactionsDto;

import java.util.List;

/**
 * Работа с транзакциями.
 */
public interface TransactionsService {

    /**
     * Сохранить транзакцию.
     *
     * @param dto данные о транзакции.
     * @return данные сохраненой транзакции.
     */
    TransactionsDto save(TransactionsDto dto);

    /**
     * Получить транзакции по логину пользователя.
     *
     * @param login логин пользователя.
     * @return список транзакции.
     */
    List<TransactionsDto> findByUserLogin(String login);
}
