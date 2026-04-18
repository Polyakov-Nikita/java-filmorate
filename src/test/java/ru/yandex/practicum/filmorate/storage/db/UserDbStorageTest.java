package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage storage;

    @Test
    public void create() {
        String description = "Должен получить id";
        User created = storage.create(createUserData());
        Assertions.assertNotNull(created.getId(), description);
    }

    private User createUserData() {
        return User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();
    }

    @Test
    public void updateData() {
        String description = "Должен обновить данные";
        User created = storage.create(createUserData());
        created.setEmail("newEmail");
        created.setLogin("newLogin");
        created.setName("newName");
        created.setBirthday(LocalDate.now());
        User updated = storage.updateData(created);
        Assertions.assertEquals(created, updated, description);
    }

    @Test
    public void getAll() {
        String description = "Количество пользователей должно увеличиться";
        int previousCount = storage.getAll().size();
        int newUsersCount = 3;
        addUsers(newUsersCount);
        int currentCount = storage.getAll().size();
        Assertions.assertEquals(previousCount + newUsersCount, currentCount, description);
    }

    private long[] addUsers(int usersCount) {
        long[] ids = new long[usersCount];
        for (int i = 0; i < usersCount; i++) {
            ids[i] = storage.create(createUserData()).getId();
        }
        return ids;
    }

    @Test
    public void checkId_NullId() {
        String description = "Должен выбрасывать исключение при пустом id";
        Assertions.assertThrows(NullIdException.class, () -> storage.checkId(null), description);
    }

    @Test
    public void checkId_AbsentId() {
        String description = "Должен выбрасывать исключение при несуществующем id";
        Assertions.assertThrows(NotFoundException.class, () -> storage.checkId(9999999999L), description);
    }

    @Test
    public void get() {
        String description = "Должен возвращать пользователя по id";
        User created = storage.create(createUserData());
        User received = storage.get(created.getId());
        Assertions.assertEquals(created, received, description);
    }

    @Test
    public void getFriends() {
        String description = "Должен возвращать список друзей";
        long createdId = storage.create(createUserData()).getId();
        int friendsCount = 3;
        long[] friendIds = addUsers(friendsCount);
        addFriendsToId(createdId, friendIds);
        Set<User> friendsActual = storage.getFriends(createdId);
        Set<User> friendsExpected = collectToSet(friendIds);
        Assertions.assertEquals(friendsExpected, friendsActual, description);
    }

    private void addFriendsToId(long userId, long[] friendIds) {
        for (Long id : friendIds) {
            storage.addFriend(userId, id);
        }
    }

    private Set<User> collectToSet(long[] userIds) {
        Set<User> users = new HashSet<>();
        for (long userId : userIds) {
            users.add(storage.get(userId));
        }
        return users;
    }

    @Test
    public void addFriend() {
        String description = "Возвращаемая запись должна содержать список друзей";
        int friendsCount = 5;
        long createdId = storage.create(createUserData()).getId();
        long[] friendIds = addUsers(friendsCount);
        addFriendsToId(createdId, friendIds);
        Set<Long> receivedIds = storage.get(createdId).getFriends();
        Assertions.assertEquals(friendsCount, receivedIds.size(), description);
    }

    @Test
    public void deleteFriend() {
        String description = "Возвращаемая запись не должна содержать список друзей";
        long friendId = storage.create(createUserData()).getId();
        long createdId = storage.create(createUserData()).getId();
        storage.addFriend(createdId, friendId);
        storage.deleteFriend(createdId, friendId);
        Set<Long> receivedIds = storage.get(createdId).getFriends();
        Assertions.assertEquals(Set.of(), receivedIds, description);
    }

    @Test
    public void getCommonFriends() {
        String description = "Должен возвращать список общих друзей";
        long[] commonIds = createCommonFriends();
        long userId = createUserWithFriends(commonIds);
        long otherId = createOtherWithFriends(commonIds);
        Set<User> commonReceived = storage.getCommonFriends(userId, otherId);
        Set<Long> expectedCommonIdSet = getExpectedCommonSet(commonIds);
        Set<Long> commonReceivedIdSet = getAvtualCommonSet(commonReceived);
        Assertions.assertEquals(expectedCommonIdSet, commonReceivedIdSet, description);
    }

    private long[] createCommonFriends() {
        int commonFriendsCount = 3;
        return addUsers(commonFriendsCount);
    }

    private long createUserWithFriends(long[] commonIds) {
        long userId = storage.create(createUserData()).getId();
        addFriendsToId(userId, commonIds);
        int userFriendsCount = 2;
        long[] userFriends = addUsers(userFriendsCount);
        addFriendsToId(userId, userFriends);
        return userId;
    }

    private long createOtherWithFriends(long[] commonIds) {
        long otherId = storage.create(createUserData()).getId();
        addFriendsToId(otherId, commonIds);
        int otherFriendsCount = 3;
        long[] otherFriends = addUsers(otherFriendsCount);
        addFriendsToId(otherId, otherFriends);
        return otherId;
    }

    private static Set<Long> getExpectedCommonSet(long[] commonIds) {
        return Arrays.stream(commonIds)
                .boxed()
                .collect(Collectors.toSet());
    }

    private static Set<Long> getAvtualCommonSet(Set<User> commonReceived) {
        return commonReceived.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }
}
