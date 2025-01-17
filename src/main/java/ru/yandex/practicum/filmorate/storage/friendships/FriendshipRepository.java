package ru.yandex.practicum.filmorate.storage.friendships;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);

    @Query("SELECT f.friendId FROM Friendship f WHERE f.userId = :userId")

    Set<Long> findFriendsByUserId(@Param("userId") Long userId);
}
