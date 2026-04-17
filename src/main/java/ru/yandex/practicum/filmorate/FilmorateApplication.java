package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan("ru.yandex.practicum.filmorate")
public class FilmorateApplication {
    public static void main(String[] args) {
        log.info("Запуск сервера");
        SpringApplication.run(FilmorateApplication.class, args);
    }
}
