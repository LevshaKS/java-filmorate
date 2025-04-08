package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final ValidateController validateController = new ValidateController();
    private final Map<Long, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public ResponseEntity<Collection<Film>> filmAll() {
        log.info("вывод списка фильмов");
        return ResponseEntity.ok(films.values());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        validateController.validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("фильм добавлен id: " + film.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("ID пустой");
            throw new ValidationException("пользователь с таким ID не найден");
            // изначально сделал чтоб в теле была ошибка, но не прошло тесты в Postman
            //  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID пустой");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn("пользователь с таким ID не найден");
            // изначально сделал чтоб в теле была ошибка, но не прошло тесты в Postman
            //   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("пользователь с таким ID не найден");
            throw new ValidationException("пользователь с таким ID не найден");
        }
        validateController.validateFilm(newFilm);
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("запись фильма обновлена");
        return ResponseEntity.ok(oldFilm);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;

    }
}
