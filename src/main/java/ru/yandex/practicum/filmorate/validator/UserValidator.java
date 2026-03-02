package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator implements ModelValidator<User> {
    public static final String INCORRECT_EMAIL_MESSAGE = "Некорректный email";
    public static final String INCORRECT_LOGIN_MESSAGE = "Некорректный логин";
    public static final String FUTURE_BIRTHDAY_MESSAGE = "Дата рождения не может быть в будущем";

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+$";
    private static final String LOGIN_REGEX = "^\\S+$";

    @Override
    public void validate(User model) {
        log.debug("Валидация модели {}", model);
        checkEmail(model.getEmail());
        checkLogin(model.getLogin());
        checkBirthday(model.getBirthday());
    }

    private void checkEmail(String email) {
        log.trace("Проверка email \"{}\"", email);
        if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException(INCORRECT_EMAIL_MESSAGE);
        }
    }

    private void checkLogin(String login) {
        log.trace("Проверка логина \"{}\"", login);
        if (!login.matches(LOGIN_REGEX)) {
            throw new ValidationException(INCORRECT_LOGIN_MESSAGE);
        }
    }

    private void checkBirthday(LocalDate birthday) {
        log.trace("Проверка даты рождения \"{}\"", birthday);
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException(FUTURE_BIRTHDAY_MESSAGE);
        }
    }
}
