package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserUpdateRequest(@NotNull Long id,
                                @Email String email,
                                @NotBlank @Pattern(regexp = "^\\S+$",
                                        message = "Логин не может быть пустым или содержать пробелы") String login,
                                String name,
                                @Past LocalDate birthday) {
}
