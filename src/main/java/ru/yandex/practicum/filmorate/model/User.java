package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

}