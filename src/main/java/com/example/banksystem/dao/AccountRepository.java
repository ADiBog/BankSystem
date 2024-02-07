package com.example.banksystem.dao;

import com.example.banksystem.dao.Entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Репозиторий по работе с таблицей account.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    /**
     * @param accountId
     */
    Optional<AccountEntity> findById(Long accountId);

    /**
     * @param accountNumber
     */
    Optional<AccountEntity> findByAccountNumber(Long accountNumber);

    /**
     * Обновить баланс по номеру счета.
     */
    @Modifying
    @Query("update AccountEntity a set a.balance = :balance where a.accountId = :accountId")
    void updateBalance(@Param("accountId") Long accountId, @Param("balance") BigDecimal balance);
}
