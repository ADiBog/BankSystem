package com.example.banksystem.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Пополнение счета.
 */

@Data
@NoArgsConstructor
@Schema(description = "Пополнение счета")
public class DepositRequest {
    /**
     * Номер счета
     */
    @NonNull
    @Schema(description = "Номер счета.")
    private Long accountNumber;

    /**
     * Сумма пополнения
     */
    @NonNull
    @Min(value = 0)
    @Schema(description = "Сумма пополнения.")
    private BigDecimal replenishmentAmount;
}
