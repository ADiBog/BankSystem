package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.WithdrawalRequest;
import com.example.banksystem.dto.MoneyTransferDto;
import com.example.banksystem.dto.WithdrawalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawalMapper {
    WithdrawalDto toWithdrawalDto (WithdrawalRequest request);
    @Mapping(target = "accountNumber", source = "dto.outgoingAccountNumber")
    WithdrawalDto toWithdrawalDto(MoneyTransferDto dto);
}
