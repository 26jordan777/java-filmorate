package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class Film {
    @Getter
    private long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой.")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительным числом.")
    private long duration;

    private Set<Long> likes = new HashSet<>();

    public void addLike(long userId) {
        likes.add(userId);
        log.info("Пользователь с ID {} добавил лайк к фильму с ID {}", userId, this.id);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
        log.info("Пользователь с ID {} убрал лайк у фильма с ID {}", userId, this.id);
    }

    public int getLikeCount() {
        return likes.size();
    }
}