package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPAStorage storage;

    public List<MPA> getAll() {
        log.debug("Передача запроса на получение всех рейтингов в контейнер");
        return storage.getAll();
    }

    public MPA get(long id) {
        storage.checkId(id);
        log.debug("Передача запроса на получение рейтинга с id = {} в контейнер", id);
        return storage.get(id);
    }
}
