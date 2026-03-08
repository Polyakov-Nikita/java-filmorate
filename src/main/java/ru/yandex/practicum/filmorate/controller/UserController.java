package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.handler.UserHandler;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UserController.URL)
@RequiredArgsConstructor
public class UserController  {
    public static final String URL = "/users";

    private final UserHandler handler;

    @PostMapping
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя {}", user);
        return handler.create(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя {}", user);
        return handler.update(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("Получен GET-запрос на получение всех пользователей");
        return handler.geatAll();
    }
}
