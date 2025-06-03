package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
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


    private UserDbStorage userDbStorage;

    public UserDbStorageTest(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @Test
    public void testAddUser() {
        User user = new User("user" + System.currentTimeMillis() + "@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isGreaterThan(0);
        assertThat(addedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindUserById() {
        User user = new User("user" + System.currentTimeMillis() + "@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);

        User foundUser = userDbStorage.getUserById(addedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("user" + System.currentTimeMillis() + "@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);
        userDbStorage.deleteUser(addedUser.getId());

        User deletedUser = userDbStorage.getUserById(addedUser.getId());
        assertThat(deletedUser).isNull();
    }
}
