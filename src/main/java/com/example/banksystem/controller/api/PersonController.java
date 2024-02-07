package com.example.banksystem.controller.api;

import com.example.banksystem.controller.request.PersonRequest;
import com.example.banksystem.dto.PersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * API для работы с пользователями.
 */
@Tag(name = "Контроллер для работы с пользователями")
public interface PersonController {

    @Operation(summary = "Добавить пользователя")
    PersonDto createUser(PersonRequest request);

    @Operation(summary = "Найти пользователя")
    PersonDto getByLogin(String login);
}
