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
        User user = new User("user" + System.currentTimeMillis() + "@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isGreaterThan(0);
        assertThat(addedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("user@example.com", "validLogin", "Valid Name", LocalDate.of(2000, 1, 1));
        User addedUser = userDbStorage.addUser(user);

        addedUser.setName("Updated Name");
        User updatedUser = userDbStorage.updateUser(addedUser);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
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

    @Test
    public void testAddFriend() {
        User user1 = userDbStorage.addUser(new User("user1@example.com", "login1", "User 1", LocalDate.of(2000, 1, 1)));
        User user2 = userDbStorage.addUser(new User("user2@example.com", "login2", "User 2", LocalDate.of(2000, 1, 1)));

        userDbStorage.addFriend(user1.getId(), user2.getId());

        List<User> friends = userDbStorage.getFriends(user1.getId());
        assertThat(friends).hasSize(1);
        assertThat(friends.getFirst().getId()).isEqualTo(user2.getId());
    }
}

