-- Таблица для жанров
CREATE TABLE IF NOT EXISTS genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Таблица для MPA рейтингов
CREATE TABLE IF NOT EXISTS mpa_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating VARCHAR(10) NOT NULL
);

-- Таблица для фильмов
CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Используем film_id вместо id
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_rating_id BIGINT NOT NULL,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings(id)
);

-- Таблица для связи фильмов с жанрами (многие ко многим)
CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Таблица для пользователей
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Используем user_id вместо id
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

-- Таблица для лайков фильмов
CREATE TABLE IF NOT EXISTS film_likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Таблица для дружбы пользователей
CREATE TABLE IF NOT EXISTS friendships (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        friend_id BIGINT NOT NULL,
        status BOOLEAN NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(user_id),
        FOREIGN KEY (friend_id) REFERENCES users(user_id),
        CONSTRAINT friendship_unique UNIQUE (user_id, friend_id)
);
