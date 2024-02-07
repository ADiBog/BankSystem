package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.MoneyTransferRequest;
import com.example.banksystem.service.dto.MoneyTransferDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoneyTransferMapper {
    MoneyTransferDto toMoneyTransferDto (MoneyTransferRequest request);
}
