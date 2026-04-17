package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(GenreController.URL_BASE)
@RequiredArgsConstructor
public class GenreController {
    public static final String URL_BASE = "/genres";

    private final GenreService service;
    private final GenreMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreResponse> getAll() {
        List<Genre> result = service.getAll();
        return result.stream()
                .map(mapper::toGenreResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreResponse get(@PathVariable long id) {
        Genre result = service.get(id);
        return mapper.toGenreResponse(result);
    }
}
