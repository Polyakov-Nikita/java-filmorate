package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;
    private final GenreMapper mapper;

    public List<GenreResponse> getAll() {
        log.debug("Передача запроса на получение всех жанров в контейнер");
        List<Genre> result = storage.getAll();
        return result.stream()
                .map(mapper::toGenreResponse)
                .toList();
    }

    public GenreResponse get(long id) {
        storage.checkId(id);
        log.debug("Передача запроса на получение жанра с id = {} в контейнер", id);
        Genre result = storage.get(id);
        return mapper.toGenreResponse(result);
    }
}
