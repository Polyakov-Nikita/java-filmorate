package ru.yandex.practicum.filmorate.storage.db.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.requestbuilder.GenreRequestBuilder;

import java.util.List;

@Slf4j
@Repository
public class GenreDbStorage extends DbStorage implements GenreStorage {
    private final GenreRequestBuilder requestBuilder;
    private final RowMapper<Genre> genreRowMapper;

    public GenreDbStorage(JdbcTemplate jdbc, GenreRequestBuilder requestBuilder, RowMapper<Genre> genreRowMapper) {
        super(jdbc);
        this.requestBuilder = requestBuilder;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<Genre> getAll() {
        log.info("Возврат списка жанров");
        return selectMany(requestBuilder.getAll(), genreRowMapper);
    }

    @Override
    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (selectOne(requestBuilder.getGenre(id), genreRowMapper) == null) {
            throw new NotFoundException(id);
        }
    }

    @Override
    public Genre get(long id) {
        log.info("Возврат жанра по id={}", id);
        return selectOne(requestBuilder.getGenre(id), genreRowMapper);
    }
}
