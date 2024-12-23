package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long userId, Long filmId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));

        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        if (film.getLikes().contains(userId)) {
            throw new ValidateException("Пользователь уже поставил лайк этому фильму");
        }

        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(Long userId, Long filmId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь не ставил лайк этому фильму или " +
                    "такого пользователя не существует");
        }

        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getMostPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .map(film -> {
                    if (film.getLikes() == null) {
                        film.setLikes(new HashSet<>());
                    }
                    return film;
                })
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }


    public Film getFilmById(Long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + id + " не найден"));
    }
}
