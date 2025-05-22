package ru.yandex.practicum.filmorate.storage.dao;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

@Repository

public class FilmDbStore extends BaseRepository<Film> implements FilmStorage<Film> {

    protected final Logger logger = LoggerFactory.getLogger(FilmDbStore.class);  //логгер

    public FilmDbStore(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(Film film) { //добавление нового фильма

        try {
            String query = "INSERT INTO films(name, description, release_date, duration, mpa_rating_id) VALUES(?,?,?,?,?)";
            long id = insert(
                    query,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
            film.setId(id);
            logger.info("добавили фильм id: " + id);
        } catch (DataAccessException e) {
            throw new ErrorIsNull(e.getMessage());
        }
        return film;
    }

    @Override
    public Film update(Film film) { //обновили запись фильма
        String query = "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_rating_id=? WHERE id=?";
        update(
                query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        logger.info("запись фильма обновлена");
        return film;
    }

    @Override
    public Film delete(long id) {    // удаление фильма по id
        Film film = getId(id);
        String query = "DELETE FROM films WHERE id=?";
        boolean isDelete = delete(query, id);
        if (isDelete) {
            logger.info("удален фильм id: " + id);

            return film;
        } else {
            throw new NotFoundException("ошибка удаления записи");
        }
    }

    @Override
    public Film getId(long id) {        //получам фильм по id
        String query = "SELECT * FROM films WHERE id=?";
        Optional<Film> film = findOne(query, id);
        if (film.isPresent()) {
            logger.info("возращаем фильм id: " + id);
            Film result = film.get();
            result.setLikesId(new HashSet<>(getLikeId(result.getId())));
            return result;
        } else {
            throw new ErrorIsNull("фильма с таким id нет");
        }
    }

    @Override
    public Collection<Film> getAll() {      //получам весь список фильмов
        String query = "SELECT * FROM films";
        logger.info("возращаем список фильмов");
        return findMany(query);
    }

    @Override
    public Collection<Long> getLikeId(long filmId) { // получение списка лайков
        String query = "SELECT user_id FROM film_likes WHERE film_id=" + filmId;
        logger.info("получили список лайков");
        return jdbc.queryForList(query, Long.class);
    }

    @Override
    public Collection<Long> setLikeId(long filmId, long userId) { //добавление лайка
        Collection<Long> likeList = getLikeId(filmId);
        if (likeList.contains(userId)) {
            throw new ErrorIsNull("данный пользователе уже поставил лайк");
        }
        String query = "INSERT INTO film_likes(film_id, user_id) VALUES (?,?)";
        int rowUpdated = jdbc.update(query, filmId, userId);
        if (rowUpdated > 0) {
            logger.info("лайк добавлен");
            return getLikeId(filmId);
        } else {
            throw new ErrorIsNull("ошибка добавления лайка");
        }
    }

    @Override
    public Collection<Long> delLikesId(long filmId, long userId) { // удалили лайк из списка
        String quary = "DELETE FROM film_likes WHERE film_id=? AND user_id=?";
        int rowDeleted = jdbc.update(quary, filmId, userId);
        if (rowDeleted > 0) {
            logger.info("удалили лайк из списка");
            return getLikeId(filmId);
        }
        throw new ErrorIsNull("ошибка при удалении лайка");
    }

    @Override
    public void addGenre(long filmId, Set<Genre> genreSet) {
        try {
            List<Object[]> batch = new ArrayList<>();
            for (Genre genre : genreSet) {
                Object[] value = new Object[]{filmId, genre.getId()};
                batch.add(value);
            }
            jdbc.batchUpdate("INSERT INTO genre(film_id, genre_id) VALUES(?,?)", batch);
            logger.info("добавление списка жанров фильма в db");
        } catch (DataAccessException e) {
            throw new ErrorIsNull(e.getMessage());
        }
    }

    @Override
    public void deleteGenre(long filmId) {
        jdbc.update("DELETE FROM genre WHERE film_id=?", filmId);
        logger.info("удаление списка жанров фильма в db");
    }

    @Override
    public void updateGenre(long filmId, Set<Genre> genreSet) {
        deleteGenre(filmId);
        addGenre(filmId, genreSet);
        logger.info("обновление списка жанров фильма в db");
    }

    @Override
    public Set<Genre> getGenre(long filmId) {
        String quary = "SELECT g.genre_id AS id, gs.name AS name " +
                "FROM genre AS g LEFT OUTER JOIN genres AS gs ON g.genre_id = gs.id " +
                "WHERE g.film_id =" + filmId + " ORDER BY id";
        Set<Genre> newGenreSet = new HashSet<>(jdbc.query(quary, new GenreRowMapper()));

        logger.info("возращаем список жанров фильма");
        return newGenreSet;
    }

}
