package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ModelValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(UserController.URL)
@Slf4j
public class UserController extends ModelController<User> {
    public static final String URL = "/users";

    private final ModelValidator<User> validator = new UserValidator();

    @PostMapping
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        try {
            log.info("Получен POST-запрос на добавление модели {}", user);
            validator.validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            saveModel(user);
            URI location = URI.create(String.format("%s/%d", URL, user.getId()));
            return ResponseEntity.created(location)
                    .body(user);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(user);
        }
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление модели {}", user);
        Long id = user.getId();
        if (id == null) {
            log.debug("Id не инициализирован");
            return ResponseEntity.badRequest().body(user);
        }
        if (!store.containsKey(id)) {
            return createNotFound(id);
        }
        return processUser(store.get(id), user);
    }

    private static ResponseEntity<User> createNotFound(Long id) {
        log.debug("Id {} не найден", id);
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Пользователь не найден");
        detail.setDetail("Пользователь с ID " + id + " не найден");
        detail.setProperty("id", id);
        return ResponseEntity.of(detail).build();
    }

    private ResponseEntity<User> processUser(User actual, User userUpdate) {
        try {
            log.debug("Модель {} найдена в хранилище", actual);
            validator.validate(userUpdate);
            updateUserData(actual, userUpdate);
            return ResponseEntity.ok().body(actual);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(actual);
        }
    }

    private void updateUserData(User actual, User userUpdate) {
        log.debug("Обновление данных actual: {}, update: {}", actual, userUpdate);
        actual.setEmail(userUpdate.getEmail());
        actual.setLogin(userUpdate.getLogin());
        actual.setName(userUpdate.getName());
        actual.setBirthday(userUpdate.getBirthday());
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAll() {
        log.info("Получен GET-запрос");
        return ResponseEntity.ok().body(store.values());
    }
}
