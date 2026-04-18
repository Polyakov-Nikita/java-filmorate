package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.mpa.MPAResponse;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(MPAController.URL_BASE)
@RequiredArgsConstructor
public class MPAController {
    public static final String URL_BASE = "/mpa";

    private final MPAService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MPAResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MPAResponse get(@PathVariable long id) {
        return service.get(id);
    }
}
