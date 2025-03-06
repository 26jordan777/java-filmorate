package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        User addedUser = userStorage.addUser(user);
        log.info("Пользователь добавлен: {}", addedUser);
        return addedUser;
    }

    public User updateUser(User user) {
        User updatedUser = userStorage.updateUser(user);
        log.info("Пользователь обновлен: {}", updatedUser);
        return updatedUser;
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.error("Пользователь с ID {} не найден.", id);
        }
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.addFriend(friendId);
            friend.addFriend(userId);
            log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", userId, friendId);
        } else {
            log.error("Не удалось добавить в друзья: один из пользователей не найден.");
        }
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.removeFriend(friendId);
            friend.removeFriend(userId);
            log.info("Пользователь с ID {} убрал из друзей пользователя с ID {}", userId, friendId);
        } else {
            log.error("Не удалось убрать из друзей: один из пользователей не найден.");
        }
    }

    public Collection<User> getFriends(long userId) {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            return user.getFriends().stream()
                    .map(userStorage::getUserById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        log.warn("Пользователь с ID {} не найден, не удается получить список друзей.", userId);
        return Collections.emptyList();
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        if (user != null && otherUser != null) {
            return user.getFriends().stream()
                    .filter(otherUser.getFriends()::contains)
                    .map(userStorage::getUserById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        log.warn("Не удалось получить общих друзей: один из пользователей не найден.");
        return Collections.emptyList();
    }
}