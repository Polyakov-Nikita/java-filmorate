package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.FilmInMemory;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.mapper.InMemoryFilmMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class InMemoryFilmStorage extends ModelContainer<FilmInMemory> implements FilmStorage {
    private final InMemoryFilmMapper mapper;

    @Override
    public Film create(Film film) {
        FilmInMemory filmInMemory = mapper.toFilmInMemory(film);
        FilmInMemory result = create(filmInMemory);
        return mapper.toFilm(result);
    }

    @Override
    public Film updateData(Film filmUpdate) {
        long filmId = filmUpdate.getId();
        FilmInMemory filmInMemory = getFromMemory(filmId);
        filmInMemory.setName(filmUpdate.getName());
        filmInMemory.setDescription(filmUpdate.getDescription());
        filmInMemory.setReleaseDate(filmUpdate.getReleaseDate());
        filmInMemory.setDuration(filmUpdate.getDuration());
        log.info("Данные фильма {} обновлены", filmInMemory);
        return mapper.toFilm(filmInMemory);
    }

    @Override
    public List<Film> getAll() {
        List<FilmInMemory> result = getAllFromMemory();
        return result.stream()
                .map(mapper::toFilm)
                .toList();
    }

    @Override
    public Film get(long filmId) {
        FilmInMemory result = getFromMemory(filmId);
        return mapper.toFilm(result);
    }

    @Override
    public void addLike(long filmId, long likerId) {
        log.debug("Добавление лайка фильму с id={} от пользователя с id={}", filmId, likerId);
        getFromMemory(filmId).addLike(likerId);
    }

    @Override
    public List<Film> getPopular(int count) {
        log.debug("Получение {} самых популярных фильмов", count);
        return getAllFromMemory().stream()
                .sorted(Comparator.comparing(FilmInMemory::getLikes, Comparator.comparingInt(Set::size)).reversed())
                .limit(count)
                .map(mapper::toFilm)
                .toList();
    }

    @Override
    public void deleteLike(long filmId, long likerId) {
        log.debug("Удаление лайка с фильма с id={} от пользователя с id={}", filmId, likerId);
        getFromMemory(filmId).deleteLike(likerId);
    }
}
