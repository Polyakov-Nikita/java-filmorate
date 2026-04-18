package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserAddRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;
    private final UserMapper mapper;

    public UserResponse create(UserAddRequest request) {
        User user = mapper.toUser(request);
        fixName(user);
        log.debug("Передача запроса на добавление пользователя {} в контейнер", user);
        User result = storage.create(user);
        return mapper.toUserResponse(result);
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

    public UserResponse update(UserUpdateRequest request) {
        User user = mapper.toUser(request);
        storage.checkId(user.getId());
        fixName(user);
        log.debug("Передача запроса на обновление пользователя {} в контейнер", user);
        User result = storage.updateData(user);
        return mapper.toUserResponse(result);
    }

    public void addFriend(long userId, long friendId) {
        storage.checkId(userId);
        storage.checkId(friendId);
        log.debug("Передача запроса на добавление в друзья пользователя с id={} пользователя с id={} в контейнер", userId, friendId);
        storage.addFriend(userId, friendId);
    }

    public List<UserResponse> getAll() {
        log.debug("Передача запроса на получение всех пользователей в контейнер");
        List<User> result = storage.getAll();
        return result.stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    public UserResponse get(long userId) {
        storage.checkId(userId);
        log.debug("Передача запроса на получение пользователя с id = {} в контейнер", userId);
        User result = storage.get(userId);
        return mapper.toUserResponse(result);
    }

    public Set<UserResponse> getFriends(long userId) {
        log.debug("Получение всех друзей пользователя с id={}", userId);
        storage.checkId(userId);
        Set<User> friends = storage.getFriends(userId);
        return friends.stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toSet());
    }

    public Set<UserResponse> getCommonFriends(long userId, long otherId) {
        log.debug("Передача запроса на получение общих друзей пользователей с id={} и id={} в контейнер", userId, otherId);
        Set<User> result = storage.getCommonFriends(userId, otherId);
        return result.stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toSet());
    }

    public void deleteFriend(long userId, long friendId) {
        log.debug("Передача запроса на удаление из друзей пользователя с id={} пользователя с id={} в контейнер", userId, friendId);
        storage.checkId(userId);
        storage.checkId(friendId);
        storage.deleteFriend(userId, friendId);
    }
}
