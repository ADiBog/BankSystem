package com.example.banksystem.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Сервисная модель для представления данных о пользователе.")
public class PersonDto {

    /**
     * Идентификатор пользователя.
     */
    @Schema(description = "Идентификатор пользователя.")
    private Long personId;

    /**
     * Логин пользователя.
     */
    @NotBlank
    @Schema(description = "Логин пользователя.")
    private String login;
}
