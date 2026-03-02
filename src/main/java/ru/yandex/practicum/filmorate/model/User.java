package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.controller.Storable;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"id"})
public class User implements Storable {
    private Long id;
    @Email
    @Builder.Default
    private String email = "";
    @NotBlank
    @Builder.Default
    private String login = "";
    @Builder.Default
    private String name = "";
    @Past
    @Builder.Default
    private LocalDate birthday = LocalDate.MIN;

    @JsonCreator
    public User(@JsonProperty("id") Long id,
                @JsonProperty("email") String email,
                @JsonProperty("login") String login,
                @JsonProperty("name") String name,
                @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
