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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(UserDbStorage.class)
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @BeforeEach
    public void setUp() {
        List<User> users = userDbStorage.getAllUsers();
        for (User user : users) {
            userDbStorage.deleteUser(user.getId());
        }
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userDbStorage.addUser(user);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setLogin("newLogin");
        user.setName("New User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        userDbStorage.addUser(user);

        List<User> users = userDbStorage.getAllUsers();
        assertThat(users).hasSize(2);
        assertThat(users.get(1).getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    public void testFindUserById() {
        User foundUser = userDbStorage.getUserById(1);
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
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userDbStorage.deleteUser(user.getId());

    }
}