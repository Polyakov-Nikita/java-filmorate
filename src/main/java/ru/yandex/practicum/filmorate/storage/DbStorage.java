package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.storage.requestbuilder.DBRequest;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public abstract class DbStorage {
    public static final String TABLE_GENRES = "genres";
    public static final String TABLE_FILMS = "films";
    public static final String TABLE_MPA = "mpa";
    public static final String TABLE_GENRES_FILMS = "genres_films";
    public static final String TABLE_LIKES = "likes";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_FRIENDS = "friends";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FILM_ID = "film_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FRIEND_ID = "friend_id";

    protected final JdbcTemplate jdbc;

    protected void insert(DBRequest request) {
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(request.query());
            Object[] params = request.params();
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        });
    }

    protected long insertWithId(DBRequest request) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(request.query(), Statement.RETURN_GENERATED_KEYS);
            Object[] params = request.params();
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return id;
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
    }

    protected void update(DBRequest request) {
        jdbc.update(request.query(), request.params());
    }

    protected <E> List<E> selectMany(DBRequest request, RowMapper<E> mapper) {
        return jdbc.query(request.query(), mapper, request.params());
    }

    protected <E> E selectOne(DBRequest request, RowMapper<E> mapper) {
        try {
            return jdbc.queryForObject(request.query(), mapper, request.params());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
