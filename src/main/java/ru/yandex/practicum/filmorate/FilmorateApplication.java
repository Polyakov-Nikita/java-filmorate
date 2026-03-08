package ru.yandex.practicum.filmorate;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {
    private static final Level LOG_LEVEL = Level.INFO;

    public static void main(String[] args) {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(LOG_LEVEL);
        log.info("Запуск сервера");
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
