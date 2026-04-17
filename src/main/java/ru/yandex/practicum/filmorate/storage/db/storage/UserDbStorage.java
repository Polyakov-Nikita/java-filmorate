package ru.yandex.practicum.filmorate.storage.db.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.requestbuilder.UserRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@Primary
public class UserDbStorage extends DbStorage implements UserStorage {
    private final UserRequestBuilder requestBuilder;
    private final RowMapper<User> userRowMapper;
    private final RowMapper<Friendship> friendshipRowMapper;

    public UserDbStorage(JdbcTemplate jdbc, UserRequestBuilder requestBuilder,
                         RowMapper<User> userRowMapper,
                         RowMapper<Friendship> friendshipRowMapper) {
        super(jdbc);
        this.requestBuilder = requestBuilder;
        this.userRowMapper = userRowMapper;
        this.friendshipRowMapper = friendshipRowMapper;
    }

    @Override
    public User create(User user) {
        long userId = insertWithId(requestBuilder.insertUser(user));
        log.info("Пользователь {} сохранён", user);
        user.setId(userId);
        return user;
    }

    @Override
    public User updateData(User userUpdate) {
        update(requestBuilder.updateUser(userUpdate));
        log.info("Данные пользователя {} обновлены", userUpdate);
        return userUpdate;
    }

    @Override
    public List<User> getAll() {
        log.info("Возврат списка пользователей");
        return selectMany(requestBuilder.getAll(), userRowMapper);
    }

    @Override
    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (selectOne(requestBuilder.getUser(id), userRowMapper) == null) {
            throw new NotFoundException(id);
        }
    }

    @Override
    public User get(long userId) {
        log.info("Возврат пользователя по id={}", userId);
        User user = selectOne(requestBuilder.getUser(userId), userRowMapper);
        compose(user);
        return user;
    }

    private void compose(User user) {
        List<Friendship> friendshipList = selectMany(requestBuilder.getFriends(user.getId()), friendshipRowMapper);
        for (Friendship friendship : friendshipList) {
            user.addFriend(friendship.friendId());
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.debug("Добавление в друзья пользователя с id={} пользователя с id={}", userId, friendId);
        Friendship friendship = new Friendship(userId, friendId);
        insert(requestBuilder.insertFriendship(friendship));
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        log.debug("Удаление из друзей пользователя с id={} пользователя с id={}", userId, friendId);
        update(requestBuilder.deleteFriend(userId, friendId));
    }

    @Override
    public Set<User> getCommonFriends(long userId, long otherId) {
        log.debug("Получение общих друзей пользователей с id={} и id={}", userId, otherId);
        List<Friendship> commonFriendIds = selectCommonFriendIds(userId, otherId);
        Set<User> result = new HashSet<>();
        for (Friendship friendship : commonFriendIds) {
            result.add(get(friendship.friendId()));
        }
        result.forEach(this::compose);
        return result;
    }

    private List<Friendship> selectCommonFriendIds(long userId, long otherId) {
        return selectMany(requestBuilder.getCommonFriends(userId, otherId), friendshipRowMapper);
    }
}
