package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.ModelValidator;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(FilmController.URL)
@Slf4j
public class FilmController extends ModelController<Film> {
    public static final String URL = "/films";

    private final ModelValidator<Film> validator = new FilmValidator();

    @PostMapping
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        try {
            log.info("Получен POST-запрос на добавление модели {}", film);
            validator.validate(film);
            saveModel(film);
            URI location = URI.create(String.format("%s/%d", URL, film.getId()));
            return ResponseEntity.created(location)
                    .body(film);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(film);
        }
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление модели {}", film);
        Long id = film.getId();
        if (id == null) {
            log.debug("Id не инициализирован");
            return ResponseEntity.badRequest().body(film);
        }
        if (!store.containsKey(id)) {
            return createNotFound(id);
        }
        return processFilm(store.get(id), film);
    }

    private static ResponseEntity<Film> createNotFound(Long id) {
        log.debug("Id {} не найден", id);
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Пользователь не найден");
        detail.setDetail("Пользователь с ID " + id + " не найден");
        detail.setProperty("id", id);
        return ResponseEntity.of(detail).build();
    }

    private ResponseEntity<Film> processFilm(Film actual, Film filmUpdate) {
        try {
            log.debug("Модель {} найдена в хранилище", actual);
            validator.validate(filmUpdate);
            updateFilmData(actual, filmUpdate);
            return ResponseEntity.ok().body(actual);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(actual);
        }
    }

    private void updateFilmData(Film actual, Film filmUpdate) {
        log.debug("Обновление данных actual: {}, update: {}", actual, filmUpdate);
        actual.setName(filmUpdate.getName());
        actual.setDescription(filmUpdate.getDescription());
        actual.setReleaseDate(filmUpdate.getReleaseDate());
        actual.setDuration(filmUpdate.getDuration());
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAll() {
        log.info("Получен GET-запрос");
        return ResponseEntity.ok().body(store.values());
    }
}
