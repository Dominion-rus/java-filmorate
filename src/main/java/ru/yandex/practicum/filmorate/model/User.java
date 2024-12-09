package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "email не может быть пустым")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}


