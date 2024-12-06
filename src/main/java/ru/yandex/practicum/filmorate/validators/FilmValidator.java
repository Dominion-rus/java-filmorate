package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validate(Film film) {

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    public void validateId(Film film) {
        // Проверяем, передан ли ID
        if (film.getId() == null) {
            throw new ConditionsNotMetException("ID фильма должен быть указан");
        }
    }
}

