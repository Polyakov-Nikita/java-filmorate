package ru.yandex.practicum.filmorate.storage.db.requestbuilder;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.db.storage.DbStorage;

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
        return new DBRequest(query, getParams(film));
    }

    private Object[] getParams(Film film) {
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
        Object[] params = new Object[2];
        params[0] = filmGenre.filmId();
        params[1] = filmGenre.genreId();
        return new DBRequest(query, params);
    }

    public DBRequest insertLike(Like like) {
        String query = buildInsertQuery(DbStorage.TABLE_LIKES, likeColumns);
        Object[] params = new Object[2];
        params[0] = like.filmId();
        params[1] = like.userId();
        return new DBRequest(query, params);
    }

    public DBRequest updateFilm(Film filmUpdate) {
        String query = buildUpdate(DbStorage.TABLE_FILMS, filmColumns);
        return new DBRequest(query, getParams(filmUpdate));
    }

    public DBRequest getAll() {
        String query = buildSelect(DbStorage.TABLE_FILMS);
        return new DBRequest(query, new Object[0]);
    }

    public DBRequest getFilm(long filmId) {
        return getById(DbStorage.TABLE_FILMS, DbStorage.PARAMETER_ID, filmId);
    }

    public DBRequest getMPA(long mpaId) {
        return getById(DbStorage.TABLE_MPA, DbStorage.PARAMETER_ID, mpaId);
    }

    public DBRequest getFilmGenres(long filmId) {
        return getById(DbStorage.TABLE_GENRES_FILMS, DbStorage.PARAMETER_FILM_ID, filmId);
    }

    public DBRequest getGenre(long genreId) {
        return getById(DbStorage.TABLE_GENRES, DbStorage.PARAMETER_ID, genreId);
    }

    public DBRequest getLikes(long filmId) {
        return getById(DbStorage.TABLE_LIKES, DbStorage.PARAMETER_FILM_ID, filmId);
    }

    public DBRequest deleteLike(long filmId, long likerId) {
        String query = buildDelete(DbStorage.TABLE_LIKES, DbStorage.PARAMETER_FILM_ID, DbStorage.PARAMETER_USER_ID);
        Object[] params = new Object[2];
        params[0] = filmId;
        params[1] = likerId;
        return new DBRequest(query, params);
    }

    public DBRequest getPopular(int count) {
        String query = buildPopularQuery();
        Object[] params = new Object[1];
        params[0] = count;
        return new DBRequest(query, params);
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
                DbStorage.PARAMETER_FILM_ID,
                DbStorage.TABLE_LIKES,
                DbStorage.PARAMETER_FILM_ID,
                DbStorage.PARAMETER_USER_ID,
                DbStorage.TABLE_FILMS, DbStorage.PARAMETER_FILM_ID);
    }
}
