package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

public class ErrorResponse {
    @Getter
    String error;
    @Getter
    String description;

public ErrorResponse (String error, String description){
    this.error=error;
    this.description=description;
}
}
