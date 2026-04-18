package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.MPARequest;
import ru.yandex.practicum.filmorate.validation.MinDate;

import java.time.LocalDate;
import java.util.Set;

public record FilmAddRequest(@NotBlank String name,
                             @Size(min = 1, max = 200) String description,
                             @MinDate LocalDate releaseDate,
                             @Min(1) int duration,
                             Set<GenreRequest> genres,
                             @NotNull MPARequest mpa) {
}
