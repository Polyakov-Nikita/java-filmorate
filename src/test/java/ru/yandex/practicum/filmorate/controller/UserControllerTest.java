package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.TestUtility;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final User USER_CORRECT = User.builder()
            .email("email@yandex.ru")
            .login("login")
            .name("name")
            .birthday(LocalDate.of(2023, 1, 1))
            .build();
    private static final User USER_INCORRECT = User.builder().build();
    private static final User USER_UPDATE = User.builder()
            .email("newemail@yandex.ru")
            .login("newLogin")
            .name("newName")
            .birthday(USER_CORRECT.getBirthday())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void add_CorrectData_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void add_CorrectData_ReturnsObject() throws Exception {
        String description = "Сервер должен вернуть добавленный объект";
        User received = receive(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL));
        Assertions.assertEquals(USER_CORRECT, received, description);
    }

    private User receive(MockHttpServletRequestBuilder builder) throws Exception {
        String content = mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return TestUtility.parseObject(content, User.class);
    }

    @Test
    public void add_CorrectData_NonNullId() throws Exception {
        String description = "У полученного объекта должно быть проинициализировано поле id";
        User user = receive(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL));
        Assertions.assertNotNull(user.getId(), description);
    }

    @Test
    public void add_EmptyName_SetLoginAsName() throws Exception {
        String description = "У полученного объекта поле имя должно быть равно отправленному логину";
        User sent = USER_CORRECT.toBuilder()
                .name("")
                .build();
        User received = receive(TestUtility.createPostBuilder(sent, UserController.URL));
        Assertions.assertEquals(sent.getLogin(), received.getName(), description);
    }

    @Test
    public void add_IncorrectData_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPostBuilder(USER_INCORRECT, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_CorrectData_StatusCode() throws Exception {
        User sent = receive(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL));
        User update = USER_UPDATE.toBuilder()
                .id(sent.getId())
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void update_CorrectData_ReturnsObject() throws Exception {
        String description = "Сервер должен вернуть обновлённый объект";
        User sent = receive(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL));
        User update = USER_UPDATE.toBuilder()
                .id(sent.getId())
                .build();
        User received = receive(TestUtility.createPutBuilder(update, UserController.URL));
        Assertions.assertEquals(update, received, description);
    }

    @Test
    public void update_IncorrectData_StatusCode() throws Exception {
        User sent = receive(TestUtility.createPostBuilder(USER_CORRECT, UserController.URL));
        User update = USER_INCORRECT.toBuilder()
                .id(sent.getId())
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_NullId_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createPutBuilder(USER_UPDATE, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update_AbsentId_StatusCode() throws Exception {
        User update = USER_CORRECT.toBuilder()
                .id(999999999999L)
                .build();
        mockMvc.perform(TestUtility.createPutBuilder(update, UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAll_StatusCode() throws Exception {
        mockMvc.perform(TestUtility.createGetBuilder(UserController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAll_ReturnsArray() throws Exception {
        String description = "Сервер должен вернуть список объектов";
        List<User> users = TestUtility.receiveObjects(mockMvc, UserController.URL);
        Assertions.assertNotNull(users, description);
    }
}
