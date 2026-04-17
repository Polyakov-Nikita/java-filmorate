package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film updateData(Film filmUpdate);

    List<Film> getAll();

    void checkId(Long id);

    Film get(long filmId);

    void addLike(long filmId, long likerId);

    void deleteLike(long filmId, long likerId);

    List<Film> getPopular(int count);
}
