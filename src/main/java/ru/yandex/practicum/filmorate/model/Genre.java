package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Genre extends DataModel {

    @NonNull
    private Long id;

    @NonNull
    private String name;

}
