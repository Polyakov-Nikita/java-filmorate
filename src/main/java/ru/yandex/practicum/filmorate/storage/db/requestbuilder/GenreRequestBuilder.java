package ru.yandex.practicum.filmorate.storage.db.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.db.storage.DbStorage;

@Component
public class GenreRequestBuilder extends RequestBuilder {
    public DBRequest getAll() {
        String query = buildSelect(DbStorage.TABLE_GENRES);
        return new DBRequest(query, new Object[0]);
    }

    public DBRequest getGenre(long genreId) {
        return getById(DbStorage.TABLE_GENRES, DbStorage.PARAMETER_ID, genreId);
    }
}
