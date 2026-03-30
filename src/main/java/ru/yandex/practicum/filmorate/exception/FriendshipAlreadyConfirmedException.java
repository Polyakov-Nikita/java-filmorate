package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class FriendshipAlreadyConfirmedException extends RuntimeException {
    private final long userId;
    private final long friendId;

    public FriendshipAlreadyConfirmedException(long userId, long friendId) {
        super(String.format("Пользователи с id=%d и id=%d уже друзья", userId, friendId));
        this.userId = userId;
        this.friendId = friendId;
    }
}
