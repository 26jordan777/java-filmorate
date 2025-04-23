package ru.yandex.practicum.filmorate;

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

    @Test
    public void testAddUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userDbStorage.addUser(user);

        List<User> users = userDbStorage.getAllUsers();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("user@example.com");
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setLogin("existingLogin");
        user.setName("Existing Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userDbStorage.addUser(user);
        User foundUser = userDbStorage.getUserById(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("existing@example.com");
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("update@example.com");
        user.setLogin("updateLogin");
        user.setName("Update Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userDbStorage.addUser(user);

        user.setName("Updated Name");
        userDbStorage.updateUser(user);

        User updatedUser = userDbStorage.getUserById(user.getId());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }
}