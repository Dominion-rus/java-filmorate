package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String invalidUserJson = "    {\n" +
                                 "        \"name\": \"Vladimir\",\n" +
                                 "        \"email\": \"\",\n" +
                                 "        \"login\": \"testLogin\",\n" +
                                 "        \"birthday\": \"1992-10-13\"\n" +
                                 "    }\n";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginHasSpaces() throws Exception {
        String invalidUserJson = "    {\n" +
                                 "        \"name\": \"Vladimir\",\n" +
                                 "        \"email\": \"test@test.ru\",\n" +
                                 "        \"login\": \"test Login\",\n" +
                                 "        \"birthday\": \"1992-10-13\"\n" +
                                 "    }\n";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkWhenValidUserIsProvided() throws Exception {
        String validUserJson = "    {\n" +
                               "               \"login\": \"dolore\",\n" +
                               "               \"name\": \"Nick Name\",\n" +
                               "               \"email\": \"mail@mail.ru\",\n" +
                               "               \"birthday\": \"1946-08-20\"\n" +
                               "             }\n";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated());
    }
}

