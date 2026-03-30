package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFollowerException extends RuntimeException {
    private final long userId;
    private final long friendId;

    public NotFollowerException(long userId, long friendId) {
        super(String.format("Пользователь с id=%d не подписан на пользователя с id=%d", friendId, userId));
        this.userId = userId;
        this.friendId = friendId;
    }
}
