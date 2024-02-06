package com.example.banksystem.service.api;

import com.example.banksystem.service.dto.AccountDto;
import com.example.banksystem.service.dto.DepositDto;
import com.example.banksystem.service.dto.MoneyTransferDto;
import com.example.banksystem.service.dto.WithdrawalDto;

import java.math.BigDecimal;

/**
 * Работа со счетами.
 */
public interface AccountService {

    /**
     * Создать новый счет.
     *
     * @param dto Данные для создания счета.
     * @return номер созданного счета.
     */
    String save(AccountDto dto);

    /**
     * Снять деньги со счета.
     *
     * @param dto Данные для снятия с счета.
     * @return Остаток на счете после снятия.
     */
    String withdrawalMoney(WithdrawalDto dto);

    /**
     * Пополнить счет.
     *
     * @param dto Данные для пополнения счета (номер счета и сумма).
     * @return Остаток на счете после пополнения.
     */
    String depositMoney(DepositDto dto);

    /**
     * Перевести деньги на другой счет.
     *
     * @param dto Модель с данными для перевода средств с одного счета на другой.
     * @return Остаток на счете, с оторого выполнялся перевод.
     */
    String transferMoney(MoneyTransferDto dto);

    /**
     * Найти счет по его номеру.
     *
     * @param accountNumber Номер счета.
     * @return Данные о счете.
     */
    AccountDto findByAccountNumber(Long accountNumber);

    /**
     * Обновить баланс счета по его номеру.
     *
     * @param accountId id аккаунта.
     * @param accountId баланс аккаунта.
     */
    void updateBalance(Long accountId, BigDecimal balance);
}
