package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class User {
    private long id;

    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ '@'.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public void addFriend(long friendId) {
        friends.add(friendId);
        log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", this.id, friendId);
    }

    public void removeFriend(long friendId) {
        friends.remove(friendId);
        log.info("Пользователь с ID {} убрал из друзей пользователя с ID {}", this.id, friendId);
    }
}