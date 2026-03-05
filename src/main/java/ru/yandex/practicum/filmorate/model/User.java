package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User implements Model {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может быть пустым или содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
