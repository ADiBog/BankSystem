package com.example.banksystem.service;

import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.dao.PersonRepository;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.dto.PersonDto;
import com.example.banksystem.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса по работе с пользователеями.
 */
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

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
}
