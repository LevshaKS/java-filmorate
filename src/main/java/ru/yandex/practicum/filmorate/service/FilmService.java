package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage<Film> filmStorage;

    private final UserStorage<User> userStorage;
    private final ValidateController validateController;
    private final MpaStorage<Mpa> mpaStorage;

    private final GenreStorage<Genre> genreStorage;

    private final Logger logger = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage<Film> filmStorage, UserStorage<User> userStorage, ValidateController validateController,
                       MpaStorage<Mpa> mpaStorage, GenreStorage<Genre> genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validateController = validateController;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;


    }

    public Film create(Film film) {
        validateController.validateFilm(film);
        Film returnFilm = filmStorage.create(film);
        filmStorage.addGenre(returnFilm.getId(), returnFilm.getGenres());
        logger.info("Фильм добавлен id: " + returnFilm.getId());
        return returnFilm;
    }

    public Film getFilmId(long id) {
        Film getFilm = filmStorage.getId(id);
        if (getFilm == null) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("поиск по id=" + id);

        getFilm.setGenres(filmStorage.getGenre(id));
        getFilm.setMpa(mpaStorage.getId(getFilm.getMpa().getId()));
        getFilm.setLikesId(new HashSet<>(filmStorage.getLikeId(id)));
        return getFilm;
    }

    public Collection<Film> getAll() {
        logger.info("Вернули список");
        Collection<Film> returnAllFilm = filmStorage.getAll();
        Collection<Mpa> returnAllMpa = mpaStorage.getAll();
        Map<Long, Mpa> allMpa = new HashMap<>();
        returnAllMpa.stream().peek(mpa -> allMpa.put(mpa.getId(), mpa));

        if (returnAllFilm.isEmpty()) {
            throw new ErrorIsNull("список пуст");
        }
        return returnAllFilm.stream()
                .peek(film -> {
                    film.setMpa(allMpa.get(film.getMpa().getId()));
                    film.setGenres(filmStorage.getGenre(film.getId()));
                    film.setLikesId(new HashSet<>(filmStorage.getLikeId(film.getId())));
                })
                .collect(Collectors.toSet());
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

        filmStorage.updateGenre(newFilm.getId(), newFilm.getGenres());
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
