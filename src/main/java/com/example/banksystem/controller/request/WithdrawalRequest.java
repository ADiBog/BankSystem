package com.example.banksystem.controller.request;

import com.example.banksystem.service.dto.PersonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * Модель запроса на снятие денежных средств.
 */
@Data
@NoArgsConstructor
@Schema(description = "Модель запроса на снятие денежных средств.")
public class WithdrawalRequest {
    /**
     * Данные о владельце счета.
     */
    @Valid
    @NonNull
    @Schema(description = "Данные о владельце счета.")
    private PersonDto personDto;

    /**
     * Пин код к счету.
     */
    @NonNull
    @Schema(description = "Пин код к счету.")
    private String pinCode;

    /**
     * Номер счета.
     */
    @NonNull
    @Schema(description = "Номер счета.")
    private Long accountNo;

    /**
     * Сумма снятия со счета.
     */
    @NonNull
    @Min(value = 0)
    @Schema(description = "Сумма снятия со счета.")
    private BigDecimal price;
}
