package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final FilmValidator filmValidator;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid  @RequestBody Film film) {
        filmValidator.validate(film);

        film.setId(getNextId());
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        filmValidator.validateId(updatedFilm);
        // Проверяем, существует ли фильм с таким ID
        if (!films.containsKey(updatedFilm.getId())) {
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден");
        }

        // Выполняем валидацию нового объекта
        filmValidator.validate(updatedFilm);

        // Получаем существующий фильм
        Film existingFilm = films.get(updatedFilm.getId());

        // Обновляем данные фильма
        existingFilm.setName(updatedFilm.getName());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseDate(updatedFilm.getReleaseDate());
        existingFilm.setDuration(updatedFilm.getDuration());

        // Сохраняем обновлённый фильм
        films.put(updatedFilm.getId(), existingFilm);

        return existingFilm;
    }


    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}

