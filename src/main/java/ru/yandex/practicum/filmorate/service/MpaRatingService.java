package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingRepository;

import java.util.Collection;

@Service
public class MpaRatingService {
    private final MpaRatingRepository mpaRatingRepository;

    @Autowired
    public MpaRatingService(MpaRatingRepository mpaRatingRepository) {
        this.mpaRatingRepository = mpaRatingRepository;
    }


    public Collection<MpaRating> getAllMpaRatings() {
        return mpaRatingRepository.findAll();
    }


    public MpaRating getMpaRatingById(Long id) {
        return mpaRatingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с ID " + id + " не найден"));
    }
}
