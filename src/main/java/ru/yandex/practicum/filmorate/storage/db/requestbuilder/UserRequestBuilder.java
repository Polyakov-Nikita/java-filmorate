package ru.yandex.practicum.filmorate.storage.db.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.db.storage.DbStorage;

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
        return new DBRequest(query, getParams(user));
    }

    private Object[] getParams(User user) {
        Object[] params = new Object[4];
        params[0] = user.getEmail();
        params[1] = user.getLogin();
        params[2] = user.getName();
        params[3] = user.getBirthday();
        return params;
    }

    public DBRequest insertFriendship(Friendship friendship) {
        String query = buildInsertQuery(DbStorage.TABLE_FRIENDS, friendshipColumns);
        Object[] params = new Object[2];
        params[0] = friendship.userId();
        params[1] = friendship.friendId();
        return new DBRequest(query, params);
    }

    public DBRequest updateUser(User userUpdate) {
        String query = buildUpdate(DbStorage.TABLE_USERS, userColumns);
        return new DBRequest(query, getParams(userUpdate));
    }

    public DBRequest getAll() {
        String query = buildSelect(DbStorage.TABLE_USERS);
        return new DBRequest(query, new Object[0]);
    }

    public DBRequest getUser(long userId) {
        return getById(DbStorage.TABLE_USERS, DbStorage.PARAMETER_ID, userId);
    }

    public DBRequest getFriends(long userId) {
        return getById(DbStorage.TABLE_FRIENDS, DbStorage.PARAMETER_USER_ID, userId);
    }

    public DBRequest deleteFriend(long userId, long friendId) {
        String query = buildDelete(DbStorage.TABLE_FRIENDS, DbStorage.PARAMETER_USER_ID, DbStorage.PARAMETER_FRIEND_ID);
        Object[] params = new Object[2];
        params[0] = userId;
        params[1] = friendId;
        return new DBRequest(query, params);
    }

    public DBRequest getCommonFriends(long userId, long otherId) {
        String query = buildCommonFriendsQuery();
        Object[] params = new Object[2];
        params[0] = userId;
        params[1] = otherId;
        return new DBRequest(query, params);
    }

    private String buildCommonFriendsQuery() {
        return String.format("SELECT f1.%s, f1.%s " +
                        "FROM %s f1, %s f2 " +
                        "WHERE f1.%s = f2.%s " +
                        "AND f1.%s = ? " +
                        "AND f2.%s = ?",
                DbStorage.PARAMETER_FRIEND_ID, DbStorage.PARAMETER_USER_ID,
                DbStorage.TABLE_FRIENDS, DbStorage.TABLE_FRIENDS,
                DbStorage.PARAMETER_FRIEND_ID, DbStorage.PARAMETER_FRIEND_ID,
                DbStorage.PARAMETER_USER_ID,
                DbStorage.PARAMETER_USER_ID);
    }
}
