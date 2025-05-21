package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {

    private final Logger logger = LoggerFactory.getLogger(MpaStorage.class);
    private final MpaStorage<Mpa> mpa;

    @Autowired
    public MpaService(MpaStorage<Mpa> mpa) {
        this.mpa = mpa;
    }

    public Mpa getMpaId(long id) {
        Mpa result = mpa.getId(id);
        if (result == null) {
            logger.warn("нет такого рейтиена");
            throw new ErrorIsNull("нет такого рейтиена");
        }
        logger.info("возвращаем рейтинг по id: " + id);
        return result;
    }

    public Collection<Mpa> getMpaAll() {
        logger.info("возращаем все жанры");
        Collection<Mpa> reselt = mpa.getAll();
        if (reselt.isEmpty()) {
            throw new ErrorIsNull("список жанров пуст");
        }
        return reselt;
    }

}
