package ru.yandex.practicum.filmorate.storage.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.DbStorage;

@Component
public class FilmRequestBuilder extends RequestBuilder {
    private static final String[] filmColumns = {
            "name",
            "description",
            "release_date",
            "duration",
            "mpa_id"
    };

    private static final String[] filmGenreColumns = {
            "film_id",
            "genre_id"
    };

    private static final String[] likeColumns = {
            "film_id",
            "user_id"
    };

    public DBRequest insertFilm(Film film) {
        String query = buildInsertQuery(DbStorage.TABLE_FILMS, filmColumns);
        return createRequest(query, getFilmParams(film));
    }

    private Object[] getFilmParams(Film film) {
        Object[] params = new Object[5];
        params[0] = film.getName();
        params[1] = film.getDescription();
        params[2] = film.getReleaseDate();
        params[3] = film.getDuration();
        params[4] = film.getMpaId();
        return params;
    }

    public DBRequest insertFilmGenre(FilmGenre filmGenre) {
        String query = buildInsertQuery(DbStorage.TABLE_GENRES_FILMS, filmGenreColumns);
        return createRequest(query, filmGenre.filmId(), filmGenre.genreId());
    }

    public DBRequest insertLike(Like like) {
        String query = buildInsertQuery(DbStorage.TABLE_LIKES, likeColumns);
        return createRequest(query, like.filmId(), like.userId());
    }

    public DBRequest updateFilm(Film filmUpdate) {
        String query = buildUpdateQuery(DbStorage.TABLE_FILMS, filmColumns);
        return createRequest(query, getFilmParams(filmUpdate));
    }

    public DBRequest getAll() {
        String query = buildSelectQuery(DbStorage.TABLE_FILMS);
        return createRequest(query);
    }

    public DBRequest getFilm(long filmId) {
        return getById(DbStorage.TABLE_FILMS, DbStorage.COLUMN_ID, filmId);
    }

    public DBRequest getMPA(long mpaId) {
        return getById(DbStorage.TABLE_MPA, DbStorage.COLUMN_ID, mpaId);
    }

    public DBRequest getFilmGenres(long filmId) {
        return getById(DbStorage.TABLE_GENRES_FILMS, DbStorage.COLUMN_FILM_ID, filmId);
    }

    public DBRequest getGenre(long genreId) {
        return getById(DbStorage.TABLE_GENRES, DbStorage.COLUMN_ID, genreId);
    }

    public DBRequest getLikes(long filmId) {
        return getById(DbStorage.TABLE_LIKES, DbStorage.COLUMN_FILM_ID, filmId);
    }

    public DBRequest deleteLike(long filmId, long likerId) {
        String query = buildDeleteQuery(DbStorage.TABLE_LIKES, DbStorage.COLUMN_FILM_ID, DbStorage.COLUMN_USER_ID);
        return createRequest(query, filmId, likerId);
    }

    public DBRequest getPopular(int count) {
        String query = buildPopularQuery();
        return createRequest(query, count);
    }

    private String buildPopularQuery() {
        return String.format("SELECT films.* " +
                        "FROM (" +
                        "SELECT %s " +
                        "FROM %s " +
                        "GROUP BY %s " +
                        "ORDER BY COUNT(%s) DESC " +
                        "LIMIT ?" +
                        ") top " +
                        "JOIN %s films ON top.%s = films.id",
                DbStorage.COLUMN_FILM_ID,
                DbStorage.TABLE_LIKES,
                DbStorage.COLUMN_FILM_ID,
                DbStorage.COLUMN_USER_ID,
                DbStorage.TABLE_FILMS, DbStorage.COLUMN_FILM_ID);
    }
}
