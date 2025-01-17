package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
