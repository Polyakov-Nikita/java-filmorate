package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModelContainer<M extends MemoryEntity<M>> {
    private final Map<Long, M> container = new HashMap<>();

    private long currentId = 0;

    public M create(M model) {
        currentId++;
        model.setId(currentId);
        log.debug("Модели {} установлен id {}", model, currentId);
        container.put(currentId, model);
        log.info("Модель {} сохранена", model);
        return model;
    }

    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (!container.containsKey(id)) {
            throw new NotFoundException(id);
        }
    }

    public List<M> getAllFromMemory() {
        log.info("Возврат списка моделей");
        return new ArrayList<>(container.values());
    }

    public M getFromMemory(long id) {
        log.info("Возврат модели по id={}", id);
        return container.get(id);
    }
}
