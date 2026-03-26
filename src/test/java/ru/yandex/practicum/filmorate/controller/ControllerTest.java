package ru.yandex.practicum.filmorate.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTest {
    protected static final Long ABSENT_ID = 999999L;

    private static final String ADD_REQUEST_PATH = "add/request/";
    private static final String ADD_RESPONSE_PATH = "add/response/";
    private static final String UPDATE_REQUEST_PATH = "update/request/";
    private static final String UPDATE_RESPONSE_PATH = "update/response/";

    @Autowired
    protected MockMvc mockMvc;

    protected static String getContentAddRequest(String basePath, String name, String modelType) {
        return getContent(String.format("%s%s%s-%s-add-request.json", basePath, ADD_REQUEST_PATH, name, modelType));
    }

    protected static String getContentAddResponse(String basePath, String name, String modelType) {
        return getContent(String.format("%s%s%s-%s-add-response.json", basePath, ADD_RESPONSE_PATH, name, modelType));
    }

    protected static String getContentUpdateRequest(String basePath, String name, String modelType) {
        return getContent(String.format("%s%s%s-%s-update-request.json", basePath, UPDATE_REQUEST_PATH, name, modelType));
    }

    protected static String getContentUpdateResponse(String basePath, String name, String modelType) {
        return getContent(String.format("%s%s%s-%s-update-response.json", basePath, UPDATE_RESPONSE_PATH, name, modelType));
    }

    private static String getContent(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions performPost(String url, String content) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions performPut(String url, String content) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions performGetAll(String url) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions performGet(String url, Long id) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(createUrl(url, id)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String createUrl(String url, Long id) {
        return String.format("%s/%d", url, id);
    }

    protected void expectStatusIsOk(ResultActions actions) {
        expect(actions, MockMvcResultMatchers.status().isOk());
    }

    private void expect(ResultActions actions, ResultMatcher matcher) {
        try {
            actions.andExpect(matcher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void expectStatusIsCreated(ResultActions actions) {
        expect(actions, MockMvcResultMatchers.status().isCreated());
    }

    protected void expectStatusIsBadRequest(ResultActions actions) {
        expect(actions, MockMvcResultMatchers.status().isBadRequest());
    }

    protected void expectStatusIsNotFound(ResultActions actions) {
        expect(actions, MockMvcResultMatchers.status().isNotFound());
    }

    protected void expectContentJSON(ResultActions actions, String expectedJSON) {
        expect(actions, MockMvcResultMatchers.content().json(expectedJSON));
    }

    protected void expectNotEmptyId(ResultActions actions) {
        expect(actions, MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    protected Long getId(ResultActions actions) {
        try {
            String content = getContent(actions);
            return Long.parseLong(new JSONObject(content).getString("id"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getContent(ResultActions actions) throws UnsupportedEncodingException {
        return actions
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    protected Set<Long> getIdSet(ResultActions actions, String setName) {
        try {
            String content = getContent(actions);
            JSONArray idsArray = getJsonArray(setName, content);
            return createSet(idsArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray getJsonArray(String setName, String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONArray(setName);
    }

    private Set<Long> createSet(JSONArray idsArray) throws JSONException {
        Set<Long> idSet = new HashSet<>();
        for (int i = 0; i < idsArray.length(); i++) {
            idSet.add(idsArray.getLong(i));
        }
        return idSet;
    }

    protected Long[] getObjectIds(ResultActions actions) {
        try {
            String content = getContent(actions);
            JSONArray objectsArray = new JSONArray(content);
            return getIds(objectsArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Long[] getIds(JSONArray objectsArray) throws JSONException {
        int arrayLength = objectsArray.length();
        Long[] objectIds = new Long[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            JSONObject jsonObject = objectsArray.getJSONObject(i);
            objectIds[i] = jsonObject.getLong("id");
        }
        return objectIds;
    }

    protected String addId(String json, Long id) {
        try {
            JSONObject object = new JSONObject(json);
            object.put("id", id);
            return object.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
