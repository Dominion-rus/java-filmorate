package ru.yandex.practicum.filmorate.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingRepository;

import java.time.LocalDate;

@Component
public class FilmValidator {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final MpaRatingRepository mpaRatingRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public FilmValidator(MpaRatingRepository mpaRatingRepository, GenreRepository genreRepository) {
        this.mpaRatingRepository = mpaRatingRepository;
        this.genreRepository = genreRepository;
    }

    public void validate(Film film) {

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getMpaRating() == null || !mpaRatingRepository.existsById(film.getMpaRating().getId())) {
            throw new ValidateException("MPA рейтинг с ID " +
                    (film.getMpaRating() != null ? film.getMpaRating().getId() : null) + " не найден");
        }

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!genreRepository.existsById(genre.getId())) {
                    throw new ValidateException("Жанр с ID " + genre.getId() + " не найден");
                }
            }
        }
    }

    public void validateId(Film film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("ID фильма должен быть указан");
        }
    }

}

