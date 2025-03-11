package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> update(@PathVariable long id, @Valid @RequestBody User updatedUser) throws ValidationException {
        log.info("Обновление пользователя с ID: {}", id);
        updatedUser.setId(id);
        User user = userService.updateUser(updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<User>> findAll() {
        log.info("Получение списка всех пользователей");
        Collection<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getFriends(@PathVariable("id") Long userId) throws ValidationException {
        log.info("Вызван метод GET /users/{id}/friends с id = {}", userId);
        List<User> userFriends = userService.getFriends(userId);
        return ResponseEntity.ok(userFriends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getFriendsCommonOther(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherId) throws ValidationException {
        log.info("Вызван метод GET /users/{id}/friends/common/{otherId} с id = {} и otherId = {}", userId, otherId);
        List<User> commonFriends = userService.getFriendsCommonOther(userId, otherId);
        return ResponseEntity.ok(commonFriends);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) throws ValidationException {
        log.info("Вызван метод PUT /{id}/friends/{friendId} с id = {} и friendId = {}", userId, friendId);
        List<User> userFriends = userService.addFriend(userId, friendId);
        return ResponseEntity.ok(userFriends);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) throws ValidationException {
        log.info("Вызван метод DELETE /{id}/friends/{friendId} с id = {} и friendId = {}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("Метод DELETE /{id}/friends/{friendId} успешно выполнен");
    }
}