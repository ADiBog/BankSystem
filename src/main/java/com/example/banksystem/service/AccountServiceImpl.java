package com.example.banksystem.service;

import com.example.banksystem.dao.AccountRepository;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.exception.BankSystemIllegalArgumentException;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.service.api.AccountService;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.api.TransactionsService;
import com.example.banksystem.service.dto.*;
import com.example.banksystem.utils.ModelMapperUtils;
import com.example.banksystem.utils.api.DigestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Реализация сервиса по работе с счетами пользователей.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final DigestService digestService;
    private final PersonService personService;
    private final TransactionsService transactionsService;
    private final AccountRepository accountRepository;

    @Override
    public List<DisplayAllAccountsDto> findAll() {
        return ModelMapperUtils.mapAll(accountRepository.findAll(), DisplayAllAccountsDto.class);
    }

    @Override
    public String save(AccountDto dto) {

        //нет проверки на нул т.к проверка осуществялется в контроллере при получения аргументов.
        String login = dto.getPersonDto().getLogin();
        PersonDto personDto = personService.findByLogin(login);

        AccountEntity accountEntity = buildAccountEntity(personDto, dto.getPinCode());
        accountRepository.save(accountEntity);
        return accountEntity.getAccountNo().toString();
    }

    @Override
    @Transactional
    public String withdrawalMoney(WithdrawalDto dto) {

        Long accountNo = dto.getAccountNo();
        AccountDto selectedAccount = findByAccountNo(accountNo);

        String pinCodeFromDb = selectedAccount.getPinCode();
        checkEqualsPinCodes(pinCodeFromDb, dto.getPinCode());

        BigDecimal balanceOnAccount = selectedAccount.getBalance();
        BigDecimal withdrawalAmount = dto.getPrice();
        checkAvailableBalanceForWithdrawal(balanceOnAccount, withdrawalAmount);

        AccountDto accountAfterWithdrawal = getAccountDto(accountNo, balanceOnAccount.subtract(withdrawalAmount));
        updateBalance(accountAfterWithdrawal);

        TransactionsDto transactionDto = getTransactionDto(selectedAccount.getAccountId(), withdrawalAmount.multiply(new BigDecimal("-1")));
        transactionsService.save(transactionDto);

        return accountAfterWithdrawal.getBalance().toString();
    }

    @Override
    @Transactional
    public String depositMoney(DepositDto dto) {
        Long accountNo = dto.getAccountNo();
        AccountDto selectedAccount = findByAccountNo(accountNo);

        BigDecimal balanceOnAccount = selectedAccount.getBalance();
        BigDecimal withdrawalAmount = dto.getPrice();

        AccountDto accountAfterDeposit = getAccountDto(accountNo, balanceOnAccount.add(withdrawalAmount));
        updateBalance(accountAfterDeposit);

        TransactionsDto transactionDto = getTransactionDto(selectedAccount.getAccountId(), withdrawalAmount);
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
    public AccountDto findByAccountNo(Long accountNo) {
        return accountRepository.findByAccountNo(accountNo)
                .map(entity -> ModelMapperUtils.map(entity, AccountDto.class))
                .orElseThrow(() ->
                        new BankSystemNotFoundException(String.format("Счет с номером %s не найден", accountNo)));
    }

    @Override
    public void updateBalance(AccountDto dto) {
        accountRepository.updateBalance(ModelMapperUtils.map(dto, AccountEntity.class));
    }

    private AccountEntity buildAccountEntity(PersonDto personDto, String pinCode) {
        PersonEntity map = ModelMapperUtils.map(personService.findById(personDto.getPersonId()), PersonEntity.class);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setPerson(map);
        accountEntity.setAccountNo(getAccountNumber());
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

    private AccountDto getAccountDto(Long accountNo, BigDecimal balance) {
        AccountDto account = new AccountDto();
        account.setAccountNo(accountNo);
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
        WithdrawalDto withdrawMoneyDto = ModelMapperUtils.map(dto, WithdrawalDto.class);
        withdrawMoneyDto.setAccountNo(dto.getAccountNoFrom());
        return withdrawMoneyDto;
    }

    private DepositDto getDepositMoneyDto(MoneyTransferDto dto) {
        DepositDto depositMoneyDto = ModelMapperUtils.map(dto, DepositDto.class);
        depositMoneyDto.setAccountNo(dto.getAccountNoTo());
        return depositMoneyDto;
    }
}
