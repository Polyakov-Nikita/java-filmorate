package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.TestUtility;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    private static final Film FILM_CORRECT = Film.builder()
            .name("Title")
            .description("Description")
            .releaseDate(LocalDate.of(2023, 1, 1))
            .duration(120)
            .build();
    private static final Film FILM_INCORRECT = Film.builder().build();
    private static final Film FILM_UPDATE = Film.builder()
            .name("NewTitle")
            .description("NewDescription")
            .releaseDate(FILM_CORRECT.getReleaseDate())
            .duration(FILM_CORRECT.getDuration() + 10)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void add_CorrectData_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void add_CorrectData_ReturnsObject() throws Exception {
        String description = "Сервер должен вернуть добавленный объект";
        Film received = receive(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL));
        Assertions.assertEquals(FILM_CORRECT, received, description);
    }

    private Film receive(MockHttpServletRequestBuilder builder) throws  Exception {
        String content = mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return TestUtility.parseObject(content, Film.class);
    }

    @Test
    public void add_CorrectData_NonNullId() throws Exception {
        String description = "У полученного объекта должно быть проинициализировано поле id";
        Film film = receive(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL));
        Assertions.assertNotNull(film.getId(), description);
    }

    @Test
    public void add_IncorrectData_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPostBuilder(FILM_INCORRECT, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_CorrectData_StatusCode() throws Exception {
        Film sent = receive(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL));
        Film update = FILM_UPDATE.toBuilder()
                .id(sent.getId())
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_CorrectData_ReturnsObject() throws Exception {
        String description = "Сервер должен вернуть обновлённый объект";
        Film sent = receive(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL));
        Film update = FILM_UPDATE.toBuilder()
                .id(sent.getId())
                .build();
        Film received = receive(TestUtility.createPutBuilder(update, FilmController.URL));
        Assertions.assertEquals(update, received, description);
    }

    @Test
    public void update_IncorrectData_StatusCode() throws Exception {
        Film sent = receive(TestUtility.createPostBuilder(FILM_CORRECT, FilmController.URL));
        Film update = FILM_INCORRECT.toBuilder()
                .id(sent.getId())
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_NullId_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPutBuilder(FILM_UPDATE, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_AbsentId_StatusCode() throws Exception {
        Film update = FILM_CORRECT.toBuilder()
                .id(999999999999L)
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAll_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createGetBuilder(FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAll_ReturnsArray() throws Exception {
        String description = "Сервер должен вернуть список объектов";
        List<Film> films = TestUtility.receiveObjects(mockMvc, FilmController.URL);
        Assertions.assertNotNull(films, description);
    }
}
