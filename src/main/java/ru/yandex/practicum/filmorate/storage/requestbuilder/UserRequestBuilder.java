package ru.yandex.practicum.filmorate.storage.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage;

@Component
public class UserRequestBuilder extends RequestBuilder {
    private static final String[] userColumns = {
            "email",
            "login",
            "name",
            "birthday"
    };

    private static final String[] friendshipColumns = {
            "user_id",
            "friend_id"
    };

    public DBRequest insertUser(User user) {
        String query = buildInsertQuery(DbStorage.TABLE_USERS, userColumns);
        return createRequest(query, getUserParams(user));
    }

    private Object[] getUserParams(User user) {
        Object[] params = new Object[4];
        params[0] = user.getEmail();
        params[1] = user.getLogin();
        params[2] = user.getName();
        params[3] = user.getBirthday();
        return params;
    }

    public DBRequest insertFriendship(Friendship friendship) {
        String query = buildInsertQuery(DbStorage.TABLE_FRIENDS, friendshipColumns);
        return createRequest(query, friendship.userId(), friendship.friendId());
    }

    public DBRequest updateUser(User userUpdate) {
        String query = buildUpdateQuery(DbStorage.TABLE_USERS, userColumns);
        return createRequest(query, getUserParams(userUpdate));
    }

    public DBRequest getAll() {
        String query = buildSelectQuery(DbStorage.TABLE_USERS);
        return createRequest(query);
    }

    public DBRequest getUser(long userId) {
        return getById(DbStorage.TABLE_USERS, DbStorage.COLUMN_ID, userId);
    }

    public DBRequest getFriends(long userId) {
        String query = buildGetFriendsQuery();
        return createRequest(query, userId);
    }

    private String buildGetFriendsQuery() {
        return String.format("SELECT %s.* " +
                        "FROM (SELECT %s " +
                        "FROM %s " +
                        "WHERE %s = ?) user_friends " +
                        "JOIN %s ON user_friends.%s = %s.%s",
                DbStorage.TABLE_USERS,
                DbStorage.COLUMN_FRIEND_ID,
                DbStorage.TABLE_FRIENDS,
                DbStorage.COLUMN_USER_ID,
                DbStorage.TABLE_USERS, DbStorage.COLUMN_FRIEND_ID, DbStorage.TABLE_USERS, DbStorage.COLUMN_ID);
    }

    public DBRequest getFriendships(long userId) {
        return getById(DbStorage.TABLE_FRIENDS, DbStorage.COLUMN_USER_ID, userId);
    }

    public DBRequest deleteFriend(long userId, long friendId) {
        String query = buildDeleteQuery(DbStorage.TABLE_FRIENDS, DbStorage.COLUMN_USER_ID, DbStorage.COLUMN_FRIEND_ID);
        return createRequest(query, userId, friendId);
    }

    public DBRequest getCommonFriends(long userId, long otherId) {
        String query = buildGetCommonFriendsQuery();
        return createRequest(query, userId, otherId);
    }

    private String buildGetCommonFriendsQuery() {
        return String.format("SELECT %s.* " +
                        "FROM (SELECT f1.%s " +
                        "FROM %s f1, %s f2 " +
                        "WHERE f1.%s = f2.%s " +
                        "AND f1.%s = ? " +
                        "AND f2.%s = ?) common_friends " +
                        "JOIN %s ON common_friends.%s = %s.%s",
                DbStorage.TABLE_USERS,
                DbStorage.COLUMN_FRIEND_ID,
                DbStorage.TABLE_FRIENDS, DbStorage.TABLE_FRIENDS,
                DbStorage.COLUMN_FRIEND_ID, DbStorage.COLUMN_FRIEND_ID,
                DbStorage.COLUMN_USER_ID,
                DbStorage.COLUMN_USER_ID,
                DbStorage.TABLE_USERS, DbStorage.COLUMN_FRIEND_ID, DbStorage.TABLE_USERS, DbStorage.COLUMN_ID);
    }
}
