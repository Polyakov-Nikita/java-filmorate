package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.mapper.UserMapper;
import ru.yandex.practicum.filmorate.dto.user.UserAddRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse add(@Valid @RequestBody UserAddRequest request) {
        User user = mapper.toUser(request);
        User result = service.create(user);
        return mapper.toUserResponse(result);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@Valid @RequestBody UserUpdateRequest request) {
        User film = mapper.toUser(request);
        User result = service.update(film);
        return mapper.toUserResponse(result);
    }

    @PutMapping("/{userId}" + URL_FRIENDS + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        service.addFriend(userId, friendId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAll() {
        List<User> result = service.getAll();
        return result.stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse get(@PathVariable long userId) {
        User result = service.get(userId);
        return mapper.toUserResponse(result);
    }

    @GetMapping("/{userId}" + URL_FRIENDS)
    @ResponseStatus(HttpStatus.OK)
    public Set<UserResponse> getFriends(@PathVariable long userId) {
        Set<User> result = service.getFriends(userId);
        return result.stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{userId}" + URL_FRIENDS + URL_COMMON + "/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<UserResponse> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        Set<User> result = service.getCommonFriends(userId, otherId);
        return result.stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toSet());
    }

    @DeleteMapping("/{userId}" + URL_FRIENDS + "/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        service.deleteFriend(userId, friendId);
    }
}
