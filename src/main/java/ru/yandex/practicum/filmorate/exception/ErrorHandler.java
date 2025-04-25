package ru.yandex.practicum.filmorate.exception;


import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
public  ErrorResponse handlerIsNull (final ErrorIsNull e){
    logger.warn(e.getMessage());
    return new ErrorResponse("error",e.getMessage());
}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowbla (final Throwable e){
        logger.warn(e.getMessage());
        return new ErrorResponse("error", "Произошла непредвиденная ошибка.") ;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(final ValidationException e) {
        logger.warn(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationNullException(final ValidationNullException e) {
        logger.warn(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<ErrorResponse> violations = e.getConstraintViolations().stream()
                .map(
                        error -> new ErrorResponse(
                                error.getPropertyPath().toString(),
                                error.getMessage()
                        )
                )
                .peek(error -> logger.warn(error.getDescription()))
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ErrorResponse> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse(error.getField(), error.getDefaultMessage()))
                .peek(error -> logger.warn(error.getError() + " = " + error.getDescription()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}


