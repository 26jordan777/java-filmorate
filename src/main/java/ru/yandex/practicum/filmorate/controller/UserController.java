package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос на создание пользователя: {}", user);
        User createdUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> read(@PathVariable long id) throws ValidationException {
        log.info("Получен запрос на получение пользователя с идентификатором: {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> update(@Valid @RequestBody User updatedUser) throws ValidationException {
        log.info("Обновление пользователя с ID: {}", updatedUser.getId());

        User existingUser = userService.getUserById(updatedUser.getId());
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + updatedUser.getId() + " не найден.");
        }

        User updatedUserResponse = userService.updateUser(updatedUser);
        return ResponseEntity.ok(updatedUserResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("Удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        return null;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> findAll() {
        log.info("Получение списка всех пользователей");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getFriends(@PathVariable("id") Long userId) throws ValidationException {
        log.info("Получение списка друзей пользователя с ID: {}", userId);
        List<User> userFriends = userService.getFriends(userId);
        return ResponseEntity.ok(userFriends);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) throws ValidationException {
        log.info("Добавление друга с ID {} для пользователя с ID {}", friendId, userId);
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) throws ValidationException {
        log.info("Удаление друга с ID {} у пользователя с ID {}", friendId, userId);
        userService.removeFriend(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        List<User> commonFriends = userService.getFriendsCommonOther(userId, otherId);
        return ResponseEntity.ok(commonFriends);
    }

}