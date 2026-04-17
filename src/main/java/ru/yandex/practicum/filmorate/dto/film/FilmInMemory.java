package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.memory.MemoryEntity;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmInMemory implements MemoryEntity<FilmInMemory> {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes;
    private Set<Genre> genres;
    private MPA mpa;
    private Long mpaId;

    public void addLike(Long likerId) {
        likes.add(likerId);
    }

    public void deleteLike(Long likerId) {
        likes.remove(likerId);
    }
}
