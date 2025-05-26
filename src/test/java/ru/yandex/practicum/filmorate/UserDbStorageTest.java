package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(UserDbStorage.class)
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @BeforeEach
    public void setUp() {
        userDbStorage.addUser(new User("user@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1)));
    }

    @Test
    public void testFindUserById() {
        User userToAdd = new User("user@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(userToAdd);

        User foundUser = userDbStorage.getUserById(addedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    public void testUpdateUser() {
        User user = userDbStorage.getUserById(1);
        user.setName("Updated Name");
        userDbStorage.updateUser(user);

        User updatedUser = userDbStorage.getUserById(1);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDeleteUser() {
        User user = userDbStorage.getUserById(1);
        userDbStorage.deleteUser(user.getId());

        User deletedUser = userDbStorage.getUserById(user.getId());
        assertThat(deletedUser).isNull();
    }
}