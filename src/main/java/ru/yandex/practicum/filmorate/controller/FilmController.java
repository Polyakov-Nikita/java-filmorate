package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.handler.FilmHandler;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(FilmController.URL)
@RequiredArgsConstructor
public class FilmController {
    public static final String URL = "/films";

    private final FilmHandler handler;

    @PostMapping
    public ResponseEntity<Film> add(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос на добавление фильма {}", film);
        return handler.create(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма {}", film);
        return handler.update(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        log.info("Получен GET-запрос на получение всех фильмов");
        return handler.geatAll();
    }
}
