CREATE TABLE "users" (
  "id" long PRIMARY KEY,
  "name" varchar,
  "email" varchar,
  "login" varchar,
  "birthday" date
);

CREATE TABLE "films" (
  "id" long PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" date,
  "duration" integer,
  "rating" varchar
);

CREATE TABLE "genres" (
  "id" long PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "film_genres" (
  "film_id" long,
  "genre_id" long
);

CREATE TABLE "friends" (
  "user_id" long,
  "friend_id" long,
  "status" varchar
);

CREATE TABLE "film_likes" (
  "user_id" long,
  "film_id" long
);

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");
