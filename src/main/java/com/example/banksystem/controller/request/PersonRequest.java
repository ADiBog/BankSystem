package com.example.banksystem.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Модель на создание нового пользователя.")
public class PersonRequest {
    /**
     * Логин пользователя.
     */
    @NotBlank
    @Schema(description = "Логин пользователя")
    private String login;
}
