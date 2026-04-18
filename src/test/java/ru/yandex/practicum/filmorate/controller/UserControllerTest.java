package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;

@AutoConfigureMockMvc
public class UserControllerTest extends ControllerTest {
    private static final String BASE_PATH = "users/";
    private static final String MODEL_TYPE = "user";

    public static final String CORRECT_ADD_REQUEST = getContentAddRequest(BASE_PATH, "correct", MODEL_TYPE);
    public static final String CORRECT_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "correct", MODEL_TYPE);
    public static final String FAIL_LOGIN_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-login", MODEL_TYPE);
    public static final String FAIL_EMAIL_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-email", MODEL_TYPE);
    public static final String FAIL_BIRTHDAY_ADD_REQUEST = getContentAddRequest(BASE_PATH, "fail-birthday", MODEL_TYPE);
    public static final String CORRECT_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "correct", MODEL_TYPE);
    public static final String CORRECT_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "correct", MODEL_TYPE);
    public static final String EMPTY_NAME_ADD_REQUEST = getContentAddRequest(BASE_PATH, "empty-name", MODEL_TYPE);
    public static final String EMPTY_NAME_ADD_RESPONSE = getContentAddResponse(BASE_PATH, "empty-name", MODEL_TYPE);
    public static final String EMPTY_NAME_UPDATE_REQUEST = getContentUpdateRequest(BASE_PATH, "empty-name", MODEL_TYPE);
    public static final String EMPTY_NAME_UPDATE_RESPONSE = getContentUpdateResponse(BASE_PATH, "empty-name", MODEL_TYPE);
    private static final String FRIENDS_SET_NAME = "friends";
    private static final int FRIENDS_COUNT = 11;
    private static final int FRIENDS_COMMON_COUNT = 4;

    @Test
    public void add_CorrectData_StatusCode() {
        expectStatusIsCreated(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
    }

    @Test
    public void add_CorrectData_ReturnsObject() {
        expectContentJSON(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST), CORRECT_ADD_RESPONSE);
    }

    @Test
    public void add_CorrectData_NonNullId() {
        expectNotEmptyId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
    }

    @Test
    public void add_EmptyName_StatusCode() {
        expectStatusIsCreated(performPost(UserController.URL_BASE, EMPTY_NAME_ADD_REQUEST));
    }

    @Test
    public void add_EmptyName_ReturnsObject() {
        expectContentJSON(performPost(UserController.URL_BASE, EMPTY_NAME_ADD_REQUEST), EMPTY_NAME_ADD_RESPONSE);
    }

    @Test
    public void add_FailLogin_StatusCode() {
        expectStatusIsBadRequest(performPost(UserController.URL_BASE, FAIL_LOGIN_ADD_REQUEST));
    }

    @Test
    public void add_FailEmail_StatusCode() {
        expectStatusIsBadRequest(performPost(UserController.URL_BASE, FAIL_EMAIL_ADD_REQUEST));
    }

    @Test
    public void add_FailBirthday_StatusCode() {
        expectStatusIsBadRequest(performPost(UserController.URL_BASE, FAIL_BIRTHDAY_ADD_REQUEST));
    }

    @Test
    public void update_CorrectData_StatusCode() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        expectStatusIsOk(performPut(UserController.URL_BASE, body));
    }

    @Test
    public void update_CorrectData_ReturnsObject() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(CORRECT_UPDATE_REQUEST, id);
        expectContentJSON(performPut(UserController.URL_BASE, body), CORRECT_UPDATE_RESPONSE);
    }

    @Test
    public void update_EmptyName_StatusCode() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(EMPTY_NAME_UPDATE_REQUEST, id);
        expectStatusIsOk(performPut(UserController.URL_BASE, body));
    }

    @Test
    public void update_EmptyName_ReturnsObject() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        String body = addId(EMPTY_NAME_ADD_REQUEST, id);
        expectContentJSON(performPut(UserController.URL_BASE, body), EMPTY_NAME_UPDATE_RESPONSE);
    }

    @Test
    public void update_Unknown_StatusCode() {
        String body = addId(CORRECT_UPDATE_REQUEST, ABSENT_ID);
        expectStatusIsNotFound(performPut(UserController.URL_BASE, body));
    }

    @Test
    public void addFriend_StatusCode() {
        Long userId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        Long friendId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        expectStatusIsOk(performAddFriend(userId, friendId));
    }

    private ResultActions performAddFriend(Long userId, Long friendId) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/%d%s/%d",
                    UserController.URL_BASE, userId, UserController.URL_FRIENDS, friendId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addFriend_ContainsFriend() {
        Long userId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        Long friendId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddFriend(userId, friendId);
        Set<Long> friends = getIdSet(performGet(UserController.URL_BASE, userId), FRIENDS_SET_NAME);
        Assertions.assertTrue(friends.contains(friendId));
    }

    @Test
    public void getAll_StatusCode() {
        expectStatusIsOk(performGetAll(UserController.URL_BASE));
    }

    @Test
    public void get_StatusCode() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        expectStatusIsOk(performGet(UserController.URL_BASE, id));
    }

    @Test
    public void get_ReturnsObject() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        expectContentJSON(performGet(UserController.URL_BASE, id), CORRECT_ADD_RESPONSE);
    }

    @Test
    public void getFriends_StatusCode() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        expectStatusIsOk(performGetFriends(id));
    }

    private ResultActions performGetFriends(Long id) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d%s",
                    UserController.URL_BASE, id, UserController.URL_FRIENDS)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFriends_ReturnsArray() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        Long[] friendIds = addFriends(id, FRIENDS_COUNT);
        Long[] responseIds = getObjectIds(performGetFriends(id));
        Arrays.sort(friendIds);
        Arrays.sort(responseIds);
        Assertions.assertArrayEquals(friendIds, responseIds);
    }

    private Long[] addFriends(Long id, int friendsCount) {
        Long[] friendIds = new Long[friendsCount];
        for (int i = 0; i < friendsCount; i++) {
            Long friendId = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
            performAddFriend(id, friendId);
            friendIds[i] = friendId;
        }
        return friendIds;
    }

    @Test
    public void getCommonFriends_StatusCode() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        Long otherId = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        expectStatusIsOk(performGetCommonFriends(id, otherId));
    }

    private ResultActions performGetCommonFriends(Long id, Long otherId) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d%s%s/%d",
                    UserController.URL_BASE, id, UserController.URL_FRIENDS, UserController.URL_COMMON, otherId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getCommonFriends_ReturnsArray() {
        Long id = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        Long[] commonIds = addFriends(id, FRIENDS_COMMON_COUNT);
        addFriends(id, FRIENDS_COUNT);
        Long otherId = getId(performPost(UserController.URL_BASE, CORRECT_ADD_REQUEST));
        addExistingFriends(otherId, commonIds);
        addFriends(otherId, FRIENDS_COUNT);
        Long[] responseIds = getObjectIds(performGetCommonFriends(id, otherId));
        Arrays.sort(commonIds);
        Arrays.sort(responseIds);
        Assertions.assertArrayEquals(commonIds, responseIds);
    }

    private void addExistingFriends(Long id, Long[] friendIds) {
        for (Long friendId : friendIds) {
            performAddFriend(id, friendId);
        }
    }

    @Test
    public void deleteFriend_StatusCode() {
        Long userId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        Long friendId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddFriend(userId, friendId);
        expectStatusIsOk(performDeleteFriend(userId, friendId));
    }

    private ResultActions performDeleteFriend(Long userId, Long friendId) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%d%s/%d",
                    UserController.URL_BASE, userId, UserController.URL_FRIENDS, friendId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteFriend_NotContainsFriend() {
        Long userId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        Long friendId = getId(performPost(UserController.URL_BASE, UserControllerTest.CORRECT_ADD_REQUEST));
        performAddFriend(userId, friendId);
        performDeleteFriend(userId, friendId);
        Set<Long> friends = getIdSet(performGet(UserController.URL_BASE, userId), FRIENDS_SET_NAME);
        Assertions.assertFalse(friends.contains(friendId));
    }
}
