package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class UserValidator {

    public void validate(User user) {

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidateException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validateId(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("ID пользователя должен быть указан");
        }
    }
}
