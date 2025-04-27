package ru.yandex.practicum.filmorate;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FilmControllerTest {

    Exception exception;
    private final ValidateController validate = new ValidateController();
    Film film;
    Set<Long> testList = new HashSet<>() {
    };

    @BeforeEach
    void setUp() {
        film = new Film(1L, "name", "описание ", LocalDate.now().minusYears(3), 20, testList);
    }

    @Test
    public void filmNullName() {
        film.setName(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("название пустое", exception.getMessage());
    }

    @Test
    public void filmNullDescription() {
        film.setDescription(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("описание пустое", exception.getMessage());
    }

    @Test
    public void filmNullReleaseDate() {
        film.setReleaseDate(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("дата выпуска не заполнена", exception.getMessage());
    }

    @Test
    public void filmEmptyName() {
        film.setName("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("название пустое", exception.getMessage());
    }

    @Test
    public void filmEmptyDescription() {
        film.setDescription("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("описание пустое", exception.getMessage());
    }

    @Test
    public void filmMax200Description() {
        film.setDescription(Strings.repeat("*", 220));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("превышает количество символов", exception.getMessage());
    }

    @Test
    public void filmMax201Description() {
        film.setDescription(Strings.repeat("*", 201));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("превышает количество символов", exception.getMessage());
    }

    @Test
    public void filmMax199Description() {
        film.setDescription(Strings.repeat("*", 199));
        Assertions.assertDoesNotThrow(() -> validate.validateFilm(film));
    }

    @Test
    public void filmNotReleaseDate() {
        film.setReleaseDate(LocalDate.MIN);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("дата выпуска раньше 28 декабря 1895", exception.getMessage());
    }

    @Test
    public void filmPositiveDuration() {
        film.setDuration(-4);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("продолжительность <= 0", exception.getMessage());
    }

    @Test
    public void filmIs0Duration() {
        film.setDuration(0);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateFilm(film));
        Assertions.assertEquals("продолжительность <= 0", exception.getMessage());
    }

}
