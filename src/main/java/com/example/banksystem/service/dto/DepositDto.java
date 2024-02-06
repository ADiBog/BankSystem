package com.example.banksystem.service.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DepositDto {

    /**
     * Номер счета.
     */
    @NonNull
    private Long accountNo;

    /**
     * Сумма пополнения.
     */
    @NonNull
    @Min(value = 0)
    private BigDecimal price;
}
