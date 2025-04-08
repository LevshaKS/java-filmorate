package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// взято с просторов интернета  вывод сообщений при ошибки

@Getter
@RequiredArgsConstructor
public class Violation {
    private final String fieldName;
    private final String message;

}