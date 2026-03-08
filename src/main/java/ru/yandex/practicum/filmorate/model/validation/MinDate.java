package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface MinDate {
    String message() default "Дата должна быть не раньше 28.12.1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
