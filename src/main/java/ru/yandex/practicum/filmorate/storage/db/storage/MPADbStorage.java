package ru.yandex.practicum.filmorate.storage.db.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.db.requestbuilder.MPARequestBuilder;

import java.util.List;

@Slf4j
@Repository
public class MPADbStorage extends DbStorage implements MPAStorage {
    private final MPARequestBuilder requestBuilder;
    private final RowMapper<MPA> mpaRowMapper;

    public MPADbStorage(JdbcTemplate jdbc, MPARequestBuilder requestBuilder, RowMapper<MPA> mpaRowMapper) {
        super(jdbc);
        this.requestBuilder = requestBuilder;
        this.mpaRowMapper = mpaRowMapper;
    }

    @Override
    public List<MPA> getAll() {
        log.info("Возврат списка рейтингов");
        return selectMany(requestBuilder.getAll(), mpaRowMapper);
    }

    @Override
    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (selectOne(requestBuilder.getMpa(id), mpaRowMapper) == null) {
            throw new NotFoundException(id);
        }
    }

    @Override
    public MPA get(long id) {
        log.info("Возврат рейтинга по id={}", id);
        return selectOne(requestBuilder.getMpa(id), mpaRowMapper);
    }
}
