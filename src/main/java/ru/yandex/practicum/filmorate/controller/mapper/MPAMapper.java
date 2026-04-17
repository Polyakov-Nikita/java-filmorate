package ru.yandex.practicum.filmorate.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.filmorate.dto.mpa.MPAResponse;
import ru.yandex.practicum.filmorate.model.MPA;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MPAMapper {
    MPAResponse toMPAResponse(MPA mpa);
}
