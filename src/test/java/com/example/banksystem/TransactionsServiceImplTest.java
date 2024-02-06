package com.example.banksystem;

import com.example.banksystem.dao.Entity.TransactionsEntity;
import com.example.banksystem.dao.TransactionsRepository;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.service.TransactionsServiceImpl;
import com.example.banksystem.service.dto.TransactionsDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    private TransactionsServiceImpl transactionsService;

    @Before
    public void setUp() {
        transactionsService = new TransactionsServiceImpl(transactionsRepository);
    }

    @Test
    public void save_ok() {

        TransactionsDto savedDto = transactionsService.save(new TransactionsDto());

        verify(transactionsRepository, times(1)).save(any(TransactionsEntity.class));
        assertNotNull(savedDto);
        assertNotNull(savedDto.getCreateDttm());
    }

    @Test
    public void findByUserLogin_ok() {

        doReturn(List.of(new TransactionsEntity())).when(transactionsRepository).findByPersonLogin("test");

        List<TransactionsDto> transactionsDtoList = transactionsService.findByUserLogin("test");

        verify(transactionsRepository, times(1)).findByPersonLogin("test");
        assertFalse(transactionsDtoList.isEmpty());
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void findByUserLogin_listIsEmpty() {

        doReturn(List.of()).when(transactionsRepository).findByPersonLogin("test");

        transactionsService.findByUserLogin("test");
    }
}