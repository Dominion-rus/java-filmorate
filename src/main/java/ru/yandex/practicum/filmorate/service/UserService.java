package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendships.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userStorage = userStorage;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidateException("Нельзя добавить самого себя в друзья.");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + friendId + " не найден"));

        Optional<Friendship> existingFriendship = friendshipRepository.findByUserIdAndFriendId(userId, friendId);
        if (existingFriendship.isPresent()) {
            throw new ValidateException("Заявка в друзья уже отправлена.");
        }

        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(false);
        friendshipRepository.save(friendship);
    }

    public void removeFriend(Long userId, Long friendId) {
        boolean isUserExists = userRepository.existsById(userId);
        boolean isFriendExists = userRepository.existsById(friendId);

        if (!isUserExists || !isFriendExists) {
            String message = "Пользователь с ID " + (isUserExists ? friendId : userId) + " не найден.";
            throw new NotFoundException(message);
        }

        Optional<Friendship> existingFriendship = friendshipRepository.findByUserIdAndFriendId(userId, friendId);

        if (existingFriendship.isPresent()) {
            friendshipRepository.delete(existingFriendship.get());
        }

    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<Long> userFriends = friendshipRepository.findFriendsByUserId(userId);
        Set<Long> otherUserFriends = friendshipRepository.findFriendsByUserId(otherUserId);

        userFriends.retainAll(otherUserFriends);

        return userFriends.stream()
                .map(friendId -> userRepository.findById(friendId)
                        .orElseThrow(() -> new NotFoundException("Пользователь с ID " + friendId + " не найден")))
                .collect(Collectors.toList());
    }




    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
    }

    public Collection<User> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Collection<User> friends = friendshipRepository.findFriendsByUserId(userId).stream()
                .map(friendId -> userRepository.findById(friendId)
                        .orElseThrow(() -> new NotFoundException("Пользователь с ID " + friendId + " не найден")))
                .collect(Collectors.toList());

        return friends;
    }




    public User updateUser(User updatedUser) {
        User existingUser = userStorage.findById(updatedUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setBirthday(updatedUser.getBirthday());
        existingUser.setFriends(updatedUser.getFriends());

        return userStorage.updateUser(existingUser);
    }
}
