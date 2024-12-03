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
@RequestMapping("/api/v1/users")
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

    @PutMapping("/{id}")
    public User updateUser(@PathVariable long id,
                           @RequestBody User updatedUser) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }

        User existingUser = users.get(id);
        userValidator.validate(updatedUser);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLogin(updatedUser.getLogin());
        existingUser.setBirthday(updatedUser.getBirthday());
        users.put(id, existingUser);

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
