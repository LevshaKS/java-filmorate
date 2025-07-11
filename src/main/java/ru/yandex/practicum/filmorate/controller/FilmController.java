package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Logger logger = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)

    public Film getFilmId(@Positive(message = "неверное значение") @PathVariable long id) {
        logger.info("вывод фильма по ID");
        return filmService.getFilmId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilmAll() {
        logger.info("вывод списка фильмов");
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        logger.info("Фильм добавлен");
        return filmService.create(film);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Positive(message = "неверное значение") @PathVariable long id) {
        logger.info("Удаление id=" + id);
        filmService.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@RequestBody Film newFilm) {
        logger.info("запись фильма обновлена");
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> likeAdd(@Positive(message = "неверное значение") @PathVariable long id,
                                    @Positive(message = "неверное значение") @PathVariable long userId) {
        return filmService.likeAdd(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> likeDelete(@Positive(message = "неверное значение") @PathVariable long id,
                                       @Positive(message = "неверное значение") @PathVariable long userId) {
        return filmService.likeDelete(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopular(@Positive(message = "неверное значение") @RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
