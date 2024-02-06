package com.example.banksystem.service;

import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.dao.PersonRepository;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.service.api.AccountService;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.dto.AccountDto;
import com.example.banksystem.service.dto.PersonDto;
import com.example.banksystem.utils.ModelMapperUtils;
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

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, @Lazy AccountService accountService) {
        this.personRepository = personRepository;
        this.accountService = accountService;
    }

    @Override
    public PersonDto save(PersonDto personDto) {
        PersonEntity personEntity = ModelMapperUtils.map(personDto, PersonEntity.class);
        personRepository.save(personEntity);
        return ModelMapperUtils.map(personEntity, PersonDto.class);
    }

    @Override
    public PersonDto findByLogin(String personLogin) {
        return personRepository.findByLogin(personLogin)
                .map(entity -> ModelMapperUtils.map(entity, PersonDto.class))
                .orElseThrow(() -> new BankSystemNotFoundException(
                        String.format("Пользователь с идентификатором %s не найден!", personLogin)));
    }

    @Override
    public PersonDto findById(Long personId) {
        return personRepository.findById(personId)
                .map(entity -> ModelMapperUtils.map(entity, PersonDto.class))
                .orElseThrow(() -> new BankSystemNotFoundException(
                        String.format("Пользователь с идентификатором %s не найден!", personId)));
    }

    @Override
    public PersonDto addAccToList(Long personId, Long accountNumber) {
        PersonEntity personEntity = personRepository.findById(personId).get();
        AccountDto byAccountNumber = accountService.findByAccountNumber(accountNumber);
        AccountEntity map = ModelMapperUtils.map(byAccountNumber, AccountEntity.class);
        personEntity.addAccToList(map);
        PersonEntity save = personRepository.save(personEntity);
        return ModelMapperUtils.map(save, PersonDto.class);
    }
}
