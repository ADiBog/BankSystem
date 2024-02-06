package com.example.banksystem.dao.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Таблица transactions.
 */
@Data
@Entity
@Table(name = "transactions")
public class TransactionsEntity {

    /**
     * Id транзакции
     */
    @Id
    @Column(name = "transaction_id", nullable = false)
    @GeneratedValue
    private Long transactionId;

    /**
     * Id аккаунта
     */
    private Long accountId;

    /**
     * Сумма транзакции
     */
    private BigDecimal transactionSum;

    /**
     * Время создания транзакции
     */
    private OffsetDateTime transactionTime;

    /**
     * Логин аккаунта
     */
    private String personLogin;
}
