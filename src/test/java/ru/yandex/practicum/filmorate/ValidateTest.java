package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValidateTest {
    private Validator validator;

    User user = new User();
    Film film = new Film();


    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        user.setId(1L);
        user.setEmail("ya@yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now().minusYears(3));

        film.setId(1L);
        film.setName("kino");
        film.setDescription("kino o kine");
        film.setReleaseDate(LocalDate.of(1990, 02, 22));
        film.setDuration(25);
        film.setMpa(new Mpa());

    }

    @Test
    void validatorUserOk() {
                Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validatorUserTest() {
        user.setEmail("nameyabex.ru");
        user.setBirthday(LocalDate.now().plusDays(3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("email не соотвествует формату",
                "дата из будущего");
    }

    @Test
    void validatorUserNotNull() {
        user.setEmail(null);
        user.setLogin(null);
        user.setBirthday(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("email не заполнен",
                "логин не заполнен", "дата не заполнена");
    }

    @Test
    void validatorFilmOk() {
            Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validatorFilmTest() {
        film.setDescription("*".repeat( 220));
        film.setReleaseDate( LocalDate.now().plusYears(3));
         Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("не правильное число символов",
                "дата из будущего");
    }

    @Test
    void validatorFilmNotNull() {
        film.setName(null);
        film.setReleaseDate(null);
        film.setDescription(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsExactlyInAnyOrder("название не заполнено", "дата пустая", "описание пустое");
    }

}

