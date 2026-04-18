package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
    private MPA mpa;
    private Long mpaId;

    public void addLike(Long likerId) {
        likes.add(likerId);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
