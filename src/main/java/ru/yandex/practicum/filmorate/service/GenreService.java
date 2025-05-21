package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage<Genre> genreStorage;
    private final Logger logger = LoggerFactory.getLogger(GenreStorage.class);

    @Autowired
    public GenreService(GenreStorage<Genre> genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreId(long id) {
        Genre result = genreStorage.getId(id);
        if (result == null) {
            logger.warn("нет такого жанра");
            throw new ErrorIsNull("нет такого жанра");
        }
        logger.info("возвращаем жанр по id: " + id);
        return result;
    }

    public Collection<Genre> getGenreAll() {
        logger.info("возращаем все жанры");
        Collection<Genre> reselt = genreStorage.getAll();
        if (reselt.isEmpty()) {
            throw new ErrorIsNull("список жанров пуст");
        }
        return reselt;
    }


}
