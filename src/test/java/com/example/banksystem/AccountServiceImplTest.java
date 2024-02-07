package com.example.banksystem;

import com.example.banksystem.dao.AccountRepository;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.exception.BankSystemIllegalArgumentException;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.DepositMapper;
import com.example.banksystem.mapper.PersonMapper;
import com.example.banksystem.mapper.WithdrawalMapper;
import com.example.banksystem.service.AccountServiceImpl;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.api.TransactionsService;
import com.example.banksystem.service.dto.*;
import com.example.banksystem.utils.api.DigestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void withdrawMoney() {
        AccountEntity selectedAccount = new AccountEntity();
        selectedAccount.setPinCode("1111");
        selectedAccount.setBalance(new BigDecimal("1000"));
        selectedAccount.setAccountId(1L);

        WithdrawalDto WithdrawalDto = new WithdrawalDto();
        WithdrawalDto.setAccountNumber(123L);
        WithdrawalDto.setPinCode("1111");
        WithdrawalDto.setPrice(new BigDecimal("500"));

        doReturn(Optional.of(selectedAccount)).when(accountRepository).findByAccountNumber(WithdrawalDto.getAccountNumber());
        doReturn(selectedAccount.getPinCode()).when(digestService).hash(WithdrawalDto.getPinCode());

        String balanceAfterWithdraw = accountService.withdrawalMoney(WithdrawalDto);

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        verify(digestService, times(1)).hash(WithdrawalDto.getPinCode());
        verify(accountRepository, times(1)).updateBalance(1L, BigDecimal.valueOf(500));
        verify(transactionsService, times(1)).save(any(TransactionsDto.class));
        assertNotNull(balanceAfterWithdraw);
        assertEquals(balanceAfterWithdraw, new BigDecimal("500").toString());
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
        doReturn("1111").when(digestService).hash(dto.getPinCode());

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
        doReturn("2222").when(digestService).hash(dto.getPinCode());

        String balance = accountService.withdrawalMoney(dto);
    }

    @Test
    public void depositMoney() {
        AccountEntity selectedAccount = new AccountEntity();
        selectedAccount.setPinCode("1111");
        selectedAccount.setBalance(new BigDecimal("1000"));
        selectedAccount.setAccountId(1L);

        DepositDto depositAccount = new DepositDto();
        depositAccount.setAccountNumber(123L);
        depositAccount.setReplenishmentAmount(new BigDecimal("500"));

        doReturn(Optional.of(selectedAccount)).when(accountRepository).findByAccountNumber(depositAccount.getAccountNumber());

        String balanceAfterDeposit = accountService.depositMoney(depositAccount);

        verify(accountRepository, times(1)).findByAccountNumber(depositAccount.getAccountNumber());
        verify(accountRepository, times(1)).updateBalance(1L, BigDecimal.valueOf(1500));
        verify(transactionsService, times(1)).save(any(TransactionsDto.class));
        assertNotNull(balanceAfterDeposit);
        assertEquals(balanceAfterDeposit, new BigDecimal("1500").toString());
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void depositMoney_accountNotFound() {
        DepositDto dto = new DepositDto();
        dto.setAccountNumber(123L);

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(anyLong());

        String balance = accountService.depositMoney(dto);
    }

    @Test
    public void transferMoney() {
        BigDecimal price = new BigDecimal("500");
        Long accountNoFrom = 123L;
        Long accountNoTo = 555L;
        String pinCode = "1111";

        AccountEntity accountForWithdraw = new AccountEntity();
        accountForWithdraw.setPinCode("1111");
        accountForWithdraw.setBalance(new BigDecimal("1000"));
        accountForWithdraw.setAccountId(1L);

        WithdrawalDto WithdrawalDto = new WithdrawalDto();
        WithdrawalDto.setAccountNumber(accountNoFrom);
        WithdrawalDto.setPinCode(pinCode);
        WithdrawalDto.setPrice(price);

        doReturn(Optional.of(accountForWithdraw)).when(accountRepository).findByAccountNumber(WithdrawalDto.getAccountNumber());
        doReturn(accountForWithdraw.getPinCode()).when(digestService).hash(WithdrawalDto.getPinCode());

        AccountEntity accountForDeposit = new AccountEntity();

        accountForDeposit.setBalance(new BigDecimal("5000"));

        DepositDto DepositDto = new DepositDto();
        DepositDto.setAccountNumber(accountNoTo);
        DepositDto.setReplenishmentAmount(price);

        doReturn(Optional.of(accountForDeposit)).when(accountRepository).findByAccountNumber(DepositDto.getAccountNumber());

        MoneyTransferDto MoneyTransferDto = new MoneyTransferDto();
        MoneyTransferDto.setPinCode(pinCode);
        MoneyTransferDto.setOutgoingAccountNumber(accountNoFrom);
        MoneyTransferDto.setIncomingAccountNumber(accountNoTo);
        MoneyTransferDto.setPrice(price);

        String balanceAfterWithdraw = accountService.transferMoney(MoneyTransferDto);

        verify(accountRepository, times(1)).findByAccountNumber(WithdrawalDto.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber(DepositDto.getAccountNumber());
        verify(digestService, times(1)).hash(WithdrawalDto.getPinCode());
        verify(accountRepository, times(1)).updateBalance(1L, BigDecimal.valueOf(500));
        verify(transactionsService, times(2)).save(any(TransactionsDto.class));

        assertNotNull(balanceAfterWithdraw);
        assertEquals(balanceAfterWithdraw, new BigDecimal("500").toString());
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void transferMoney_accountFoDepositNotFound() {
        BigDecimal price = new BigDecimal("500");
        Long accountNoFrom = 123L;
        Long accountNoTo = 555L;
        String pinCode = "1111";

        AccountEntity accountForWithdraw = new AccountEntity();
        accountForWithdraw.setPinCode("1111");
        accountForWithdraw.setBalance(new BigDecimal("1000"));

        WithdrawalDto WithdrawalDto = new WithdrawalDto();
        WithdrawalDto.setAccountNumber(accountNoFrom);
        WithdrawalDto.setPinCode(pinCode);
        WithdrawalDto.setPrice(price);

        doReturn(Optional.of(accountForWithdraw)).when(accountRepository).findByAccountNumber(WithdrawalDto.getAccountNumber());
        doReturn(accountForWithdraw.getPinCode()).when(digestService).hash(WithdrawalDto.getPinCode());

        AccountEntity accountForDeposit = new AccountEntity();

        accountForDeposit.setBalance(new BigDecimal("5000"));

        DepositDto DepositDto = new DepositDto();
        DepositDto.setAccountNumber(accountNoTo);
        DepositDto.setReplenishmentAmount(price);

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(DepositDto.getAccountNumber());

        MoneyTransferDto MoneyTransferDto = new MoneyTransferDto();
        MoneyTransferDto.setPinCode(pinCode);
        MoneyTransferDto.setOutgoingAccountNumber(accountNoFrom);
        MoneyTransferDto.setIncomingAccountNumber(accountNoTo);
        MoneyTransferDto.setPrice(price);

        String balanceAfterWithdraw = accountService.transferMoney(MoneyTransferDto);
    }

    @Test(expected = BankSystemIllegalArgumentException.class)
    public void transferMoney_pinCodeForAccountWithdrawWrong() {
        BigDecimal price = new BigDecimal("5000");
        Long accountNoFrom = 123L;
        Long accountNoTo = 555L;
        String pinCode = "1111";

        AccountEntity accountForWithdraw = new AccountEntity();
        accountForWithdraw.setPinCode("1111");
        accountForWithdraw.setBalance(new BigDecimal("1000"));

        WithdrawalDto WithdrawalDto = new WithdrawalDto();
        WithdrawalDto.setAccountNumber(accountNoFrom);
        WithdrawalDto.setPinCode(pinCode);
        WithdrawalDto.setPrice(price);

        doReturn(Optional.of(accountForWithdraw)).when(accountRepository).findByAccountNumber(WithdrawalDto.getAccountNumber());
        doReturn("2222").when(digestService).hash(WithdrawalDto.getPinCode());

        MoneyTransferDto MoneyTransferDto = new MoneyTransferDto();
        MoneyTransferDto.setPinCode(pinCode);
        MoneyTransferDto.setOutgoingAccountNumber(accountNoFrom);
        MoneyTransferDto.setIncomingAccountNumber(accountNoTo);
        MoneyTransferDto.setPrice(price);

        String balanceAfterWithdraw = accountService.transferMoney(MoneyTransferDto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void transferMoney_accountWithdrawNotFound() {

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(123L);

        MoneyTransferDto MoneyTransferDto = new MoneyTransferDto();
        MoneyTransferDto.setPrice(BigDecimal.valueOf(1000));
        MoneyTransferDto.setOutgoingAccountNumber(123L);
        MoneyTransferDto.setIncomingAccountNumber(555L);

        String balanceAfterWithdraw = accountService.transferMoney(MoneyTransferDto);
    }

    @Test(expected = BankSystemIllegalArgumentException.class)
    public void transferMoney_priceForWithdrawalWrong() {
        BigDecimal price = new BigDecimal("5000");
        Long accountNoFrom = 123L;
        Long accountNoTo = 555L;
        String pinCode = "1111";

        AccountEntity accountForWithdraw = new AccountEntity();
        accountForWithdraw.setPinCode("1111");
        accountForWithdraw.setBalance(new BigDecimal("1000"));

        WithdrawalDto WithdrawalDto = new WithdrawalDto();
        WithdrawalDto.setAccountNumber(accountNoFrom);
        WithdrawalDto.setPinCode(pinCode);
        WithdrawalDto.setPrice(price);

        doReturn(Optional.of(accountForWithdraw)).when(accountRepository).findByAccountNumber(WithdrawalDto.getAccountNumber());
        doReturn(accountForWithdraw.getPinCode()).when(digestService).hash(WithdrawalDto.getPinCode());

        MoneyTransferDto MoneyTransferDto = new MoneyTransferDto();
        MoneyTransferDto.setPinCode(pinCode);
        MoneyTransferDto.setOutgoingAccountNumber(accountNoFrom);
        MoneyTransferDto.setIncomingAccountNumber(accountNoTo);
        MoneyTransferDto.setPrice(price);

        String balanceAfterWithdraw = accountService.transferMoney(MoneyTransferDto);
    }

    @Test
    public void findByAccountNumber() {

        doReturn(Optional.of(new AccountEntity())).when(accountRepository).findByAccountNumber(123L);

        AccountDto byAccountNumber = accountService.findByAccountNumber(123L);

        verify(accountRepository, times(1)).findByAccountNumber(123L);
        assertNotNull(byAccountNumber);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void findByAccountNumber_notFound() {

        doReturn(Optional.empty()).when(accountRepository).findByAccountNumber(123L);

        AccountDto byAccountNo = accountService.findByAccountNumber(123L);
    }

    /*@Test
    public void updateBalance() {
        AccountDto dto = new AccountDto();
        dto.setAccountNumber(123L);
        dto.setBalance(new BigDecimal("500"));

        AccountEntity entity = new AccountEntity();
        entity.setAccountNumber(123L);
        entity.setBalance(new BigDecimal("500"));

        accountService.updateBalance(dto);

        verify(accountRepository, times(1)).updateBalance(entity);
    }*/
}