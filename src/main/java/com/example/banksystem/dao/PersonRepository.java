package com.example.banksystem.dao;

import com.example.banksystem.dao.Entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репоситорий по работе с БД таблицей person.
 */

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    /**
     * Получить пользователя по его логину.
     *
     * @param personLogin логин пользователя.
     * @return опциональная сущность с информацие о пользователе.
     */
    Optional<PersonEntity> findByLogin(String personLogin);

    /**
     * Сохранить данные о пользователе.
     *
     * @param entity данные о пользователе.
     */
    //void save(PersonEntity entity);
}
