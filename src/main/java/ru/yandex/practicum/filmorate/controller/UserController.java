package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        if (user == null) {
            throw new ConditionsNotMetException("Пользователь не может быть null");
        }
        userValidator.validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        if (updatedUser.getId() == null) {
            throw new ConditionsNotMetException("ID пользователя должен быть указан");
        }

        if (!users.containsKey(updatedUser.getId())) {
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден");
        }

        User existingUser = users.get(updatedUser.getId());
        userValidator.validate(updatedUser);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setBirthday(updatedUser.getBirthday());
        users.put(updatedUser.getId(), existingUser);

        return existingUser;
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
