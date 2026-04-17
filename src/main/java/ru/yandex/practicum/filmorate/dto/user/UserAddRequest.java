package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Set;

public record UserAddRequest(@Email String email,
                             @NotBlank @Pattern(regexp = "^\\S+$",
                                     message = "Логин не может быть пустым или содержать пробелы") String login,
                             String name,
                             @Past LocalDate birthday,
                             Set<Long> friends) {
}
