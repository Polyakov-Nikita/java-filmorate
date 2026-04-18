package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User create(User user);

    User updateData(User userUpdate);

    List<User> getAll();

    void checkId(Long id);

    User get(long userId);

    Set<User> getFriends(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Set<User> getCommonFriends(long userId, long otherId);
}
