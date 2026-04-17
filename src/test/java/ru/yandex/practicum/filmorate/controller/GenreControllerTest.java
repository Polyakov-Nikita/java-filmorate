package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
public class GenreControllerTest extends ControllerTest {
    @Test
    public void getAll_StatusCode() {
        expectStatusIsOk(performGetAll(GenreController.URL_BASE));
    }

    @Test
    public void get_StatusCode() {
        expectStatusIsOk(performGet(GenreController.URL_BASE, 1L));
    }

    @Test
    public void get_ReturnsObject() {
        String expectedJson = "{\"id\": 1, \"name\": \"Комедия\"}";
        expectContentJSON(performGet(GenreController.URL_BASE, 1L), expectedJson);
    }
}
