package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        validateUser(user);
        User addedUser = userStorage.addUser(user);
        log.info("Пользователь добавлен: {}", addedUser);
        return addedUser;
    }

    public User getUserById(long id) throws ValidationException {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.error("Пользователь с ID {} не найден.", id);
            throw new ValidationException("Пользователь с ID " + id + " не найден.");
        }
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        validateUser(user);
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {log.error("Пользователь с ID {} не найден для обновления.", user.getId());
            throw new ValidationException("Пользователь с ID " + user.getId() + " не найден.");
        }
        log.info("Пользователь обновлен: {}", updatedUser);
        return updatedUser;
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
        log.info("Пользователь с ID {} удален.", id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> getFriends(Long userId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ValidationException("Пользователь с ID " + userId + " не найден.");
        }
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        if (user == null || otherUser == null) {
            throw new ValidationException("Один из пользователей не найден.");
        }
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : commonFriendIds) {
            User commonFriend = userStorage.getUserById(friendId);
            if (commonFriend != null) {
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }

    public List<User> addFriend(Long userId, Long friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("Один из пользователей не найден.");
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
        return getFriends(userId);
    }

    public void removeFriend(Long userId, Long friendId) throws ValidationException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("Один из пользователей не найден.");
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
    }
}