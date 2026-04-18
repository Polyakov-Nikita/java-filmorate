package ru.yandex.practicum.filmorate.storage.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@SuppressWarnings("unused")
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        Date releaseDate = rs.getDate("release_date");
        film.setReleaseDate(releaseDate.toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpaId(rs.getLong("mpa_id"));
        return film;
    }
}
