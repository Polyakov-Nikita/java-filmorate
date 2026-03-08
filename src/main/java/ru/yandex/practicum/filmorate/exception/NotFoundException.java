package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public final Long id;

    public NotFoundException(Long id) {
        super(String.format("Модель с id <%d> не найдена в контейнере", id));
        this.id = id;
    }
}
