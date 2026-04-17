package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.user.UserInMemory;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.memory.mapper.InMemoryUserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class InMemoryUserStorage extends ModelContainer<UserInMemory> implements UserStorage {
    private final InMemoryUserMapper mapper;

    @Override
    public User create(User user) {
        UserInMemory userInMemory = mapper.toUserInMemory(user);
        UserInMemory result = create(userInMemory);
        return mapper.toUser(result);
    }

    @Override
    public User updateData(User userUpdate) {
        long userUpdateId = userUpdate.getId();
        UserInMemory userInMemory = getFromMemory(userUpdateId);
        userInMemory.setEmail(userUpdate.getEmail());
        userInMemory.setLogin(userUpdate.getLogin());
        userInMemory.setName(userUpdate.getName());
        userInMemory.setBirthday(userUpdate.getBirthday());
        log.info("Данные пользователя {} обновлены", userInMemory);
        return mapper.toUser(userInMemory);
    }

    @Override
    public List<User> getAll() {
        List<UserInMemory> result = getAllFromMemory();
        return result.stream()
                .map(mapper::toUser)
                .toList();
    }

    @Override
    public User get(long userId) {
        UserInMemory result = getFromMemory(userId);
        return mapper.toUser(result);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.debug("Добавление в друзья пользователя с id={} пользователя с id={}", userId, friendId);
        getFromMemory(userId).addFriend(friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        log.debug("Удаление из друзей пользователя с id={} пользователя с id={}", userId, friendId);
        getFromMemory(userId).deleteFriend(friendId);
    }

    @Override
    public Set<User> getCommonFriends(long userId, long otherId) {
        log.debug("Получение общих друзей пользователей с id={} и id={}", userId, otherId);
        Set<Long> commonIds = getCommonIds(userId, otherId);
        return commonIds.stream()
                .map(this::getFromMemory)
                .map(mapper::toUser)
                .collect(Collectors.toSet());
    }

    private Set<Long> getCommonIds(long userId, long otherId) {
        Set<Long> userFriends = getFromMemory(userId).getFriends();
        Set<Long> otherFriends = getFromMemory(otherId).getFriends();
        Set<Long> common = new HashSet<>(userFriends);
        common.retainAll(otherFriends);
        return common;
    }
}
