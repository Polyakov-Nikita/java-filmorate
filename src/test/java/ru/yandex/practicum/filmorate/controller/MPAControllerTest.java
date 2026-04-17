package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
public class MPAControllerTest extends ControllerTest {
    @Test
    public void getAll_StatusCode() {
        expectStatusIsOk(performGetAll(MPAController.URL_BASE));
    }

    @Test
    public void get_StatusCode() {
        expectStatusIsOk(performGet(MPAController.URL_BASE, 1L));
    }

    @Test
    public void get_ReturnsObject() {
        String expectedJson = """
                {
                "id": 1,
                "name": "G"
                }""";
        expectContentJSON(performGet(MPAController.URL_BASE, 1L), expectedJson);
    }
}
