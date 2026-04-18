package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.UserAddRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(UserController.URL_BASE)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserController {
    public static final String URL_BASE = "/users";
    public static final String URL_FRIENDS = "/friends";
    public static final String URL_COMMON = "/common";

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse add(@Valid @RequestBody UserAddRequest request) {
        return service.create(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@Valid @RequestBody UserUpdateRequest request) {
        return service.update(request);
    }

    @PutMapping("/{userId}" + URL_FRIENDS + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        service.addFriend(userId, friendId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse get(@PathVariable long userId) {
        return service.get(userId);
    }

    @GetMapping("/{userId}" + URL_FRIENDS)
    @ResponseStatus(HttpStatus.OK)
    public Set<UserResponse> getFriends(@PathVariable long userId) {
        return service.getFriends(userId);
    }

    @GetMapping("/{userId}" + URL_FRIENDS + URL_COMMON + "/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<UserResponse> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return service.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}" + URL_FRIENDS + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        service.deleteFriend(userId, friendId);
    }
}
