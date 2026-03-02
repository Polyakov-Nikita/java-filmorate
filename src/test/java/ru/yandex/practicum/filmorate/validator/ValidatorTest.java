package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.exception.ValidationException;

public abstract class ValidatorTest {
    protected void assertValidationException(Runnable method, String description, String expectedMessage) {
        Assertions.assertThrows(ValidationException.class, method::run, description);
        assertExceptionMessage(method, expectedMessage);
    }

    private void assertExceptionMessage(Runnable method, String expectedMessage) {
        String description = String.format("Ожидается сообщение об ошибке <%s>", expectedMessage);
        try {
            method.run();
        } catch (ValidationException e) {
            Assertions.assertEquals(expectedMessage, e.getMessage(), description);
        }
    }
}
