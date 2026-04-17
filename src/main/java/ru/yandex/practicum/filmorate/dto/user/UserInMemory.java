package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.storage.memory.MemoryEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserInMemory implements MemoryEntity<UserInMemory> {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        friends.remove(friendId);
    }
}
