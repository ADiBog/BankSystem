package com.example.banksystem.service;

import com.example.banksystem.api.AccountService;
import com.example.banksystem.api.PersonService;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.dao.PersonRepository;
import com.example.banksystem.dto.AccountDto;
import com.example.banksystem.dto.PersonDto;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.mapper.AccountMapper;
import com.example.banksystem.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса по работе с пользователеями.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final AccountService accountService;
    private final PersonMapper personMapper;
    private final AccountMapper accountMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, @Lazy AccountService accountService, PersonMapper personMapper, AccountMapper accountMapper) {
        this.personRepository = personRepository;
        this.accountService = accountService;
        this.personMapper = personMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public PersonDto save(PersonDto personDto) {
        PersonEntity personEntity = personMapper.toPersonEntity(personDto);
        personRepository.save(personEntity);
        return personMapper.toPersonDto(personEntity);
    }

    @Override
    public PersonDto findByLogin(String personLogin) {
        return personRepository.findByLogin(personLogin)
                .map(personMapper::toPersonDto)
                .orElseThrow(() -> new BankSystemNotFoundException(
                        String.format("Пользователь с идентификатором %s не найден!", personLogin)));
    }

    @Override
    public PersonDto findById(Long personId) {
        return personRepository.findById(personId)
                .map(personMapper::toPersonDto)
                .orElseThrow(() -> new BankSystemNotFoundException(
                        String.format("Пользователь с идентификатором %s не найден!", personId)));
    }

    @Override
    public PersonDto addAccToList(Long personId, Long accountNumber) {
        PersonEntity personEntity = personRepository.findById(personId).get();
        AccountDto byAccountNumber = accountService.findByAccountNumber(accountNumber);
        AccountEntity map = accountMapper.toAccountEntity(byAccountNumber);
        personEntity.addAccToList(map);
        PersonEntity save = personRepository.save(personEntity);
        return personMapper.toPersonDto(save);
    }
}
