package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
 * User.
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
}


