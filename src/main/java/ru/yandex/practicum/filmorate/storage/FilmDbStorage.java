package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.requestbuilder.FilmRequestBuilder;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@Primary
public class FilmDbStorage extends DbStorage implements FilmStorage {
    private final FilmRequestBuilder requestBuilder;
    private final RowMapper<Film> filmRowMapper;
    private final RowMapper<FilmGenre> filmGenreRowMapper;
    private final RowMapper<MPA> mpaRowMapper;
    private final RowMapper<Genre> genreRowMapper;
    private final RowMapper<Like> likeRowMapper;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRequestBuilder requestBuilder,
                         RowMapper<Film> filmRowMapper,
                         RowMapper<FilmGenre> filmGenreRowMapper,
                         RowMapper<MPA> mpaRowMapper,
                         RowMapper<Genre> genreRowMapper,
                         RowMapper<Like> likeRowMapper) {
        super(jdbc);
        this.requestBuilder = requestBuilder;
        this.filmRowMapper = filmRowMapper;
        this.filmGenreRowMapper = filmGenreRowMapper;
        this.mpaRowMapper = mpaRowMapper;
        this.genreRowMapper = genreRowMapper;
        this.likeRowMapper = likeRowMapper;
    }

    @Override
    public Film create(Film film) {
        film.setMpaId(film.getMpa().getId());
        long filmId = insertWithId(requestBuilder.insertFilm(film));
        film.setId(filmId);
        saveFilmGenres(film);
        log.info("Фильм {} сохранён", film);
        return film;
    }

    private void saveFilmGenres(Film film) {
        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            long filmId = film.getId();
            for (Genre genre : film.getGenres()) {
                FilmGenre filmGenre = new FilmGenre(filmId, genre.getId());
                insert(requestBuilder.insertFilmGenre(filmGenre));
            }
        }
    }

    @Override
    public Film updateData(Film filmUpdate) {
        update(requestBuilder.updateFilm(filmUpdate));
        log.info("Данные фильма {} обновлены", filmUpdate);
        return filmUpdate;
    }

    @Override
    public List<Film> getAll() {
        log.info("Возврат списка фильмов");
        return selectMany(requestBuilder.getAll(), filmRowMapper);
    }

    @Override
    public void checkId(Long id) {
        if (id == null) {
            throw new NullIdException();
        }
        if (selectOne(requestBuilder.getFilm(id), filmRowMapper) == null) {
            throw new NotFoundException(id);
        }
    }

    @Override
    public Film get(long filmId) {
        log.info("Возврат фильма по id={}", filmId);
        Film film = selectOne(requestBuilder.getFilm(filmId), filmRowMapper);
        compose(film);
        return film;
    }

    private void compose(Film film) {
        addMPAToFilm(film);
        addGenresToFilm(film);
        addLikesToFilm(film);
    }

    private void addMPAToFilm(Film film) {
        MPA mpa = selectOne(requestBuilder.getMPA(film.getMpaId()), mpaRowMapper);
        film.setMpa(mpa);
    }

    private void addGenresToFilm(Film film) {
        List<FilmGenre> filmGenresList = selectMany(requestBuilder.getFilmGenres(film.getId()), filmGenreRowMapper);
        for (FilmGenre filmGenre : filmGenresList) {
            Genre genre = selectOne(requestBuilder.getGenre(filmGenre.genreId()), genreRowMapper);
            film.addGenre(genre);
        }
    }

    private void addLikesToFilm(Film film) {
        List<Like> likesList = selectMany(requestBuilder.getLikes(film.getId()), likeRowMapper);
        for (Like like : likesList) {
            film.addLike(like.userId());
        }
    }

    @Override
    public void addLike(long filmId, long likerId) {
        log.debug("Добавление лайка фильму с id={} от пользователя с id={}", filmId, likerId);
        Like like = new Like(filmId, likerId);
        insert(requestBuilder.insertLike(like));
    }

    @Override
    public void deleteLike(long filmId, long likerId) {
        log.debug("Удаление лайка с фильма с id={} от пользователя с id={}", filmId, likerId);
        update(requestBuilder.deleteLike(filmId, likerId));
    }

    @Override
    public List<Film> getPopular(int count) {
        log.debug("Получение {} самых популярных фильмов", count);
        List<Film> top = selectPopularFilms(count);
        top.forEach(this::compose);
        return top;
    }

    private List<Film> selectPopularFilms(int count) {
        return selectMany(requestBuilder.getPopular(count), filmRowMapper);
    }
}
