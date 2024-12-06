package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    void shouldThrowExceptionWhenLoginContainsSpaces() {
        User user = User.builder()
                .name("Vladimir")
                .email("test@test.ru")
                .login("test Login")
                .birthday(LocalDate.of(1992, 10, 13))
                .build();

        assertThrows(ValidateException.class, () -> userValidator.validate(user));
    }

    @Test
    void shouldThrowExceptionWhenBirthdayIsInFuture() {
        User user = User.builder()
                .name("Vladimir")
                .email("test@test.ru")
                .login("testLogin")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ValidateException.class, () -> userValidator.validate(user));
    }

    @Test
    void shouldSetNameToLoginWhenNameIsEmpty() {
        User user = User.builder()
                .name("")
                .email("test@test.ru")
                .login("testLogin")
                .birthday(LocalDate.of(1992, 10, 13))
                .build();

        userValidator.validate(user);

        assert user.getName().equals("testLogin");
    }

}

