package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.controller.handler.FilmHandler;

@AutoConfigureMockMvc
public class FilmControllerTest extends ControllerTest {
    private static final String BASE_PATH = "films/";
    private static final String MODEL_TYPE = "film";
    private static final String CORRECT_ADD_REQUEST = getContentAddRequest(BASE_PATH, "correct", MODEL_TYPE);
    private static final String CORRECT_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "correct", MODEL_TYPE);
    private static final String FAIL_NAME_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-name", MODEL_TYPE);
    private static final String FAIL_DESCRIPTION_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-description", MODEL_TYPE);
    private static final String FAIL_RELEASE_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-release", MODEL_TYPE);
    private static final String FAIL_DURATION_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-duration", MODEL_TYPE);
    private static final String CORRECT_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "correct", MODEL_TYPE);
    private static final String CORRECT_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "correct", MODEL_TYPE);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmHandler handler;

    @Test
    public void add_CorrectData_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void add_CorrectData_ReturnsObject() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.content().json(CORRECT_ADD_RESPONSE));
    }

    @Test
    public void add_CorrectData_NonNullId() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void add_FailName_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, FAIL_NAME_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void add_FailDescription_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, FAIL_DESCRIPTION_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void add_FailRelease_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, FAIL_RELEASE_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void add_FailDuration_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(FilmController.URL, FAIL_DURATION_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_CorrectData_StatusCode() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(FilmController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        mockMvc.perform(createPutBuilder(FilmController.URL, body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_CorrectData_ReturnsObject() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(FilmController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        mockMvc.perform(createPutBuilder(FilmController.URL, body))
                .andExpect(MockMvcResultMatchers.content().json(CORRECT_UPDATE_RESPONSE));
    }

    @Test
    public void update_Unknown_StatusCode() throws Exception {
        String body = addId(CORRECT_UPDATE_REQUEST, ABSENT_ID);
        mockMvc.perform(createPutBuilder(FilmController.URL, body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAll_StatusCode() throws Exception {
        mockMvc.perform(createGetBuilder(FilmController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
