package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturn201WhenFilmIsValid() throws Exception {
        String validFilmJson = "    { \"name\": \"Inception\",\n" +
                               "      \"description\": \"A mind-bending thriller\",\n" +
                               "      \"releaseDate\": \"2010-07-16\",\n" +
                               "      \"duration\": 148\n" +
                               "    }\n";

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFilmJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        String invalidFilmJson = "{ \"name\": \"\",\n" +
                                 "  \"description\": \"A mind-bending thriller\",\n" +
                                 "  \"releaseDate\": \"2010-07-16\",\n" +
                                 "  \"duration\": 148\n" +
                                 "}";

        mockMvc.perform(post("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200AndListOfFilms() throws Exception {
        mockMvc.perform(get("/api/v1/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}

