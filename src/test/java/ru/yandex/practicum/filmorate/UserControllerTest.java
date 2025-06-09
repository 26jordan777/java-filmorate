package ru.yandex.practicum.filmorate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        user = new User("user@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
    }

    @Test
    void shouldCreateUserSuccessfully() throws ValidationException {
        when(userService.addUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.create(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());
        assertEquals("validLogin", response.getBody().getLogin());
    }

    @Test
    public void shouldUpdateUserSuccessfully() throws ValidationException {
        user.setId(1);
        when(userService.getUserById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        user.setName("Updated Name");
        ResponseEntity<User> response = userController.update(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User updatedUser = response.getBody();
        assertThat(updatedUser).isNotNull();
        assertEquals("Updated Name", updatedUser.getName());
    }

    @SneakyThrows
    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNonExistentId() {
        user.setId(999);
        when(userService.getUserById(999)).thenReturn(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Пользователь с ID 999 не найден.", exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("user@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);


        userDbStorage.deleteUser(addedUser.getId());


        User deletedUser = userDbStorage.getUserById(addedUser.getId());
        assertThat(deletedUser).isNull();
    }
}