package com.example.banksystem.dao.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Талица person.
 */
@Data
@Entity
@Table(name = "person")
public class PersonEntity {

    /**
     * Id аккаунта
     */
    @Id
    @Column(name = "person_id", nullable = false)
    @GeneratedValue
    private Long personId;

    /**
     * Логин аккаунта
     */
    private String personLogin;

    /**
     * Список аккаунтов
     */
    @OneToMany(mappedBy = "person")
    private List<AccountEntity> accountEntityList;
}
