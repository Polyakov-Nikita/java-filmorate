package ru.yandex.practicum.filmorate.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

public class TestUtility {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

    public static <O> MockHttpServletRequestBuilder createPostBuilder(O object, String url) {
        try {
            return MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(object));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <O> MockHttpServletRequestBuilder createPutBuilder(O object, String url) {
        try {
            return MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(object));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder createGetBuilder(String url) {
        try {
            return MockMvcRequestBuilders.get(url)
                    .contentType(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <O> O parseObject(String content, Class<O> oClass) throws Exception {
        return MAPPER.readValue(content, oClass);
    }

    public static <O> List<O> receiveObjects(MockMvc mockMvc, String url) throws Exception {
        String content = mockMvc.perform(createGetBuilder(url))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return MAPPER.readValue(content, new TypeReference<>() {
        });
    }
}
