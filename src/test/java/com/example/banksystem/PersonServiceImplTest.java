package com.example.banksystem;

import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.dao.PersonRepository;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.PersonMapper;
import com.example.banksystem.service.PersonServiceImpl;
import com.example.banksystem.service.api.AccountService;
import com.example.banksystem.service.dto.PersonDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.internal.util.Assert;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AccountService accountService;
    @Mock
    PersonMapper personMapper;
    @Mock
    AccountMapper accountMapper;

    private PersonServiceImpl personService;

    @Before
    public void setUp() {
        personService = new PersonServiceImpl(personRepository, accountService, personMapper, accountMapper);
    }

    @Test
    public void save() {
        PersonEntity entity = new PersonEntity();
        entity.setLogin("test");

        PersonDto personDto = new PersonDto();
        personDto.setLogin("test");
        PersonDto save = personService.save(personDto);

        verify(personRepository, times(1)).save(entity);
        Assert.notNull(save);
    }

    @Test
    public void findByLogin_ok() {
        PersonEntity entity = new PersonEntity();
        entity.setPersonId(1L);
        entity.setLogin("test");

        doReturn(Optional.of(entity)).when(personRepository).findByLogin("test");

        PersonDto savedDto = personService.findByLogin("test");

        verify(personRepository, times(1)).findByLogin("test");
        Assert.notNull(savedDto);
    }

    @Test(expected = BankSystemNotFoundException.class)
    public void findByLogin_notFound() {
        PersonEntity entity = new PersonEntity();
        entity.setPersonId(1L);
        entity.setLogin("test");

        doReturn(Optional.empty()).when(personRepository).findByLogin("test");

        personService.findByLogin("test");
    }
}