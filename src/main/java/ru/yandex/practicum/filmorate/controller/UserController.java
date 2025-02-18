package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long counter = 0L;


    public void validate(User user) throws ValidationException {
        log.debug("Валидация пользователя: {}", this);

        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации: {}", "Электронная почта не может быть пустой и должна содержать символ '@'.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: {}", "Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.error("Ошибка валидации: {}", "Имя пользователя не может быть пустым.");
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: {}", "Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        log.info("Пользователь {} успешно прошел валидацию.", this);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Создание пользователя: {}", user);
        validate(user);
        user.setId(++counter);
        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser) throws ValidationException {
        log.info("Обновление пользователя с id: {}", updatedUser.getId());
        if (!users.containsKey(updatedUser.getId())) {
            throw new ValidationException("Not found key: " + updatedUser.getId());
        }
        validate(updatedUser);
        users.put(updatedUser.getId(), updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("Получение всех пользователей.");
        return ResponseEntity.ok(users.values());
    }
}