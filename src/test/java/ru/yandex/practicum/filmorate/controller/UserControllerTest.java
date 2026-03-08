package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.controller.handler.UserHandler;

@AutoConfigureMockMvc
public class UserControllerTest extends ControllerTest {
    private static final String BASE_PATH = "users/";
    private static final String MODEL_TYPE = "user";
    private static final String CORRECT_ADD_REQUEST = getContentAddRequest(BASE_PATH, "correct", MODEL_TYPE);
    private static final String CORRECT_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "correct", MODEL_TYPE);
    private static final String FAIL_LOGIN_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-login", MODEL_TYPE);
    private static final String FAIL_EMAIL_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-email", MODEL_TYPE);
    private static final String FAIL_BIRTHDAY_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-birthday", MODEL_TYPE);
    private static final String CORRECT_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "correct", MODEL_TYPE);
    private static final String CORRECT_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "correct", MODEL_TYPE);
    private static final String EMPTY_NAME_ADD_REQUEST = getContentAddRequest(BASE_PATH, "empty-name", MODEL_TYPE);
    private static final String EMPTY_NAME_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "empty-name", MODEL_TYPE);
    private static final String EMPTY_NAME_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "empty-name", MODEL_TYPE);
    private static final String EMPTY_NAME_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "empty-name", MODEL_TYPE);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserHandler handler;

    @Test
    public void add_CorrectData_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void add_CorrectData_ReturnsObject() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.content().json(CORRECT_ADD_RESPONSE));
    }

    @Test
    public void add_CorrectData_NonNullId() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void add_FailLogin_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, FAIL_LOGIN_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void add_FailEmail_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, FAIL_EMAIL_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void add_FailBirthday_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, FAIL_BIRTHDAY_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_CorrectData_StatusCode() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        mockMvc.perform(createPutBuilder(UserController.URL, body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_CorrectData_ReturnsObject() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        mockMvc.perform(createPutBuilder(UserController.URL, body))
                .andExpect(MockMvcResultMatchers.content().json(CORRECT_UPDATE_RESPONSE));
    }

    @Test
    public void update_Unknown_StatusCode() throws Exception {
        String body = addId(CORRECT_UPDATE_REQUEST, ABSENT_ID);
        mockMvc.perform(createPutBuilder(UserController.URL, body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAll_StatusCode() throws Exception {
        mockMvc.perform(createGetBuilder(UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void add_EmptyName_StatusCode() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, EMPTY_NAME_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void add_EmptyName_ReturnsObject() throws Exception {
        mockMvc.perform(createPostBuilder(UserController.URL, EMPTY_NAME_ADD_REQUEST))
                .andExpect(MockMvcResultMatchers.content().json(EMPTY_NAME_ADD_RESPONSE));
    }

    @Test
    public void update_EmptyName_StatusCode() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(EMPTY_NAME_UPDATE_REQUEST, id);
        mockMvc.perform(createPutBuilder(UserController.URL, body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_EmptyName_ReturnsObject() throws Exception {
        Long id = getId(mockMvc.perform(createPostBuilder(UserController.URL, CORRECT_ADD_REQUEST)));
        String body = addId(EMPTY_NAME_ADD_REQUEST, id);
        mockMvc.perform(createPutBuilder(UserController.URL, body))
                .andExpect(MockMvcResultMatchers.content().json(EMPTY_NAME_UPDATE_RESPONSE));
    }
}
