package ru.yandex.practicum.filmorate.dto.film;

import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.dto.mpa.MPAResponse;

import java.time.LocalDate;
import java.util.Set;

public record FilmResponse(Long id,
                           String name,
                           String description,
                           LocalDate releaseDate,
                           int duration,
                           Set<Long> likes,
                           Set<GenreResponse> genres,
                           MPAResponse mpa) {
}
