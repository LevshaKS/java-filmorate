package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Friends {

    @NonNull
    private long userId;

    @NonNull
    private long toUserId;
}
