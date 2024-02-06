package com.example.banksystem.dao.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Таблица account.
 */
@Data
@Entity
@Table(name = "account")
public class AccountEntity {

    /**
     * Id аккаунта
     */
    @Id
    @Column(name = "account_id", nullable = false)
    @GeneratedValue
    private Long accountId;

    /**
     * Номер счета
     */
    private Long accountNo;

    /**
     * Id аккаунта
     */
    //private Long personId;

    /**
     * Пин код счета в виде хэша
     */
    private String pinCode;

    /**
     * Баланс аккаунта
     */
    private BigDecimal balance;

    /**
     * Владелец аккаунта
     */
    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    /**
     * Обновление баланса
     */
    private BigDecimal updateBalance;
}