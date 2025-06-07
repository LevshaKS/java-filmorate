package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage<Genre> {

    protected final Logger logger = LoggerFactory.getLogger(GenreDbStorage.class);  //логгер

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getId(long id) {
        String query = "SELECT * FROM genres WHERE id=?";
        Optional<Genre> genre = findOne(query, id);
        if (genre.isPresent()) {
            logger.info("возращаем жанр id: " + id);
            return genre.get();
        } else {
            throw new ErrorIsNull("значение жанра пустое");
        }
    }

    @Override
    public Collection<Genre> getAll() {
        String query = "SELECT * FROM genres";
        logger.info("возращаем список жанров");
        return findMany(query);
    }
}
