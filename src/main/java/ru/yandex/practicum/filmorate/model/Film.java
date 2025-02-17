package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ValidationException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Slf4j
public class Film {
    private long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой.")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом.")
    private long duration;

    public void validate() throws ValidationException {
        log.debug("Валидация фильма: {}", this);

        if (name == null || name.isEmpty()) {
            log.error("Ошибка валидации: {}", "Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (description != null && description.length() > 200) {
            log.error("Ошибка валидации: {}", "Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }

        if (releaseDate != null && releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: {}", "Дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (duration <= 0) {
            log.error("Ошибка валидации: {}", "Продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }

        log.info("Фильм {} успешно прошел валидацию.", this);
    }
}