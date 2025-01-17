package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final FilmRepository filmRepository;

    public FilmDbStorage(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return filmRepository.findById(id);
    }
}
