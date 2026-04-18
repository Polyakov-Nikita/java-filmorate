package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(GenreController.URL_BASE)
@RequiredArgsConstructor
public class GenreController {
    public static final String URL_BASE = "/genres";

    private final GenreService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreResponse get(@PathVariable long id) {
        return service.get(id);
    }
}
