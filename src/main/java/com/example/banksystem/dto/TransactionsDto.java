package com.example.banksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Сервсиная модель для представления информации о транзакции.
 */
@Data
@Schema(description = "Сервсиная модель для представления информации о транзакции.")
public class TransactionsDto {

    /**
     * Идентификатор транзакции.
     */
    @Schema(description = "Идентификатор транзакции.")
    private Long transactionId;

    /**
     * Идентификатор счета по которому произошла транзакция.
     */
    @Schema(description = "Идентификатор счета по которому произошла транзакция.")
    private Long accountId;

    /**
     * Сумма транзакции.
     */
    @Schema(description = "Сумма транзакции.")
    private BigDecimal price;

    /**
     * Время создания транзакции.
     */
    @Schema(description = "Время создания транзакции.")
    private OffsetDateTime createDttm;
}
