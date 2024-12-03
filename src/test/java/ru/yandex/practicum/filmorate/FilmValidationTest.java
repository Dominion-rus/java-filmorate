package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTest {

    private final FilmValidator filmValidator = new FilmValidator();

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Film film = Film.builder()
                .name("")
                .description("A great movie")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooLong() {
        Film film = Film.builder()
                .name("Test Film")
                .description("A".repeat(201))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsTooEarly() {
        Film film = Film.builder()
                .name("Test Film")
                .description("A great movie")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(120)
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = Film.builder()
                .name("Test Film")
                .description("A great movie")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-1)
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }
}

