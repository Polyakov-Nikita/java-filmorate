package ru.yandex.practicum.filmorate.storage.db.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.db.storage.DbStorage;

@Component
public class MPARequestBuilder extends RequestBuilder {
    public DBRequest getAll() {
        String query = buildSelect(DbStorage.TABLE_MPA);
        return new DBRequest(query, new Object[0]);
    }

    public DBRequest getMpa(long mpaId) {
        return getById(DbStorage.TABLE_MPA, DbStorage.PARAMETER_ID, mpaId);
    }
}
