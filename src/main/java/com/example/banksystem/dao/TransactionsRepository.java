package com.example.banksystem.dao;

import com.example.banksystem.dao.Entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий по работе с таблицей transactions.
 */

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Long> {

    /**
     * Получить все транзакции по логину пользователя.
     *
     * @return
     */
    List<TransactionsEntity> findByPersonLogin(String loginPerson);

    /**
     * @param transactionsEntity
     */
    //void save(TransactionsEntity transactionsEntity);
}
