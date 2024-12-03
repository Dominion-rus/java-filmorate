package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validate(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidateException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidateException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateException("Продолжительность фильма должна быть положительным числом");
        }
    }
}

