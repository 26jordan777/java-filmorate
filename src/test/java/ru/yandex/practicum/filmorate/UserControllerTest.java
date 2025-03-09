package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully()  {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());
        assertEquals("validLogin", response.getBody().getLogin());
    }

    @Test
    void shouldSetNameToLoginWhenNameIsEmpty()  {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("validLogin", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws ValidationException {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setId(1);

        userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("validLoginUpdated");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));

        ResponseEntity<User> response = userController.updateUser(updatedUser);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNonExistentId() {
        User updatedUser = new User();
        updatedUser.setId(999);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("validLoginUpdated");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser));
        assertEquals("Пользователь с ID 999 не найден.", exception.getMessage());
    }
}