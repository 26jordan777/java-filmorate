package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail("");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotContainAt() {
        User user = new User();
        user.setEmail("invalidEmail.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginIsEmpty() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginIsNull() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin(null);
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        Exception exception = assertThrows(ValidationException.class, user::validate);
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldPassValidationWithValidData() throws ValidationException {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        user.validate();
    }
}