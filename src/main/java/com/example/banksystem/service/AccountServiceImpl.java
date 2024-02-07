package com.example.banksystem.service;

import com.example.banksystem.dao.AccountRepository;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.exception.BankSystemIllegalArgumentException;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.DepositMapper;
import com.example.banksystem.mapper.PersonMapper;
import com.example.banksystem.mapper.WithdrawalMapper;
import com.example.banksystem.service.api.AccountService;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.api.TransactionsService;
import com.example.banksystem.service.dto.*;
import com.example.banksystem.utils.api.DigestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Random;

/**
 * Реализация сервиса по работе с счетами пользователей.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final DigestService digestService;
    private final TransactionsService transactionsService;
    private final AccountRepository accountRepository;
    private final PersonService personService;
    private final AccountMapper accountMapper;
    private final DepositMapper depositMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final PersonMapper personMapper;

    @Override
    public String save(AccountDto dto) {

        //нет проверки на нул т.к проверка осуществялется в контроллере при получения аргументов.
        String login = dto.getPersonDto().getLogin();
        PersonDto personDto = personService.findByLogin(login);

        AccountEntity accountEntity = buildAccountEntity(personDto, dto.getPinCode());
        accountRepository.save(accountEntity);
        return accountEntity.getAccountNumber().toString();
    }

    @Override
    @Transactional
    public String withdrawalMoney(WithdrawalDto dto) {

        Long accountNumber = dto.getAccountNumber();
        AccountDto selectedAccount = findByAccountNumber(accountNumber);

        String pinCodeFromDb = selectedAccount.getPinCode();
        checkEqualsPinCodes(pinCodeFromDb, dto.getPinCode());

        BigDecimal balanceOnAccount = selectedAccount.getBalance();
        BigDecimal withdrawalAmount = dto.getPrice();
        checkAvailableBalanceForWithdrawal(balanceOnAccount, withdrawalAmount);

        AccountDto accountAfterWithdrawal = getAccountDto(selectedAccount.getAccountId(), accountNumber, balanceOnAccount.subtract(withdrawalAmount));
        updateBalance(accountAfterWithdrawal.getAccountId(), accountAfterWithdrawal.getBalance());

        TransactionsDto transactionDto = getTransactionDto(selectedAccount.getAccountId(), withdrawalAmount.multiply(new BigDecimal("-1")));
        transactionsService.save(transactionDto);

        return accountAfterWithdrawal.getBalance().toString();
    }

    @Override
    @Transactional
    public String depositMoney(DepositDto dto) {
        Long accountNumber = dto.getAccountNumber();
        AccountDto selectedAccount = findByAccountNumber(accountNumber);

        BigDecimal balanceOnAccount = selectedAccount.getBalance();
        BigDecimal replenishmentAmount = dto.getReplenishmentAmount();

        balanceOnAccount = balanceOnAccount.add(replenishmentAmount);
        AccountDto accountAfterDeposit = getAccountDto(selectedAccount.getAccountId(), accountNumber, balanceOnAccount);
        updateBalance(accountAfterDeposit.getAccountId(), accountAfterDeposit.getBalance());

        TransactionsDto transactionDto = getTransactionDto(selectedAccount.getAccountId(), replenishmentAmount);
        transactionsService.save(transactionDto);

        return accountAfterDeposit.getBalance().toString();
    }

    @Override
    @Transactional
    public String transferMoney(MoneyTransferDto dto) {
        WithdrawalDto withdrawMoneyDto = getWithdrawMoneyDto(dto);
        DepositDto depositMoneyDto = getDepositMoneyDto(dto);

        String balanceAfterWithdraw = withdrawalMoney(withdrawMoneyDto);
        depositMoney(depositMoneyDto);

        return balanceAfterWithdraw;
    }

    @Override
    public AccountDto findByAccountNumber(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toAccountDto)
                .orElseThrow(() ->
                        new BankSystemNotFoundException(String.format("Счет с номером %s не найден", accountNumber)));
    }

    @Override
    public void updateBalance(Long accountId, BigDecimal balance) {
        accountRepository.updateBalance(accountId, balance);
    }

    private AccountEntity buildAccountEntity(PersonDto personDto, String pinCode) {
        PersonDto byId = personService.findById(personDto.getPersonId());
        PersonEntity map = personMapper.toPersonEntity(byId);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setPerson(map);
        accountEntity.setAccountNumber(getAccountNumber());
        accountEntity.setPinCode(getHashPinCode(pinCode));
        accountEntity.setBalance(new BigDecimal("0.00"));

        return accountEntity;
    }

    private Long getAccountNumber() {
        long accountNumber = new Random().nextLong();
        return Math.abs(accountNumber);
    }

    private String getHashPinCode(String pinCode) {
        return digestService.hash(pinCode);
    }

    private void checkEqualsPinCodes(String codeFromDb, String codeFromRequest) {
        String hashPinCodeRequest = digestService.hash(codeFromRequest);
        if (!Objects.equals(codeFromDb, hashPinCodeRequest)) {
            throw new BankSystemIllegalArgumentException(
                    String.format("Введен неверный пароль %s!", codeFromRequest));
        }
    }

    private void checkAvailableBalanceForWithdrawal(BigDecimal balanceOnAccount, BigDecimal withdrawalAmount) {
        if (balanceOnAccount.compareTo(withdrawalAmount) < 0) {
            throw new BankSystemIllegalArgumentException(
                    String.format("Сумма для снятия %s превышает текущий остаток %s!", withdrawalAmount, balanceOnAccount));
        }
    }

    private AccountDto getAccountDto(Long accountId, Long accountNumber, BigDecimal balance) {
        AccountDto account = new AccountDto();
        account.setAccountId(accountId);
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        return account;
    }

    private TransactionsDto getTransactionDto(Long accountId, BigDecimal price) {
        TransactionsDto transactionsDto = new TransactionsDto();
        transactionsDto.setAccountId(accountId);
        transactionsDto.setPrice(price);
        transactionsDto.setCreateDttm(OffsetDateTime.now());
        return transactionsDto;
    }

    private WithdrawalDto getWithdrawMoneyDto(MoneyTransferDto dto) {
        WithdrawalDto withdrawalMoneyDto = withdrawalMapper.toWithdrawalDto(dto);
        withdrawalMoneyDto.setAccountNumber(dto.getOutgoingAccountNumber());
        return withdrawalMoneyDto;
    }

    private DepositDto getDepositMoneyDto(MoneyTransferDto dto) {
        DepositDto depositMoneyDto = depositMapper.toDepositDto(dto);
        depositMoneyDto.setAccountNumber(dto.getIncomingAccountNumber());
        depositMoneyDto.setReplenishmentAmount(dto.getPrice());
        return depositMoneyDto;
    }
}
