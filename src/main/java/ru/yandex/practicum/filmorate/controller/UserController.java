package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        users.add(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        log.info("Обновление пользователя с id: {}", id);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                users.set(i, updatedUser);
                return ResponseEntity.ok(updatedUser);
            }
        }
        log.warn("Пользователь с id: {} не найден.", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Получение всех пользователей.");
        return ResponseEntity.ok(users);
    }
}
