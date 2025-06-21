package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User addUser(User user) throws ValidationException {
        validateUser(user);
        User addedUser = userDbStorage.addUser(user);
        log.info("Пользователь добавлен: {}", addedUser);
        return addedUser;
    }

    public User getUserById(long id) {
        User user = userDbStorage.getUserById(id);
        if (user == null) {
            log.error("Пользователь с ID {} не найден.", id);
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден.");
        }
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        validateUser(user);

        User existingUser = userDbStorage.getUserById(user.getId());
        if (existingUser == null) {
            log.error("Пользователь с ID {} не найден для обновления.", user.getId());
            throw new ResourceNotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }

        User updatedUser = userDbStorage.updateUser(user);
        log.info("Пользователь обновлен: {}", updatedUser);
        return updatedUser;
    }

    public void deleteUser(long id) {
        userDbStorage.deleteUser(id);
        log.info("Пользователь с ID {} удален.", id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userDbStorage.getAllUsers());
    }

    public List<User> getFriends(Long userId) throws ValidationException {
        User user = userDbStorage.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            User friend = userDbStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getFriendsCommonOther(Long userId, Long otherId) throws ValidationException {
        User user = userDbStorage.getUserById(userId);
        User otherUser = userDbStorage.getUserById(otherId);
        if (user == null || otherUser == null) {
            throw new ResourceNotFoundException("Один из пользователей не найден.");
        }
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : commonFriendIds) {
            User commonFriend = userDbStorage.getUserById(friendId);
            if (commonFriend != null) {
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }


    public void removeFriend(Long userId, Long friendId) {
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);

        if (user == null || friend == null) {
            throw new ResourceNotFoundException("Один из пользователей не найден.");
        }

        user.removeFriend(friendId);
        friend.removeFriend(userId);
        userDbStorage.updateUser(user);
        userDbStorage.updateUser(friend);
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Дата рождения не может быть пустой.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже текущей даты.");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);

        if (user == null) {
            throw new ResourceNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (friend == null) {
            throw new ResourceNotFoundException("Пользователь с ID " + friendId + " не найден.");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);

        userDbStorage.updateUser(user);
        userDbStorage.updateUser(friend);
    }
}