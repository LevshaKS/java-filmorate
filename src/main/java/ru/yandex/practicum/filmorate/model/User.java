package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"id", "name", "birthday"}, callSuper = false)
public class User extends DataModel {

    private Long id;

    @NotNull(message = "email не заполнен")
    @Email(message = "email не соотвествует формату")
    private String email;

    @NotNull(message = "логин не заполнен")
    private String login;

    private String name;

    @NotNull(message = "дата не заполнена")
    @Past(message = "дата из будущего")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

}
