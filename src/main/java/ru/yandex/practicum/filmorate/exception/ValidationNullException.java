package ru.yandex.practicum.filmorate.exception;

public class ValidationNullException extends NullPointerException {
    public ValidationNullException(String message) {
        super(message);
    }
}

