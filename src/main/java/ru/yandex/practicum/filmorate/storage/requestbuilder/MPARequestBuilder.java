package ru.yandex.practicum.filmorate.storage.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.DbStorage;

@Component
public class MPARequestBuilder extends RequestBuilder {
    public DBRequest getAll() {
        String query = buildSelectQuery(DbStorage.TABLE_MPA);
        return createRequest(query);
    }

    public DBRequest getMpa(long mpaId) {
        return getById(DbStorage.TABLE_MPA, DbStorage.COLUMN_ID, mpaId);
    }
}
