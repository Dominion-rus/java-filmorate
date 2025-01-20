-- Жанры
INSERT INTO genres (name)
SELECT 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия')
UNION ALL
SELECT 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма')
UNION ALL
SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм')
UNION ALL
SELECT 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер')
UNION ALL
SELECT 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный')
UNION ALL
SELECT 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');

-- MPA-рейтинг
INSERT INTO mpa_ratings (rating)
SELECT 'G' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'G')
UNION ALL
SELECT 'PG' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'PG')
UNION ALL
SELECT 'PG-13' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'PG-13')
UNION ALL
SELECT 'R' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'R')
UNION ALL
SELECT 'NC-17' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'NC-17');

-- Фильмы
INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
SELECT 'Фильм 1', 'Описание фильма 1', '2023-01-01', 120, (SELECT id FROM mpa_ratings WHERE rating = 'PG')
WHERE NOT EXISTS (SELECT 1 FROM films WHERE name = 'Фильм 1')
UNION ALL
SELECT 'Фильм 2', 'Описание фильма 2', '2023-02-01', 90, (SELECT id FROM mpa_ratings WHERE rating = 'R')
WHERE NOT EXISTS (SELECT 1 FROM films WHERE name = 'Фильм 2');

-- Жанры для фильмов
-- Для связки фильмов и жанров
INSERT INTO film_genres (film_id, genre_id)
SELECT f.film_id, g.id
FROM films f, genres g
WHERE f.name = 'Фильм 1' AND g.name = 'Комедия'
AND NOT EXISTS (SELECT 1 FROM film_genres WHERE film_id = f.film_id AND genre_id = g.id)
UNION ALL
SELECT f.film_id, g.id
FROM films f, genres g
WHERE f.name = 'Фильм 2' AND g.name = 'Драма'
AND NOT EXISTS (SELECT 1 FROM film_genres WHERE film_id = f.film_id AND genre_id = g.id);

-- Пользователи
INSERT INTO users (email, login, name, birthday)
SELECT 'user1@example.com', 'user1', 'User One', '1990-01-01'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user1@example.com')
UNION ALL
SELECT 'user2@example.com', 'user2', 'User Two', '1995-05-15'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user2@example.com');

-- Лайки фильмов
INSERT INTO film_likes (film_id, user_id)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM film_likes WHERE film_id = 1 AND user_id = 1)
UNION ALL
SELECT 2, 2 WHERE NOT EXISTS (SELECT 1 FROM film_likes WHERE film_id = 2 AND user_id = 2);

-- Дружба
INSERT INTO friendships (user_id, friend_id, status)
SELECT 1, 2, TRUE WHERE NOT EXISTS (SELECT 1 FROM friendships WHERE user_id = 1 AND friend_id = 2);
