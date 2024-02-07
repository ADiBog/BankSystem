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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        // Arrange
        PersonDto personDto = new PersonDto();
        personDto.setLogin("test");

        PersonEntity personEntity = new PersonEntity();
        personEntity.setLogin("test");
        when(personMapper.toPersonEntity(personDto)).thenReturn(personEntity);

        PersonDto savedPersonDto = new PersonDto();
        savedPersonDto.setLogin("test");
        when(personMapper.toPersonDto(personEntity)).thenReturn(savedPersonDto);

        // Act
        PersonDto save = personService.save(personDto);

        // Assert
        verify(personRepository, times(1)).save(personEntity);
        assertNotNull(save);
        assertEquals(savedPersonDto, save);
    }

    @Test
    public void findByLogin_ok() {
        // Arrange
        PersonEntity entity = new PersonEntity();
        entity.setPersonId(1L);
        entity.setLogin("test");

        when(personRepository.findByLogin("test")).thenReturn(Optional.of(entity));

        // Act
        PersonDto savedDto = personService.findByLogin("test");

        // Assert
        verify(personRepository, times(1)).findByLogin("test");
        assertNotNull(savedDto);
        assertEquals("test", savedDto.getLogin());
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