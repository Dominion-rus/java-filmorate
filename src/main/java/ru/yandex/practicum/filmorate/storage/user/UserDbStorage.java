package ru.yandex.practicum.filmorate.storage.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;


@Component("userDbStorage")
public class UserDbStorage implements UserStorage {


    private final UserRepository userRepository;

    public UserDbStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);

    }

    @Override
    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }


}
