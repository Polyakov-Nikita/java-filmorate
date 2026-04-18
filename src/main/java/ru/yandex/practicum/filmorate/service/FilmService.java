package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmAddRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.exception.QueryParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.mapper.FilmMapper;
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
    private final FilmMapper mapper;
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmResponse create(FilmAddRequest request) {
        Film film = mapper.toFilm(request);
        mpaStorage.checkId(film.getMpa().getId());
        checkGenres(film);
        log.debug("Передача запроса на добавление фильма {} в контейнер", request);
        Film result = storage.create(film);
        return mapper.toFilmResponse(result);
    }

    private void checkGenres(Film film) {
        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.checkId(genre.getId());
            }
        }
    }

    public FilmResponse update(FilmUpdateRequest request) {
        Film film = mapper.toFilm(request);
        storage.checkId(film.getId());
        log.debug("Передача запроса на обновление фильма {} в контейнер", film);
        Film result = storage.updateData(film);
        return mapper.toFilmResponse(result);
    }

    public void addLike(long filmId, long likerId) {
        userStorage.checkId(likerId);
        log.debug("Передача запроса на добавление лайка фильму с id={} от пользователя с id={} в контейнер", filmId, likerId);
        storage.addLike(filmId, likerId);
    }

    public List<FilmResponse> getAll() {
        log.debug("Передача запроса на получение всех фильмов в контейнер");
        List<Film> result = storage.getAll();
        return result.stream()
                .map(mapper::toFilmResponse)
                .toList();
    }

    public FilmResponse get(long filmId) {
        storage.checkId(filmId);
        log.debug("Передача запроса на получение фильма с id = {} в контейнер", filmId);
        Film result = storage.get(filmId);
        return mapper.toFilmResponse(result);
    }

    public List<FilmResponse> getPopular(int count) {
        checkCount(count);
        log.debug("Передача запроса на получение {} самых популярных фильмов в контейнер", count);
        List<Film> result = storage.getPopular(count);
        return result.stream()
                .map(mapper::toFilmResponse)
                .toList();
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
