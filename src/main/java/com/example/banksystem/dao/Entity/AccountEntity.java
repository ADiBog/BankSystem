package com.example.banksystem.dao.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Таблица account.
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
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
    private Long accountNumber;

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

}