package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendships.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
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

        List<User> commonFriends = userRepository.findAllById(userFriends);

        if (commonFriends.size() != userFriends.size()) {
            Set<Long> foundIds = commonFriends.stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            userFriends.stream()
                    .filter(id -> !foundIds.contains(id))
                    .forEach(id -> {
                        throw new NotFoundException("Пользователь с ID " + id + " не найден");
                    });
        }

        return commonFriends;
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




    @Transactional
    public User updateUser(User updatedUser) {
        Optional<User> userWithSameEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(updatedUser.getId())) {
            throw new ValidateException("Email уже используется другим пользователем");
        }

        Optional<User> userWithSameLogin = userRepository.findByLogin(updatedUser.getLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getId().equals(updatedUser.getId())) {
            throw new ValidateException("Логин уже используется другим пользователем");
        }

        try {
            User existingUser = userStorage.findById(updatedUser.getId())
                    .orElseThrow(() -> new NotFoundException("Пользователь с ID " +
                            updatedUser.getId() + " не найден"));

            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setLogin(updatedUser.getLogin());
            existingUser.setBirthday(updatedUser.getBirthday());
            existingUser.setFriends(updatedUser.getFriends());

            return userStorage.updateUser(existingUser);
        } catch (DataIntegrityViolationException e) {
            throw new ValidateException("Поля email или login должны быть уникальными");
        }
    }
}
