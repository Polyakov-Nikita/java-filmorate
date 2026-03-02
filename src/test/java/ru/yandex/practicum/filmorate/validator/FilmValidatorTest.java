package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidatorTest extends ValidatorTest {
    private static final FilmValidator VALIDATOR = new FilmValidator();
    private static final String NAME_CORRECT = "Film Title";
    private static final String DESCRIPTION_CORRECT = "Description";
    private static final String DESCRIPTION_MAX_LENGTH = String.valueOf('d').repeat(FilmValidator.MAX_DESCRIPTION_LENGTH);
    private static final LocalDate DATE_CORRECT = LocalDate.of(2023, 1, 1);
    private static final LocalDate DATE_EARLY = FilmValidator.MIN_RELEASE_DATE.minusDays(1);
    private static final int DURATION_CORRECT = 120;
    private static final int DURATION_NEGATIVE = -5;

    @Test
    public void validate_Name_Empty() {
        String description = "Ожидается ValidationException - пустое имя";
        Film film = Film.builder()
                .description(DESCRIPTION_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(DURATION_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.EMPTY_NAME_MESSAGE);
    }

    @Test
    public void validate_Description_Empty() {
        String description = "Ожидается ValidationException - пустое описание";
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(DURATION_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.EMPTY_DESCRIPTION_MESSAGE);
    }

    @Test
    public void validate_Description_MaxLength() {
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_MAX_LENGTH)
                .releaseDate(DATE_CORRECT)
                .duration(DURATION_CORRECT)
                .build();
        Assertions.assertDoesNotThrow(() -> VALIDATOR.validate(film));
    }

    @Test
    public void validate_Description_LengthIsGreaterThanMax() {
        String description = "Ожидается ValidationException - слишком длинное описание";
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_MAX_LENGTH + DESCRIPTION_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(DURATION_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.LONG_DESCRIPTION_MESSAGE);
    }

    @Test
    public void validate_Release_EarlyRelease() {
        String description = "Ожидается ValidationException - слишком ранняя дата релиза";
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_CORRECT)
                .releaseDate(DATE_EARLY)
                .duration(DURATION_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.EARLY_RELEASE_MESSAGE);
    }

    @Test
    public void validate_Release_MinDate() {
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_CORRECT)
                .releaseDate(FilmValidator.MIN_RELEASE_DATE)
                .duration(DURATION_CORRECT)
                .build();
        Assertions.assertDoesNotThrow(() -> VALIDATOR.validate(film));
    }

    @Test
    public void validate_Duration_DurationIsNegative() {
        String description = "Ожидается ValidationException - отрицательная продолжительность";
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(DURATION_NEGATIVE)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.NON_POSITIVE_DURATION_MESSAGE);
    }

    @Test
    public void validate_Duration_DurationIs0() {
        String description = "Ожидается ValidationException - нулевая продолжительность";
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(0)
                .build();
        assertValidationException(() -> VALIDATOR.validate(film), description, FilmValidator.NON_POSITIVE_DURATION_MESSAGE);
    }

    @Test
    public void validate_Duration_DurationIs1() {
        Film film = Film.builder()
                .name(NAME_CORRECT)
                .description(DESCRIPTION_CORRECT)
                .releaseDate(DATE_CORRECT)
                .duration(1)
                .build();
        Assertions.assertDoesNotThrow(() -> VALIDATOR.validate(film));
    }
}
