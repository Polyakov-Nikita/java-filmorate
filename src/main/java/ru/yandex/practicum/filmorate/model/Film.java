package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validation.MinDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film implements Model {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @MinDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
