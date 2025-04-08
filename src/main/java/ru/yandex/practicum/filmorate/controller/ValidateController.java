package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidateController {

    private final Logger log = LoggerFactory.getLogger(ValidateController.class);

    public void ValidateFilm(Film film) {

        final LocalDate FILM_DATE = LocalDate.of(1895, 12, 28);

        if (film.getName() == null) {
            log.warn("название пустое");
            throw new ValidationNullException("название пустое");
        }

        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("название пустое");
            throw new ValidationException("название пустое");
        }
        if (film.getDescription() == null) {
            log.warn("описание пустое");
            throw new ValidationNullException("описание пустое");
        }

        if (film.getDescription().isBlank() || film.getDescription().isEmpty()) {
            log.warn("описание пустое");
            throw new ValidationException("описание пустое");
        }

        if (film.getDescription().length() > 200) {
            log.warn("превышает количество символов");
            throw new ValidationException("превышает количество символов");
        }

        if (film.getReleaseDate() == null) {
            log.warn("дата выпуска не заполнена");
            throw new ValidationNullException("дата выпуска не заполнена");
        }

        if (FILM_DATE.isAfter(film.getReleaseDate())) {
            log.warn("дата выпуска раньше 28 декабря 1895");
            throw new ValidationException("дата выпуска раньше 28 декабря 1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("продолжительность <= 0");
            throw new ValidationException("продолжительность <= 0");
        }
    }

    public void ValidateUser(User user) {
        if (user.getLogin() == null) {
            log.warn("login пустой");
            throw new ValidationNullException("login пустой");
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            log.warn("логин пустой");
            throw new ValidationException("login пустой");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("присутствуют пробелы");
            throw new ValidationException("присутствуют пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("имя не заполнено, будет присвоенно имя " + user.getLogin());
        }

        if (user.getEmail() == null) {
            log.warn("email пустой");
            throw new ValidationNullException("email пустой");
        }

        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            log.warn("email пустой");
            throw new ValidationException("email пустой");
        }
        if (!user.getEmail().contains("@")) {

            log.warn("введен не email");
            throw new ValidationException("введен не email");
        }
        if (user.getBirthday() == null) {
            log.warn("дата пустая");
            throw new ValidationNullException("дата пустая");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("введена дата из будущего");
            throw new ValidationException("введена дата из будущего");
        }

    }
}


