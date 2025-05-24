package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;


@Repository
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage<Mpa> {

    protected final Logger logger = LoggerFactory.getLogger(MpaDbStorage.class);  //логгер

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa getId(long id) {
        String query = "SELECT * FROM mpa_rating WHERE id=?";
        Optional<Mpa> mpa = findOne(query, id);
        if (mpa.isPresent()) {
            logger.info("возращаем райтинг id: " + id);
            return mpa.get();
        } else {
            throw new ErrorIsNull("значение райтинга пустое");
        }
    }

    @Override
    public Collection<Mpa> getAll() {
        String query = "SELECT * FROM mpa_rating";
        logger.info("возращаем список райтингов");
        return findMany(query);
    }

}
