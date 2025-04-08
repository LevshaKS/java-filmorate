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

public class FilmControllerTest {

    Exception exception;
    private final ValidateController validate = new ValidateController();
    Film film;

    @BeforeEach
    void setUp() {
        film = new Film(1L, "name", "описание ", LocalDate.now().minusYears(3), 20);
    }

    @Test
    public void FilmNullName() {
        film.setName(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("название пустое", exception.getMessage());
    }

    @Test
    public void FilmNullDescription() {
        film.setDescription(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("описание пустое", exception.getMessage());
    }

    @Test
    public void FilmNullReleaseDate() {
        film.setReleaseDate(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("дата выпуска не заполнена", exception.getMessage());
    }

    @Test
    public void FilmEmptyName() {
        film.setName("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("название пустое", exception.getMessage());
    }

    @Test
    public void FilmEmptyDescription() {
        film.setDescription("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("описание пустое", exception.getMessage());
    }

    @Test
    public void FilmMax200Description() {
        film.setDescription(Strings.repeat("*", 220));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("превышает количество символов", exception.getMessage());
    }

    @Test
    public void FilmMax201Description() {
        film.setDescription(Strings.repeat("*", 201));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("превышает количество символов", exception.getMessage());
    }

    @Test
    public void FilmMax199Description() {
        film.setDescription(Strings.repeat("*", 199));
        Assertions.assertDoesNotThrow(() -> validate.ValidateFilm(film));
    }

    @Test
    public void FilmNotReleaseDate() {
        film.setReleaseDate(LocalDate.MIN);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("дата выпуска раньше 28 декабря 1895", exception.getMessage());
    }

    @Test
    public void FilmPositiveDuration() {
        film.setDuration(-4);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("продолжительность <= 0", exception.getMessage());
    }

    @Test
    public void FilmIs0Duration() {
        film.setDuration(0);
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.ValidateFilm(film));
        Assertions.assertEquals("продолжительность <= 0", exception.getMessage());
    }


}
