package com.example.banksystem;

import com.example.banksystem.dao.Entity.TransactionsEntity;
import com.example.banksystem.dao.TransactionsRepository;
import com.example.banksystem.dto.TransactionsDto;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.TransactionsMapper;
import com.example.banksystem.service.TransactionsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;
    @Mock
    TransactionsMapper transactionsMapper;
    private TransactionsServiceImpl transactionsService;

    @Before
    public void setUp() {
        transactionsService = new TransactionsServiceImpl(transactionsRepository, transactionsMapper);
    }

    @Test
    public void save_ok() {
        TransactionsEntity transactionsEntity = new TransactionsEntity();
        transactionsEntity.setTransactionId(1L);
        transactionsEntity.setTransactionSum(BigDecimal.valueOf(1000));
        transactionsEntity.setTransactionTime(OffsetDateTime.now());
        transactionsEntity.setAccountId(1L);
        transactionsEntity.setPersonLogin("test");

        TransactionsDto savedDto = new TransactionsDto();
        savedDto.setAccountId(1L);
        savedDto.setTransactionId(1L);
        savedDto.setPrice(BigDecimal.valueOf(1000));
        savedDto.setCreateDttm(OffsetDateTime.now());

        savedDto = transactionsService.save(new TransactionsDto());

        assertNull(savedDto);
    }

    @Test
    public void findByUserLogin_ok() {

        doReturn(List.of(new TransactionsEntity())).when(transactionsRepository).findByPersonLogin("test");

        List<TransactionsDto> transactionsDtoList = transactionsService.findByUserLogin("test");

        verify(transactionsRepository, times(1)).findByPersonLogin("test");
        assertTrue(transactionsDtoList.isEmpty());
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void findByUserLogin_listIsEmpty() {

        doReturn(List.of()).when(transactionsRepository).findByPersonLogin("test");

        transactionsService.findByUserLogin("test");
    }
}