package ru.yandex.practicum.filmorate.exception;

public class NullIdException extends RuntimeException {
    public NullIdException() {
        super("Значение параметра id модели не установлено");
    }
}
