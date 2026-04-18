package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage storage;
    private final GenreDbStorage genreStorage;
    private final UserDbStorage userStorage;

    @Test
    public void create() {
        String description = "Не должно выбрасываться исключений";
        Assertions.assertDoesNotThrow(() -> storage.create(createFilmData()).getId(), description);
    }

    private Film createFilmData() {
        return Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(2)
                .mpa(new MPA(1L, "G"))
                .build();
    }

    @Test
    public void updateData() {
        String description = "Должен обновить данные";
        Film created = storage.create(createFilmData());
        created.setName("newName");
        created.setDescription("newDescription");
        created.setReleaseDate(LocalDate.now());
        created.setDuration(5);
        Film updated = storage.updateData(created);
        Assertions.assertEquals(created, updated, description);
    }

    @Test
    public void getAll() {
        String description = "Количество фильмов должно увеличиться";
        int previousCount = storage.getAll().size();
        int newFilmsCount = 3;
        addFilms(newFilmsCount);
        int currentCount = storage.getAll().size();
        Assertions.assertEquals(previousCount + newFilmsCount, currentCount, description);
    }

    private void addFilms(int filmsCount) {
        for (int i = 0; i < filmsCount; i++) {
            storage.create(createFilmData());
        }
    }

    @Test
    public void checkId_NullId() {
        String description = "Должен выбрасывать исключение при пустом id";
        Assertions.assertThrows(NullIdException.class, () -> storage.checkId(null), description);
    }

    @Test
    public void checkId_AbsentId() {
        String description = "Должен выбрасывать исключение при несуществующем id";
        Assertions.assertThrows(NotFoundException.class, () -> storage.checkId(9999999999L), description);
    }

    @Test
    public void get() {
        String description = "Должен возвращать фильм по id";
        Film created = storage.create(createFilmData());
        Film received = storage.get(created.getId());
        Assertions.assertEquals(created, received, description);
    }

    @Test
    public void get_ContainsMPA() {
        String description = "Должен содержать рейтинг";
        Film filmData = createFilmData();
        filmData.setId(storage.create(filmData).getId());
        Film received = storage.get(filmData.getId());
        Assertions.assertEquals(filmData.getMpa(), received.getMpa(), description);
    }

    @Test
    public void get_ContainsGenres() {
        String description = "Должен содержать список жанров";
        Film filmData = createFilmData();
        addGenres(filmData);
        filmData.setId(storage.create(filmData).getId());
        Film received = storage.get(filmData.getId());
        Assertions.assertEquals(filmData.getGenres(), received.getGenres(), description);
    }

    private void addGenres(Film film) {
        List<Genre> genres = genreStorage.getAll();
        for (Genre genre : genres) {
            film.addGenre(genre);
        }
    }

    @Test
    public void addLike() {
        String description = "Возвращаемая запись должна содержать список лайков";
        long createdId = storage.create(createFilmData()).getId();
        int likesCount = 5;
        addLikes(createdId, likesCount);
        Set<Long> receivedIds = storage.get(createdId).getLikes();
        Assertions.assertEquals(likesCount, receivedIds.size(), description);
    }

    private void addLikes(long filmId, int likesCount) {
        for (int i = 0; i < likesCount; i++) {
            long likerId = userStorage.create(createLiker()).getId();
            storage.addLike(filmId, likerId);
        }
    }

    private User createLiker() {
        return User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();
    }

    @Test
    public void deleteLike() {
        String description = "Фильм не должен содержать лайков";
        long filmId = storage.create(createFilmData()).getId();
        long likerId = userStorage.create(createLiker()).getId();
        storage.addLike(filmId, likerId);
        storage.deleteLike(filmId, likerId);
        Film received = storage.get(filmId);
        Assertions.assertEquals(Set.of(), received.getLikes(), description);
    }

    @Test
    public void getPopular() {
        String description = "Должен содержать список фильмов с самым большим количеством лайков";
        int filmsCount = 5;
        List<Film> expectedList = createMostPopular(filmsCount);
        List<Film> actualList = storage.getPopular(filmsCount);
        Assertions.assertArrayEquals(expectedList.toArray(), actualList.toArray(), description);
    }

    private List<Film> createMostPopular(int filmsCount) {
        List<Film> top = new ArrayList<>();
        for (int i = 0; i < filmsCount; i++) {
            top.add(createWithLikes(filmsCount - i));
        }
        return top;
    }

    private Film createWithLikes(int likesCount) {
        long filmId = storage.create(createFilmData()).getId();
        addLikes(filmId, likesCount);
        return storage.get(filmId);
    }
}
