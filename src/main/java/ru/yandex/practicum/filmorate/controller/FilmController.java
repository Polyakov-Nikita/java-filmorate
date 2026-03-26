package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(FilmController.URL_BASE)
@RequiredArgsConstructor
public class FilmController {
    public static final String URL_BASE = "/films";
    public static final String URL_LIKE = "/like";
    public static final String URL_POPULAR = "/popular";
    public static final String COUNT_PARAMETER = "count";

    private final FilmService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film add(@Valid @RequestBody Film film) {
        return service.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film) {
        return service.update(film);
    }

    @PutMapping("/{filmId}" + URL_LIKE + "/{likerId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable long filmId, @PathVariable long likerId) {
        service.addLike(filmId, likerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAll() {
        return service.getAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable long filmId) {
        return service.get(filmId);
    }

    @GetMapping(URL_POPULAR)
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") int count) {
        return service.getPopular(count);
    }

    @DeleteMapping("/{filmId}" + URL_LIKE + "/{likerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable long filmId, @PathVariable long likerId) {
        service.deleteLike(filmId, likerId);
    }
}
