package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class ModelController<M extends Storable> {
    protected final Map<Long, M> store = new HashMap<>();

    private long currentId = 1;

    protected void saveModel(M model) {
        log.debug("Сохранение модели \"{}\"", model);
        model.setId(currentId);
        store.put(currentId, model);
        currentId++;
    }
}
