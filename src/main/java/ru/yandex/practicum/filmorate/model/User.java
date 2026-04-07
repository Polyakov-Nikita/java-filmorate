package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exception.FriendshipAlreadyConfirmedException;
import ru.yandex.practicum.filmorate.exception.NotFollowerException;
import ru.yandex.practicum.filmorate.exception.NotFriendException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Model<User> {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может быть пустым или содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();
    @Builder.Default
    private Set<Long> subscriptions = new HashSet<>();

    @Override
    public void update(User update) {
        email = update.email;
        login = update.login;
        name = update.name;
        birthday = update.birthday;
    }

    public void follow(User other) {
        if (friends.contains(other.id)) {
            throw new FriendshipAlreadyConfirmedException(id, other.id);
        }
        subscriptions.add(other.id);
    }

    public void confirmFriendship(User other) {
        checkSubscription(other);
        friends.add(other.id);
        other.moveToFriends(id);
    }

    private void checkSubscription(User follower) {
        if (!follower.subscriptions.contains(id)) {
            throw new NotFollowerException(id, follower.id);
        }
    }

    private void moveToFriends(Long id) {
        subscriptions.remove(id);
        friends.add(id);
    }

    public void deleteFriend(User friend) {
        if (!friends.contains(friend.id)) {
            throw new NotFriendException(id, friend.id);
        }
        friends.remove(friend.id);
        friend.moveToFollowers(id);
    }

    private void moveToFollowers(Long id) {
        friends.remove(id);
        subscriptions.add(id);
    }
}
