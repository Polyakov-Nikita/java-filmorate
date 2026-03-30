package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.validation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Model<Film> {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @MinDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
    private Set<String> genres;
    private String ratingMPA;

    @Override
    public void update(Film update) {
        name = update.name;
        description = update.description;
        releaseDate = update.releaseDate;
        duration = update.duration;
    }

    public void addLike(Long likerId) {
        likes.add(likerId);
    }

    public void deleteLike(Long likerId) {
        likes.remove(likerId);
    }
}
