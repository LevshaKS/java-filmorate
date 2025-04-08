package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

// взято с просторов интернета  вывод сообщений при ошибки
@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<Violation> violations;
}



