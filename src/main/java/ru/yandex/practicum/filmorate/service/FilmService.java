package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage<Film> filmStorage;

    private final UserStorage<User> userStorage;
    private final ValidateController validateController;
    private final Logger logger = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage<Film> filmStorage, UserStorage<User> userStorage, ValidateController validateController) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validateController = validateController;

    }

    public Film create(Film film) {
        validateController.validateFilm(film);
        Film returnFilm = filmStorage.create(film);
        logger.info("Фильм добавлен id: " + returnFilm.getId());
        return returnFilm;
    }

    public Film getFilmId(long id) {
        Film getFilm = filmStorage.getId(id);
        if (getFilm == null) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("поиск по id=" + id);
        return getFilm;
    }

    public Collection<Film> getAll() {
        logger.info("Вернули список");
        Collection<Film> returnAll = filmStorage.getAll();
        if (returnAll.isEmpty()) {
            throw new ErrorIsNull("список пуст");
        }
        return returnAll;
    }

    public void delete(long id) {
        Film delFilm = filmStorage.delete(id);
        if (delFilm == null) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("удаление id=" + id);
    }

    public Film update(Film newFilm) {

        if (newFilm.getId() == null) {
            logger.warn("ID пустой");
            throw new ValidationException("ID фильма пустой");
        }
        Film oldFilm = filmStorage.getId(newFilm.getId());

        if (oldFilm == null) {
            logger.warn("фильм с таким ID не найден");
            throw new ErrorIsNull("фильм с таким ID не найден");
        }
        validateController.validateFilm(newFilm);
        logger.info("запись фильма обновлена");
        return filmStorage.update(newFilm);
    }

    public Collection<Long> likeAdd(long id, long userId) {
        getFilmId(id);
        userStorage.getId(userId);
        return filmStorage.setLikeId(id, userId);
    }

    public Collection<Long> likeDelete(long id, long userId) {
        getFilmId(id);
        userStorage.getId(userId);
        return filmStorage.delLikesId(id, userId);
    }

    public Collection<Film> getPopular(int count) {

        return getAll().stream()
                .sorted(Film::compareTo)
                .limit(count)
                .collect(Collectors.toList());
    }
}
