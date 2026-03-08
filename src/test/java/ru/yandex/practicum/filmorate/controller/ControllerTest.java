package ru.yandex.practicum.filmorate.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {
    protected static final Long ABSENT_ID = 999999L;

    private static final String ADD_REQUEST_PATH = "add/request/";
    private static final String ADD_RESPONSE_PATH = "add/response/";
    private static final String UPDATE_REQUEST_PATH = "update/request/";
    private static final String UPDATE_RESPONSE_PATH = "update/response/";

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

    protected static MockHttpServletRequestBuilder createPostBuilder(String url, String content) {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    protected static MockHttpServletRequestBuilder createGetBuilder(String url) {
        return MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected static MockHttpServletRequestBuilder createPutBuilder(String url, String content) {
        return MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    protected static Long getId(ResultActions actions) throws Exception {
        String content = actions
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Long.parseLong(new JSONObject(content).getString("id"));
    }

    protected static String addId(String json, Long id) throws JSONException {
        JSONObject object = new JSONObject(json);
        object.put("id", id);
        return object.toString();
    }
}
