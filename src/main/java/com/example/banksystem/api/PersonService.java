package com.example.banksystem.api;

import com.example.banksystem.dto.PersonDto;

/**
 * Работа с пользователями.
 */
public interface PersonService {

    /**
     * Создать нового пользователя.
     *
     * @param personDto модель содержашие данные о новом пользователе.
     */
    PersonDto save(PersonDto personDto);

    /**
     * Получить пользователя по его логину.
     *
     * @param personLogin логин пользователя.
     * @return информация о пользователе.
     */
    PersonDto findByLogin(String personLogin);

    /**
     * Получить пользователя по его логину.
     *
     * @param personId id пользователя.
     * @return информация о пользователе.
     */
    PersonDto findById(Long personId);

    PersonDto addAccToList(Long personId, Long accountNumber);

}
