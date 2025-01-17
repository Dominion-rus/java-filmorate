package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    public Collection<Genre> getAllGenres() {
        return genreRepository.findAll();
    }


    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с ID " + id + " не найден"));
    }
}
