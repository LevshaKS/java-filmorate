package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final Logger logger = LoggerFactory.getLogger(MpaController.class);

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }


    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public Mpa getMpaId(@Positive(message = "неверное значение") @PathVariable long id) {
        logger.info("возращаем жанр по id: " + id);
        return mpaService.getMpaId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Mpa> getMpaAll() {
        logger.info("возращаем список жанров");
        return mpaService.getMpaAll();
    }
}
