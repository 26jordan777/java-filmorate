package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ValidationException;

import java.time.LocalDate;

@Data
@Slf4j
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void validate() throws ValidationException {
        log.debug("Валидация пользователя: {}", this);
        if (email == null || email.isEmpty() || !email.contains("@")){
            log.error("Ошибка валидации: {}", "Электронная почта не может быть пустой и должна содержать символ '@'.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }

        if (login == null || login.isEmpty() || login.contains(" ")){
            log.error("Ошибка валидации: {}", "Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: {}", "Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        log.info("Пользователь {} успешно прошел валидацию.", this);
    }
}
