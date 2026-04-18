package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage storage;

    @Test
    public void getAll() {
        String description = "Количество жанров должно быть больше нуля";
        int currentCount = storage.getAll().size();
        Assertions.assertTrue(currentCount > 0, description);
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
        String description = "Должен возвращать жанр по id";
        Genre expected = new Genre(1L, "Комедия");
        Genre received = storage.get(1L);
        Assertions.assertEquals(expected, received, description);
    }
}
