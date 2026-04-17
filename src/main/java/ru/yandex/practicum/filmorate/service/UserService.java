package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User create(User user) {
        fixName(user);
        log.debug("Передача запроса на добавление пользователя {} в контейнер", user);
        return storage.create(user);
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

    public User update(User user) {
        storage.checkId(user.getId());
        fixName(user);
        log.debug("Передача запроса на обновление пользователя {} в контейнер", user);
        return storage.updateData(user);
    }

    public void addFriend(long userId, long friendId) {
        storage.checkId(userId);
        storage.checkId(friendId);
        log.debug("Передача запроса на добавление в друзья пользователя с id={} пользователя с id={} в контейнер", userId, friendId);
        storage.addFriend(userId, friendId);
    }

    public List<User> getAll() {
        log.debug("Передача запроса на получение всех пользователей в контейнер");
        return storage.getAll();
    }

    public User get(long userId) {
        storage.checkId(userId);
        log.debug("Передача запроса на получение пользователя с id = {} в контейнер", userId);
        return storage.get(userId);
    }

    public Set<User> getFriends(long userId) {
        log.debug("Получение всех друзей пользователя с id={}", userId);
        storage.checkId(userId);
        User user = get(userId);
        return user.getFriends().stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        log.debug("Передача запроса на получение общих друзей пользователей с id={} и id={} в контейнер", userId, otherId);
        return storage.getCommonFriends(userId, otherId);
    }

    public void deleteFriend(long userId, long friendId) {
        log.debug("Передача запроса на удаление из друзей пользователя с id={} пользователя с id={} в контейнер", userId, friendId);
        storage.checkId(userId);
        storage.checkId(friendId);
        storage.deleteFriend(userId, friendId);
    }
}
