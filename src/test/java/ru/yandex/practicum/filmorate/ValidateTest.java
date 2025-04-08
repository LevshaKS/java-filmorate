package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValidateTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validatorUserOk() {
        User user = new User(1L, "name@yabex.ru", "log", "name", LocalDate.now().minusYears(3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validatorUserTest() {
        User user = new User(1L, "nameyabex.ru", "log", "name", LocalDate.now().plusYears(3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("email не соотвествует формату",
                "дата из будущего");
    }

    @Test
    void validatorUserNotNull() {
        User user = new User(1L, null, null, "name", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("email не заполнен",
                "логин не заполнен", "дата не заполнена");
    }

    @Test
    void validatorFilmOk() {
        Film film = new Film(1L, "kino", "kino o kine", LocalDate.of(1990, 02, 22), 25);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validatorFilmTest() {
        Film film = new Film(1L, "kino", Strings.repeat("*", 220),
                LocalDate.now().plusYears(3), -12);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(4);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("не правильное число символов",
                "дата из будущего", "неверное значение", "должно быть не меньше 1");
    }

    @Test
    void validatorFilmNotNull() {
        Film film = new Film(1L, null, null,
                null, 0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(5);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("название не заполнено",
                "должно быть не меньше 1", "дата пустая", "описание пустое", "неверное значение");
    }

}

