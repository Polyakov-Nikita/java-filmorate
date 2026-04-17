package ru.yandex.practicum.filmorate.storage.db.requestbuilder;

public record DBRequest(String query,
                        Object[] params) {
}
