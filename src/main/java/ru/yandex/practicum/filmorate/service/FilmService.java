package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.QueryParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        log.debug("Передача запроса на добавление фильма {} в контейнер", film);
        return storage.create(film);
    }

    public Film update(Film film) {
        log.debug("Передача запроса на обновление фильма {} в контейнер", film);
        return storage.update(film);
    }

    public void addLike(long filmId, long likerId) {
        log.debug("Добавление лайка фильму с id={} от пользователя с id={}", filmId, likerId);
        userStorage.checkId(likerId);
        get(filmId).addLike(likerId);
    }

    public List<Film> getAll() {
        log.debug("Передача запроса на получение всех фильмов в контейнер");
        return storage.getAll();
    }

    public Film get(long filmId) {
        log.debug("Передача запроса на получение фильма с id = {} в контейнер", filmId);
        return storage.get(filmId);
    }

    public List<Film> getPopular(int count) {
        checkCount(count);
        log.debug("Получение {} самых популярных фильмов в контейнер", count);
        return storage.getAll().stream()
                .sorted(Comparator.comparing(Film::getLikes, Comparator.comparingInt(Set::size)).reversed())
                .limit(count)
                .toList();
    }

    private void checkCount(int count) {
        if (count <= 0) {
            throw new QueryParameterNotValidException("count", String.valueOf(count),
                    "Должен быть положительным значением.");
        }
    }

    public void deleteLike(long filmId, long likerId) {
        log.debug("Удаление лайка с фильма с id={} от пользователя с id={}", filmId, likerId);
        userStorage.checkId(likerId);
        get(filmId).deleteLike(likerId);
    }
}
