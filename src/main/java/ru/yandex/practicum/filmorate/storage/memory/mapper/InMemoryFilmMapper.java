package ru.yandex.practicum.filmorate.storage.memory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.filmorate.dto.film.FilmInMemory;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InMemoryFilmMapper {
    FilmInMemory toFilmInMemory(Film film);

    Film toFilm(FilmInMemory filmInMemory);
}
