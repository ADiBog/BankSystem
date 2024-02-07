package com.example.banksystem.mapper;

import com.example.banksystem.dao.Entity.TransactionsEntity;
import com.example.banksystem.dto.TransactionsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {
    @Mapping(target = "transactionSum", source = "dto.price")
    @Mapping(target = "transactionTime", source = "dto.createDttm")
    TransactionsEntity toTransactionsEntity (TransactionsDto dto);
    TransactionsDto toTransactionsDto (TransactionsEntity entity);
    List<TransactionsDto> toListDto (List<TransactionsEntity> list);
}
