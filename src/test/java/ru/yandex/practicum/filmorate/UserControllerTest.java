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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() throws ValidationException {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        when(userService.addUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.create(user);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("user@example.com", response.getBody().getEmail());
        assertEquals("validLogin", response.getBody().getLogin());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws ValidationException {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("validLoginUpdated");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));

        when(userService.updateUser(updatedUser)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.update(1, updatedUser);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }

    @Test
    void shouldAddFriendSuccessfully() throws ValidationException {
        Long userId = 1L;
        Long friendId = 2L;

        User user = new User();
        user.setId(userId);
        User friend = new User();
        friend.setId(friendId);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(friendId)).thenReturn(friend);

        doNothing().when(userService).addFriend(userId, friendId);

        ResponseEntity<Void> response = userController.addFriend(userId, friendId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(userService, times(1)).addFriend(userId, friendId);
    }

    @SneakyThrows
    @Test
    void shouldThrowExceptionWhenUpdatingUserWithNonExistentId() {
        User updatedUser = new User();
        updatedUser.setId(999);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("validLoginUpdated");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));

        when(userService.updateUser(updatedUser)).thenThrow(new ValidationException("Пользователь с ID 999 не найден."));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.update(999, updatedUser));
        assertEquals("Пользователь с ID 999 не найден.", exception.getMessage());
    }
}