package com.example.banksystem;

import com.example.banksystem.dao.AccountRepository;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.DepositMapper;
import com.example.banksystem.mapper.PersonMapper;
import com.example.banksystem.mapper.WithdrawalMapper;
import com.example.banksystem.service.AccountServiceImpl;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.api.TransactionsService;
import com.example.banksystem.service.dto.AccountDto;
import com.example.banksystem.service.dto.DepositDto;
import com.example.banksystem.service.dto.PersonDto;
import com.example.banksystem.service.dto.WithdrawalDto;
import com.example.banksystem.utils.api.DigestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private DigestService digestService;

    @Mock
    private PersonService personService;

    @Mock
    private TransactionsService transactionsService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private DepositMapper depositMapper;
    @Mock
    private WithdrawalMapper withdrawalMapper;
    @Mock
    private PersonMapper personMapper;

    private AccountServiceImpl accountService;

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl(digestService, transactionsService, accountRepository, personService, accountMapper, depositMapper, withdrawalMapper, personMapper);
    }

    @Test
    public void save_ok() {
        PersonDto personDto = new PersonDto();
        personDto.setPersonId(1L);
        personDto.setLogin("test");

        doReturn(personDto).when(personService).findByLogin("test");

        AccountDto accountDto = new AccountDto();
        accountDto.setPersonDto(personDto);
        String accountNo = accountService.save(accountDto);

        verify(personService, times(1)).findByLogin("test");
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
        assertNotNull(accountNo);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void save_personNotFound() {
        doThrow(BankSystemNotFoundException.class).when(personService).findByLogin("test");

        PersonDto personDto = new PersonDto();
        personDto.setPersonId(1L);
        personDto.setLogin("test");

        AccountDto accountDto = new AccountDto();
        accountDto.setPersonDto(personDto);

        accountService.save(accountDto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void withdrawMoney_accountNotFound() {
        WithdrawalDto dto = new WithdrawalDto();
        dto.setAccountNumber(123L);

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(dto.getAccountNumber());

        String balance = accountService.withdrawalMoney(dto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void withdrawMoney_priceForWithdrawalWrong() {
        AccountEntity entity = new AccountEntity();
        entity.setPinCode("1111");
        entity.setBalance(new BigDecimal("1000"));

        WithdrawalDto dto = new WithdrawalDto();
        dto.setAccountNumber(123L);
        dto.setPinCode("1111");
        dto.setPrice(new BigDecimal("5000"));

        doReturn(Optional.of(entity)).when(accountRepository).findByAccountNumber(dto.getAccountNumber());

        String balance = accountService.withdrawalMoney(dto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void withdrawMoney_pinCodeWrong() {
        AccountEntity entity = new AccountEntity();
        entity.setPinCode("1111");
        entity.setBalance(new BigDecimal("1000"));

        WithdrawalDto dto = new WithdrawalDto();
        dto.setAccountNumber(123L);
        dto.setPinCode("1111");
        dto.setPrice(new BigDecimal("500"));

        doReturn(Optional.of(entity)).when(accountRepository).findByAccountNumber(dto.getAccountNumber());

        String balance = accountService.withdrawalMoney(dto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void depositMoney_accountNotFound() {
        DepositDto dto = new DepositDto();
        dto.setAccountNumber(123L);

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(anyLong());

        String balance = accountService.depositMoney(dto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void findByAccountNumber_notFound() {

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(123L);

        AccountDto byAccountNo = accountService.findByAccountNumber(123L);
    }
}