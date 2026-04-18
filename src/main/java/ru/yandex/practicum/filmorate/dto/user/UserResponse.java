package ru.yandex.practicum.filmorate.dto.user;

import java.time.LocalDate;
import java.util.Set;

public record UserResponse(Long id,
                           String email,
                           String login,
                           String name,
                           LocalDate birthday,
                           Set<Long> friends) {
}
