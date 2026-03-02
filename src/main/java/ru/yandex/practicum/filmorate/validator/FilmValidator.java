package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator implements ModelValidator<Film> {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1985, 12, 28);
    public static final String EMPTY_NAME_MESSAGE = "Название фильма не может быть пустым";
    public static final String EMPTY_DESCRIPTION_MESSAGE = "Описание фильма не может быть пустым";
    public static final String LONG_DESCRIPTION_MESSAGE = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов";
    public static final String EARLY_RELEASE_MESSAGE = "Дата релиза должна быть не раньше " + MIN_RELEASE_DATE;
    public static final String NON_POSITIVE_DURATION_MESSAGE = "Продолжительность фильма должна быть положительным числом";

    @Override
    public void validate(Film model) {
        log.debug("Валидация модели {}", model);
        checkName(model.getName());
        checkDescription(model.getDescription());
        checkRelease(model.getReleaseDate());
        checkDuration(model.getDuration());
    }

    private void checkName(String name) {
        log.trace("Проверка названия \"{}\"", name);
        if (name.isBlank()) {
            throw new ValidationException(EMPTY_NAME_MESSAGE);
        }
    }

    private void checkDescription(String description) {
        log.trace("Проверка описания \"{}\"", description);
        if (description.isBlank()) {
            throw new ValidationException(EMPTY_DESCRIPTION_MESSAGE);
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException(LONG_DESCRIPTION_MESSAGE);
        }
    }

    private void checkRelease(LocalDate release) {
        log.trace("Проверка даты релиза \"{}\"", release);
        if (release.isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException(EARLY_RELEASE_MESSAGE);
        }
    }

    private void checkDuration(int durationMins) {
        log.trace("Проверка длительности \"{}\"", durationMins);
        if (durationMins < 1) {
            throw new ValidationException(NON_POSITIVE_DURATION_MESSAGE);
        }
    }
}
