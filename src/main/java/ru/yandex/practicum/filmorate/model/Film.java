package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Film extends DataModel implements Comparable<Film> {

    private Long id;

    @NotNull(message = "название не заполнено")
    private String name;

    @NotNull(message = "описание пустое")
    @Size(min = 1, max = 200, message = "не правильное число символов")
    private String description;

    @NotNull(message = "дата пустая")
    @Past(message = "дата из будущего")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "неверное значение")
    @Min(1)
    private int duration;


    private Set<Long> likesId;

    @Override
    public int compareTo(Film o) {
        if (o.getLikesId()==null){
            return 0;
        }
        return (int)(o.getLikesId().size()-this.getLikesId().size());
    }
}