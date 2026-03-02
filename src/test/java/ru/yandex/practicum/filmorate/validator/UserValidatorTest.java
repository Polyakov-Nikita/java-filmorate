package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidatorTest extends ValidatorTest {
    private static final UserValidator VALIDATOR = new UserValidator();
    private static final String EMAIL_CORRECT = "email@someshit.com";
    private static final String EMAIL_WITHOUT_AT = "email";
    private static final String LOGIN_CORRECT = "login";
    private static final String LOGIN_WITH_SPACE = "login with space";
    private static final String NAME_CORRECT = "name";
    private static final LocalDate BIRTHDAY_CORRECT = LocalDate.of(2023, 1, 1);

    @Test
    public void validate_Email_Empty() {
        String description = "Ожидается ValidationException - пустой email";
        User user = User.builder()
                .login(LOGIN_CORRECT)
                .name(NAME_CORRECT)
                .birthday(BIRTHDAY_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(user), description, UserValidator.INCORRECT_EMAIL_MESSAGE);
    }

    @Test
    public void validate_Email_WithoutAt() {
        String description = "Ожидается ValidationException - email без @";
        User user = User.builder()
                .email(EMAIL_WITHOUT_AT)
                .login(LOGIN_CORRECT)
                .name(NAME_CORRECT)
                .birthday(BIRTHDAY_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(user), description, UserValidator.INCORRECT_EMAIL_MESSAGE);
    }

    @Test
    public void validate_Login_Empty() {
        String description = "Ожидается ValidationException - пустой логин";
        User user = User.builder()
                .email(EMAIL_CORRECT)
                .name(NAME_CORRECT)
                .birthday(BIRTHDAY_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(user), description, UserValidator.INCORRECT_LOGIN_MESSAGE);
    }

    @Test
    public void validate_Login_WithSpace() {
        String description = "Ожидается ValidationException - логин с пробелами";
        User user = User.builder()
                .email(EMAIL_CORRECT)
                .login(LOGIN_WITH_SPACE)
                .name(NAME_CORRECT)
                .birthday(BIRTHDAY_CORRECT)
                .build();
        assertValidationException(() -> VALIDATOR.validate(user), description, UserValidator.INCORRECT_LOGIN_MESSAGE);
    }

    @Test
    public void validate_Name_Empty() {
        User user = User.builder()
                .email(EMAIL_CORRECT)
                .login(LOGIN_CORRECT)
                .birthday(BIRTHDAY_CORRECT)
                .build();
        Assertions.assertDoesNotThrow(() -> VALIDATOR.validate(user));
    }

    @Test
    public void validate_Birthday_Now() {
        User user = User.builder()
                .email(EMAIL_CORRECT)
                .login(LOGIN_CORRECT)
                .name(NAME_CORRECT)
                .birthday(LocalDate.now())
                .build();
        Assertions.assertDoesNotThrow(() -> VALIDATOR.validate(user));
    }

    @Test
    public void validate_Birthday_Future() {
        String description = "Ожидается ValidationException - дата рождения в будущем";
        User user = User.builder()
                .email(EMAIL_CORRECT)
                .login(LOGIN_CORRECT)
                .name(NAME_CORRECT)
                .birthday(LocalDate.now().plusDays(1))
                .build();
        assertValidationException(() -> VALIDATOR.validate(user), description, UserValidator.FUTURE_BIRTHDAY_MESSAGE);
    }
}
