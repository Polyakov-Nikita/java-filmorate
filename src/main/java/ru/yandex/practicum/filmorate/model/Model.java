package ru.yandex.practicum.filmorate.model;

public interface Model<M extends Model<M>> {
    void setId(Long id);

    Long getId();

    void update(M update);
}
