package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Repository
public class InMemoryFilmStorage extends Storage<Film> implements FilmStorage<Film> {

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        if (film.getLikesId() == null) {
            film.setLikesId(new HashSet<>() {
            });
        }
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

        if (newFilm.getLikesId() == null) {
            newFilm.setLikesId(new HashSet<>() {
            });
        }
        oldFilm.setLikesId(newFilm.getLikesId());
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

    private long getNextId() {
        long currentMaxId = dataMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;

    }
}
