package ru.yandex.practicum.filmorate.storage.mpaRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Optional;

@Repository
public interface MpaRatingRepository extends JpaRepository<MpaRating, Long> {
    Optional<MpaRating> findById(Long id);
}
