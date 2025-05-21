package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
public class Mpa extends DataModel {

    @NonNull
    @Positive
    private long id;


    private String name;


}
