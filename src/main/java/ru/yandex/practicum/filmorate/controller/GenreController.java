package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final Logger logger = LoggerFactory.getLogger(GenreService.class);

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreId(@Positive(message = "неверное значение") @PathVariable long id) {
        logger.info("возращаем жанр по id: " + id);
        return genreService.getGenreId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Genre> getGenreAll() {
        logger.info("возращаем список жанров");
        return genreService.getGenreAll();
    }

}
