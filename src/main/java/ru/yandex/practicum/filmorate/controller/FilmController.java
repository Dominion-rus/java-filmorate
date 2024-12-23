package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private static final String TOP_DEFAULT_VALUE = "10";
    private final FilmStorage filmStorage;
    private final FilmValidator filmValidator;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid  @RequestBody Film film) {
        filmValidator.validate(film);

        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        filmValidator.validateId(updatedFilm);
        filmValidator.validate(updatedFilm);
        return filmStorage.updateFilm(updatedFilm);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(defaultValue = TOP_DEFAULT_VALUE) int count) {
        return filmService.getMostPopularFilms(count);
    }


}

