package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.DepositRequest;
import com.example.banksystem.dto.DepositDto;
import com.example.banksystem.dto.MoneyTransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    DepositDto toDepositDto(DepositRequest request);
    @Mapping(target = "accountNumber", source = "dto.outgoingAccountNumber")
    @Mapping(target = "replenishmentAmount", source = "dto.price")
    DepositDto toDepositDto(MoneyTransferDto dto);
}
