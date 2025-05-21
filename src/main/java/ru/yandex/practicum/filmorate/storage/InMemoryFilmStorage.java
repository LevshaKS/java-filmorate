package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage extends Storage<Film> implements FilmStorage<Film> {

    protected final Map<Long, Set<Genre>> genreMap = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        dataMap.put(film.getId(), film);
        logger.info("Фильм добавлен id: " + film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = dataMap.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setMpa(newFilm.getMpa());
        if (newFilm.getLikesId() == null) {
            newFilm.setLikesId(new HashSet<>() {
            });
        }
        oldFilm.setLikesId(newFilm.getLikesId());
        if (newFilm.getGenres() == null) {
            newFilm.setGenres(new HashSet<>());
        }
        oldFilm.setGenres(newFilm.getGenres());
        logger.info("Запись фильма обновлена");
        return oldFilm;
    }

    @Override
    public Collection<Long> getLikeId(long id) {
        Film getLikes = dataMap.get(id);
        return getLikes.getLikesId();
    }

    @Override
    public Collection<Long> setLikeId(long id, long userId) {
        Film getLikes = dataMap.get(id);
        getLikes.getLikesId().add(userId);
        return getLikes.getLikesId();
    }

    @Override
    public Collection<Long> delLikesId(long id, long userId) {
        Film getLikes = dataMap.get(id);
        Set<Long> newLikesList = getLikes.getLikesId();
        newLikesList.remove(id);
        getLikes.setLikesId(newLikesList);
        return newLikesList;
    }

    @Override
    public Film getId(long id) {
        if (!dataMap.containsKey(id)) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("Вернули по id=" + id);
        Film result = dataMap.get(id);
        result.setGenres(genreMap.get(result.getId()));
        return result;
    }

    @Override
    public Collection<Film> getAll() {
        logger.info("Вернули список");
        Collection<Film> result = dataMap.values();
        result.stream()
                .peek(film -> film.setGenres(genreMap.get(film.getId())))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public void addGenre(long filmId, Set<Genre> genres) {

        if (genres == null) {
            genres.add(new Genre());
        }
        genreMap.put(filmId, genres);
    }

    @Override
    public void deleteGenre(long filmId) {
        Film film = dataMap.get(filmId);
        genreMap.remove(filmId);
    }

    @Override
    public void updateGenre(long filmId, Set<Genre> genres) {
        genreMap.replace(filmId, genres);
    }

    @Override
    public Set<Genre> getGenre(long filmId) {
        return genreMap.get(filmId);
    }


    private long getNextId() {
        long currentMaxId = dataMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;

    }
}
