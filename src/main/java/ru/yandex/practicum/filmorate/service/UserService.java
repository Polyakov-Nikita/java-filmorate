package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
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
        fixName(user);
        log.debug("Передача запроса на обновление пользователя {} в контейнер", user);
        return storage.update(user);
    }

    public void addFriend(long userId, long friendId) {
        log.debug("Добавление в друзья пользователя с id={} пользователя с id={}", userId, friendId);
        storage.checkId(friendId);
        get(userId).addFriend(friendId);
        get(friendId).addFriend(userId);
    }

    public List<User> getAll() {
        log.debug("Передача запроса на получение всех пользователей в контейнер");
        return storage.getAll();
    }

    public User get(long userId) {
        log.debug("Передача запроса на получение пользователя с id = {} в контейнер", userId);
        return storage.get(userId);
    }

    public Set<User> getFriends(long userId) {
        log.debug("Получение всех друзей пользователя с id={}", userId);
        User user = get(userId);
        return user.getFriends().stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        log.debug("Получение общих друзей пользователей с id={} и id={}", userId, otherId);
        Set<Long> commonIds = getCommonIds(userId, otherId);
        return commonIds.stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    private Set<Long> getCommonIds(long userId, long otherId) {
        Set<Long> userFriends = get(userId).getFriends();
        Set<Long> otherFriends = get(otherId).getFriends();
        Set<Long> common = new HashSet<>(userFriends);
        common.retainAll(otherFriends);
        return common;
    }

    public void deleteFriend(long userId, long friendId) {
        log.debug("Удаление из друзей пользователя с id={} пользователя с id={}", userId, friendId);
        storage.checkId(friendId);
        get(userId).deleteFriend(friendId);
        get(friendId).deleteFriend(userId);
    }
}
