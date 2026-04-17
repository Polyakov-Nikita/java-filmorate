package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.QueryParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Film create(Film film) {
        mpaStorage.checkId(film.getMpa().getId());
        checkGenres(film);
        log.debug("Передача запроса на добавление фильма {} в контейнер", film);
        return storage.create(film);
    }

    private void checkGenres(Film film) {
        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.checkId(genre.getId());
            }
        }
    }

    public Film update(Film film) {
        storage.checkId(film.getId());
        log.debug("Передача запроса на обновление фильма {} в контейнер", film);
        return storage.updateData(film);
    }

    public void addLike(long filmId, long likerId) {
        userStorage.checkId(likerId);
        log.debug("Передача запроса на добавление лайка фильму с id={} от пользователя с id={} в контейнер", filmId, likerId);
        storage.addLike(filmId, likerId);
    }

    public List<Film> getAll() {
        log.debug("Передача запроса на получение всех фильмов в контейнер");
        return storage.getAll();
    }

    public Film get(long filmId) {
        storage.checkId(filmId);
        log.debug("Передача запроса на получение фильма с id = {} в контейнер", filmId);
        return storage.get(filmId);
    }

    public List<Film> getPopular(int count) {
        checkCount(count);
        log.debug("Передача запроса на получение {} самых популярных фильмов в контейнер", count);
        return storage.getPopular(count);
    }

    private void checkCount(int count) {
        if (count <= 0) {
            throw new QueryParameterNotValidException("count", String.valueOf(count),
                    "Должен быть положительным значением.");
        }
    }

    public void deleteLike(long filmId, long likerId) {
        userStorage.checkId(likerId);
        log.debug("Передача запроса на удаление лайка с фильма с id={} от пользователя с id={} в контейнер", filmId, likerId);
        storage.deleteLike(filmId, likerId);
    }
}
