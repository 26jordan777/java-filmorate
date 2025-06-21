/* package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserStorage userStorage;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddUserSuccessfully() throws ValidationException {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        when(userStorage.addUser(any(User.class))).thenReturn(user);

        User addedUser = userService.addUser(user);
        assertNotNull(addedUser);
        assertEquals("user@example.com", addedUser.getEmail());
        verify(userStorage, times(1)).addUser(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }
}

*/