package ru.yandex.practicum.filmorate.storage.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.DbStorage;

@Component
public class GenreRequestBuilder extends RequestBuilder {
    public DBRequest getAll() {
        String query = buildSelectQuery(DbStorage.TABLE_GENRES);
        return createRequest(query);
    }

    public DBRequest getGenre(long genreId) {
        return getById(DbStorage.TABLE_GENRES, DbStorage.COLUMN_ID, genreId);
    }
}
