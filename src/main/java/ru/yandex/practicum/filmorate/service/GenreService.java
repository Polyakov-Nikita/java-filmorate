package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    public List<Genre> getAll() {
        log.debug("Передача запроса на получение всех жанров в контейнер");
        return storage.getAll();
    }

    public Genre get(long id) {
        storage.checkId(id);
        log.debug("Передача запроса на получение жанра с id = {} в контейнер", id);
        return storage.get(id);
    }
}
