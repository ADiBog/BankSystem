package com.example.banksystem.dao.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Все счета пользователей.
 */
@Data
@Entity
public class DisplayAllAccountsEntity {

    /**
     * Логин аккаунта
     */
    @Id
    @Column(name = "login", nullable = false)
    private String login;

    /**
     * Номер счета
     */
    private String accountNumber;

    /**
     * Баланс аккаунта
     */
    private BigDecimal balance;
}