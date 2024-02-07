package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.CreateAccountRequest;
import com.example.banksystem.dao.Entity.AccountEntity;
import com.example.banksystem.dto.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toAccountDto(CreateAccountRequest request);
    AccountDto toAccountDto(AccountEntity entity);
    AccountEntity toAccountEntity(AccountDto dto);
}
