package com.example.banksystem.controller.request;

import com.example.banksystem.service.dto.PersonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Создание нового счета.
 */

@Data
@NoArgsConstructor
@Schema(description = "Создание нового счета")
public class CreateAccountRequest {
    /**
     * Данные владельца счета
     */
    @Valid
    @NonNull
    @Schema(description = "Данные владельца счета")
    private PersonDto personDto;

    /**
     * Пин код счета
     */
    @NotBlank
    @Schema(description = "Пин код счета")
    @Pattern(regexp = "^\\d{4}$\n")
    private String pinCode;
}
