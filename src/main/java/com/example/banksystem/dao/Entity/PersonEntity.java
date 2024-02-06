package com.example.banksystem.dao.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Талица person.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "accountEntityList")
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
    private String login;

    /**
     * Список аккаунтов
     */
    @OneToMany(mappedBy = "person")
    private List<AccountEntity> accountEntityList = new ArrayList<>();

    public void addAccToList (AccountEntity accountEntity) {
        accountEntityList.add(accountEntity);
    }
}
