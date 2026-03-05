package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModelContainer<M extends Model> {
    private final Map<Long, M> container = new HashMap<>();

    private long currentId = 0;

    public ResponseEntity<M> create(M model) {
        currentId++;
        model.setId(currentId);
        log.debug("Модели {} установлен id {}", model, currentId);
        container.put(currentId, model);
        log.info("Модель {} сохранена", model);
        return ResponseEntity.ok()
                .body(model);
    }

    public ResponseEntity<M> update(M model) {
        try {
            return createUpdate(model);
        } catch (NullIdException e) {
            return createNullId(model);
        } catch (NotFoundException e) {
            return createNotFound(e.id);
        }
    }

    private ResponseEntity<M> createUpdate(M model) {
        long modelId = model.getId();
        checkId(modelId);
        container.put(modelId, model);
        log.info("Данные модели {} обновлены", model);
        return ResponseEntity.ok()
                .body(model);
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (!container.containsKey(id)) {
            throw new NotFoundException(id);
        }
    }

    private ResponseEntity<M> createNullId(M model) {
        log.info("Id модели не установлен");
        return ResponseEntity.badRequest()
                .body(model);
    }

    private ResponseEntity<M> createNotFound(Long id) {
        log.info("Id {} не найден", id);
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Модель не найдена");
        detail.setDetail(String.format("Модель с id %d не найден", id));
        detail.setProperty("id", id);
        return ResponseEntity.of(detail).build();
    }

    public ResponseEntity<List<M>> geatAll() {
        log.info("Возврат списка моделей");
        return ResponseEntity.ok()
                .body(new ArrayList<>(container.values()));
    }
}
