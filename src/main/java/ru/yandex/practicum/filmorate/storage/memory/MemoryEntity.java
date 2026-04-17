package ru.yandex.practicum.filmorate.storage.memory;

public interface MemoryEntity<M extends MemoryEntity<M>> {
    void setId(Long id);

    Long getId();
}
