package com.example.banksystem.dao;

import com.example.banksystem.dao.Entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий по работе с таблицей account.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    /**
     * @param accountId
     * @return
     */
    Optional<AccountEntity> findById(Long accountId);

    /**
     * @param accountNo
     * @return
     */
    Optional<AccountEntity> findByAccountNo(Long accountNo);

    /**
     * Получить все счета пользователей.
     *
     * @return список счетов пользоваетлей.
     */
    //List<ModelForDisplayingAllAccountsEntity> findAll();


    /**
     * @param entity
     */
    //void save(AccountEntity entity);

    /**
     * Обновить баланс по номеру счета.
     */
    void updateBalance(AccountEntity entity);
}
