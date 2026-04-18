package ru.yandex.practicum.filmorate.storage.requestbuilder;

public record DBRequest(String query,
                        Object[] params) {
}
