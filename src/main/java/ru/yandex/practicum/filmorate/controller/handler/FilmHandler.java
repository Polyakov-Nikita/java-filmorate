package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@Service
public class FilmHandler {
    private final ModelContainer<Film> container = new ModelContainer<>();

    public ResponseEntity<Film> create(Film film) {
        log.trace("Передача запроса на добавление фильма {} в контейнер", film);
        return container.create(film);
    }

    public ResponseEntity<Film> update(Film film) {
        log.trace("Передача запроса на обновление фильма {} в контейнер", film);
        return container.update(film);
    }

    public ResponseEntity<List<Film>> geatAll() {
        log.trace("Передача запроса на получение всех фильмов в контейнер");
        return container.geatAll();
    }
}
