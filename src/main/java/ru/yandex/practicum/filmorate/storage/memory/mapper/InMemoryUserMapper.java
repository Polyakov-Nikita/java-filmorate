package ru.yandex.practicum.filmorate.storage.memory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.filmorate.dto.user.UserInMemory;
import ru.yandex.practicum.filmorate.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InMemoryUserMapper {
    UserInMemory toUserInMemory(User user);

    User toUser(UserInMemory userInMemory);
}
