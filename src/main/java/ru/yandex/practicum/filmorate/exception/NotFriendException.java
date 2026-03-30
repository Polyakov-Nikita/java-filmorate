package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFriendException extends RuntimeException {
    private final long userId;
    private final long friendId;

    public NotFriendException(long userId, long friendId) {
        super(String.format("Пользователь с id=%d не является другом пользователя с id=%d", friendId, userId));
        this.userId = userId;
        this.friendId = friendId;
    }
}
