package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullIdException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADbStorage;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPADbStorageTest {
    private final MPADbStorage storage;

    @Test
    public void getAll() {
        String description = "Количество рейтингов должно быть больше нуля";
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
        String description = "Должен возвращать рейтинг по id";
        MPA expected = new MPA(1L, "G");
        MPA received = storage.get(1L);
        Assertions.assertEquals(expected, received, description);
    }
}
