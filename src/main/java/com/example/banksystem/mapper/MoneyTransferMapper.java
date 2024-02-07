package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.MoneyTransferRequest;
import com.example.banksystem.dto.MoneyTransferDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoneyTransferMapper {
    MoneyTransferDto toMoneyTransferDto (MoneyTransferRequest request);
}
