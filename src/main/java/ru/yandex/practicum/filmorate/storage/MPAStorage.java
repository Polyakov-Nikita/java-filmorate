package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {
    List<MPA> getAll();

    void checkId(Long id);

    MPA get(long id);
}
