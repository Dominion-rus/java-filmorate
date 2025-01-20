package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final FilmRepository filmRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Film> findAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    @Transactional
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    @Transactional
    public Film updateFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Film> findById(Long id) {
        return filmRepository.findById(id);
    }
}
