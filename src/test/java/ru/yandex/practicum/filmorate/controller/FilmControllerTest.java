package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

@AutoConfigureMockMvc
public class FilmControllerTest extends ControllerTest {
    private static final String BASE_PATH = "films/";
    private static final String MODEL_TYPE = "film";

    public static final String CORRECT_ADD_REQUEST = getContentAddRequest(BASE_PATH, "correct", MODEL_TYPE);
    public static final String CORRECT_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "correct", MODEL_TYPE);
    public static final String FAIL_NAME_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-name", MODEL_TYPE);
    public static final String FAIL_DESCRIPTION_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-description", MODEL_TYPE);
    public static final String FAIL_RELEASE_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-release", MODEL_TYPE);
    public static final String FAIL_DURATION_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-duration", MODEL_TYPE);
    public static final String CORRECT_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "correct", MODEL_TYPE);
    public static final String CORRECT_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "correct", MODEL_TYPE);
    private static final String LIKES_SET_NAME = "likes";
    private static final int POPULAR_COUNT = 5;
    private static final int LIKES_COUNT = 10;

    @Test
    public void add_CorrectData_StatusCode() {
        expectStatusIsCreated(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
    }

    @Test
    public void add_CorrectData_ReturnsObject() {
        expectContentJSON(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST), CORRECT_ADD_RESPONSE);
    }

    @Test
    public void add_CorrectData_NonNullId() {
        expectNotEmptyId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
    }

    @Test
    public void add_FailName_StatusCode() {
        expectStatusIsBadRequest(performPost(FilmController.URL_BASE, FAIL_NAME_ADD_REQUEST));
    }

    @Test
    public void add_FailDescription_StatusCode() {
        expectStatusIsBadRequest(performPost(FilmController.URL_BASE, FAIL_DESCRIPTION_ADD_REQUEST));
    }

    @Test
    public void add_FailRelease_StatusCode() {
        expectStatusIsBadRequest(performPost(FilmController.URL_BASE, FAIL_RELEASE_ADD_REQUEST));
    }

    @Test
    public void add_FailDuration_StatusCode() {
        expectStatusIsBadRequest(performPost(FilmController.URL_BASE, FAIL_DURATION_ADD_REQUEST));
    }

    @Test
    public void update_CorrectData_StatusCode() {
        Long id = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        expectStatusIsOk(performPut(FilmController.URL_BASE, body));
    }

    @Test
    public void update_CorrectData_ReturnsObject() {
        Long id = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        expectContentJSON(performPut(FilmController.URL_BASE, body), CORRECT_UPDATE_RESPONSE);
    }

    @Test
    public void update_Unknown_StatusCode() {
        String body = addId(CORRECT_UPDATE_REQUEST, ABSENT_ID);
        expectStatusIsNotFound(performPut(FilmController.URL_BASE, body));
    }

    @Test
    public void addLike_StatusCode() {
        Long filmId = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        Long likerId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        expectStatusIsOk(performAddLike(filmId, likerId));
    }

    private ResultActions performAddLike(Long filmId, Long likerId) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/%d%s/%d",
                    FilmController.URL_BASE, filmId, FilmController.URL_LIKE, likerId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addLike_ContainsLike() {
        Long filmId = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        Long likerId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddLike(filmId, likerId);
        Set<Long> likes = getIdSet(performGet(FilmController.URL_BASE, filmId), LIKES_SET_NAME);
        Assertions.assertTrue(likes.contains(likerId));
    }

    @Test
    public void getAll_StatusCode() {
        expectStatusIsOk(performGetAll(FilmController.URL_BASE));
    }

    @Test
    public void get_StatusCode() {
        Long id = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        expectStatusIsOk(performGet(FilmController.URL_BASE, id));
    }

    @Test
    public void get_ReturnsObject() {
        Long id = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        expectContentJSON(performGet(FilmController.URL_BASE, id), CORRECT_ADD_RESPONSE);
    }

    @Test
    public void getPopular_StatusCode() {
        expectStatusIsOk(performGetPopular(POPULAR_COUNT));
    }

    private ResultActions performGetPopular(int count) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s%s?%s=%d",
                    FilmController.URL_BASE, FilmController.URL_POPULAR, FilmController.COUNT_PARAMETER, count)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPopular_ReturnsArray() {
        int likedFilmsCount = 3;
        Long[] likedFilmsIds = createLiked(likedFilmsCount);
        Long[] responseIds = getObjectIds(performGetPopular(likedFilmsCount));
        Assertions.assertArrayEquals(likedFilmsIds, responseIds);
    }

    private Long[] createLiked(int likedFilmsCount) {
        Long[] likedFilmsIds = new Long[likedFilmsCount];
        for (int i = 0; i < likedFilmsCount; i++) {
            Long id = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
            addLikes(id, LIKES_COUNT - i);
            likedFilmsIds[i] = id;
        }
        return likedFilmsIds;
    }

    private void addLikes(long filmId, int likesCount) {
        for (int i = 0; i < likesCount; i++) {
            Long likerId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
            performAddLike(filmId, likerId);
        }
    }

    @Test
    public void getPopular_WithoutParameter_StatusCode() {
        expectStatusIsOk(performGetPopular());
    }

    private ResultActions performGetPopular() {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s%s",
                    FilmController.URL_BASE, FilmController.URL_POPULAR)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPopular_IncorrectCount_StatusCode() {
        expectStatusIsBadRequest(performGetPopular(-1));
    }

    @Test
    public void deleteLike_StatusCode() {
        Long filmId = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        Long likerId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddLike(filmId, likerId);
        expectStatusIsOk(performDeleteLike(filmId, likerId));
    }

    private ResultActions performDeleteLike(Long filmId, Long likerId) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%d%s/%d",
                    FilmController.URL_BASE, filmId, FilmController.URL_LIKE, likerId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteLike_NotContainsLike() {
        Long filmId = getId(performPost(FilmController.URL_BASE, CORRECT_ADD_REQUEST));
        Long likerId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddLike(filmId, likerId);
        performDeleteLike(filmId, likerId);
        Set<Long> likes = getIdSet(performGet(FilmController.URL_BASE, filmId), LIKES_SET_NAME);
        Assertions.assertFalse(likes.contains(likerId));
    }
}
