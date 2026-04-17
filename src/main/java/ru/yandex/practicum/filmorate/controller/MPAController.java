package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.dto.mpa.MPAResponse;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(MPAController.URL_BASE)
@RequiredArgsConstructor
public class MPAController {
    public static final String URL_BASE = "/mpa";

    private final MPAService service;
    private final MPAMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MPAResponse> getAll() {
        List<MPA> result = service.getAll();
        return result.stream()
                .map(mapper::toMPAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MPAResponse get(@PathVariable long id) {
        MPA result = service.get(id);
        return mapper.toMPAResponse(result);
    }
}
