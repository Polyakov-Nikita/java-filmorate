package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModelContainer<M extends Model<M>> {
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

    public M update(M modelUpdate) {
        long modelId = modelUpdate.getId();
        checkId(modelId);
        container.get(modelId).update(modelUpdate);
        log.info("Данные модели {} обновлены", modelUpdate);
        return modelUpdate;
    }

    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (!container.containsKey(id)) {
            throw new NotFoundException(id);
        }
    }

    public List<M> getAll() {
        log.info("Возврат списка моделей");
        return new ArrayList<>(container.values());
    }

    public M get(long id) {
        checkId(id);
        log.info("Возврат модели по id={}", id);
        return container.get(id);
    }
}
