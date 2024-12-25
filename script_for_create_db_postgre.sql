CREATE TABLE "users" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "email" varchar,
  "login" varchar,
  "birthday" date
);

CREATE TABLE "films" (
  "id" integer PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" date,
  "duration" integer,
  "rating" varchar
);

CREATE TABLE "genres" (
  "id" integer PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "film_genres" (
  "film_id" integer,
  "genre_id" integer
);

CREATE TABLE "friends" (
  "user_id" integer,
  "friend_id" integer,
  "status" varchar
);

CREATE TABLE "film_likes" (
  "user_id" integer,
  "film_id" integer
);

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");
