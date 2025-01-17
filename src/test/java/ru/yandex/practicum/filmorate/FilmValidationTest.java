package ru.yandex.practicum.filmorate;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingRepository;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class FilmValidationTest {

    @Autowired
    private MpaRatingRepository mpaRatingRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private FilmValidator filmValidator;

    @BeforeEach
    void setUp() {

        mpaRatingRepository.save(new MpaRating(1L, "G"));
        mpaRatingRepository.save(new MpaRating(2L, "PG"));
        genreRepository.save(new Genre(1L, "Comedy"));
        genreRepository.save(new Genre(2L, "Drama"));
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
    void shouldThrowExceptionWhenMpaRatingDoesNotExist() {
        Film film = Film.builder()
                .name("Test Film")
                .description("A great movie")
                .releaseDate(LocalDate.of(1995, 5, 15))
                .duration(120)
                .mpaRating(new MpaRating(10L, "Unknown"))
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }

    @Test
    void shouldThrowExceptionWhenGenreDoesNotExist() {
        Film film = Film.builder()
                .name("Test Film")
                .description("A great movie")
                .releaseDate(LocalDate.of(1995, 5, 15))
                .duration(120)
                .genres(Set.of(new Genre(100L, "Nonexistent Genre")))
                .build();

        assertThrows(ValidateException.class, () -> filmValidator.validate(film));
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        Film film = Film.builder()
                .name("Valid Film")
                .description("A valid movie")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(150)
                .mpaRating(new MpaRating(1L, "G"))
                .genres(Set.of(new Genre(1L, "Comedy"), new Genre(2L, "Drama")))
                .build();

        assertDoesNotThrow(() -> filmValidator.validate(film));
    }
}


