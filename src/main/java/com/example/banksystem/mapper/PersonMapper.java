package com.example.banksystem.mapper;

import com.example.banksystem.controller.request.PersonRequest;
import com.example.banksystem.dao.Entity.PersonEntity;
import com.example.banksystem.dto.PersonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonDto toPersonDto (PersonRequest request);
    PersonDto toPersonDto (PersonEntity person);
    PersonEntity toPersonEntity (PersonDto dto);
}
