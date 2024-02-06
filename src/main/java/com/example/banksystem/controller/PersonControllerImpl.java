package com.example.banksystem.controller;

import com.example.banksystem.controller.api.PersonController;
import com.example.banksystem.controller.request.PersonRequest;
import com.example.banksystem.service.api.PersonService;
import com.example.banksystem.service.dto.PersonDto;
import com.example.banksystem.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Работа с пользователями
 */
@RestController
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonControllerImpl implements PersonController {

    private final PersonService personService;

    @Override
    @PostMapping("create")
    public PersonDto createUser(@RequestBody PersonRequest request) {
        return personService.save(ModelMapperUtils.map(request, PersonDto.class));
    }

    @Override
    @GetMapping("{login}")
    public PersonDto getByLogin(@PathVariable String login) {
        return personService.findByLogin(login);
    }
}
