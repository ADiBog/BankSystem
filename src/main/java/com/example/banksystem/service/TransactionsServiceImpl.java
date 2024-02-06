package com.example.banksystem.service;

import com.example.banksystem.dao.Entity.TransactionsEntity;
import com.example.banksystem.dao.TransactionsRepository;
import com.example.banksystem.exception.BankSystemNotFoundException;
import com.example.banksystem.service.api.TransactionsService;
import com.example.banksystem.service.dto.TransactionsDto;
import com.example.banksystem.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Ревлизация сервиса по работе с транзакциями.
 */
@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;

    @Override
    public TransactionsDto save(TransactionsDto dto) {
        checkAndSetCreateDttm(dto);

        TransactionsEntity transactionsEntity = ModelMapperUtils.map(dto, TransactionsEntity.class);
        transactionsRepository.save(ModelMapperUtils.map(dto, TransactionsEntity.class));
        return ModelMapperUtils.map(transactionsEntity, TransactionsDto.class);
    }

    @Override
    public List<TransactionsDto> findByUserLogin(String login) {
        List<TransactionsEntity> transactionsList = transactionsRepository.findByPersonLogin(login);
        checkForTransactions(transactionsList, login);

        return ModelMapperUtils.mapAll(transactionsList, TransactionsDto.class);
    }

    private void checkAndSetCreateDttm(TransactionsDto dto) {
        OffsetDateTime createDttm = dto.getCreateDttm();
        if (Objects.isNull(createDttm)) {
            dto.setCreateDttm(OffsetDateTime.now());
        }
    }

    private void checkForTransactions(List<TransactionsEntity> transactionsList, String login) {
        if (CollectionUtils.isEmpty(transactionsList)) {
            throw new BankSystemNotFoundException(String.format("Транзакции у пользователя %s не найденый!", login));
        }
    }
}