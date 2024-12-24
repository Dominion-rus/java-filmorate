package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {

        if (userId.equals(friendId)) {
            throw new ValidateException("Нельзя добавить самого себя в друзья");
        }

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        User friend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + friendId + " не найден"));

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        User friend = userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + friendId + " не найден"));

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public Set<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        User otherUser = userStorage.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + otherUserId + " не найден"));

        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());

        Set<User> commonFriends = new HashSet<>();
        for (Long friendId : commonFriendIds) {
            commonFriends.add(userStorage.findById(friendId).orElseThrow());
        }
        return commonFriends;
    }

    public User getUserById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
    }

    public Collection<User> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friendIds = user.getFriends();
        return userStorage.findAllUsers().stream()
                .filter(u -> friendIds.contains(u.getId()))
                .collect(Collectors.toList());
    }
}
