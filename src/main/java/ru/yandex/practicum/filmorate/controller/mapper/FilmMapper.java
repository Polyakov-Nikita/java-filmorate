package ru.yandex.practicum.filmorate.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.filmorate.dto.film.FilmAddRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FilmMapper {
    Film toFilm(FilmAddRequest filmAddRequest);

    Film toFilm(FilmUpdateRequest filmUpdateRequest);

    FilmResponse toFilmResponse(Film film);
}
