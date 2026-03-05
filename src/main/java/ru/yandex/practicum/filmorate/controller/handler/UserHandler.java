package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Service
public class UserHandler {
    private final ModelContainer<User> container = new ModelContainer<>();

    public ResponseEntity<User> create(User user) {
        fixName(user);
        log.trace("Передача запроса на добавление фильма {} в контейнер", user);
        return container.create(user);
    }

    private void fixName(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            String userLogin = user.getLogin();
            log.info("Значение поля name у пользователя {} отсутствует", user);
            user.setName(userLogin);
            log.debug("Установлено значение поля name, равное значению login \"{}\"", userLogin);
        }
    }

    public ResponseEntity<User> update(User user) {
        fixName(user);
        log.trace("Передача запроса на обновление фильма {} в контейнер", user);
        return container.update(user);
    }

    public ResponseEntity<List<User>> geatAll() {
        log.trace("Передача запроса на получение всех фильмов в контейнер");
        return container.geatAll();
    }
}
