package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryMpaStorage extends Storage<Mpa> implements MpaStorage<Mpa> {

    protected final Map<Long, Mpa> mpa_rating = new HashMap<>();

    protected InMemoryMpaStorage() {

        for (long i = 1; i < 7; i++) {
            mpa_rating.put(i, new Mpa());
            mpa_rating.get(i).setId(i);
        }
        mpa_rating.get(1L).setName("G");
        mpa_rating.get(2L).setName("PG");
        mpa_rating.get(3L).setName("PG-13");
        mpa_rating.get(4L).setName("R");
        mpa_rating.get(1L).setName("NC-17");
    }

    @Override
    public Mpa getId(long id) {
        return mpa_rating.get(id);
    }

    @Override
    public Collection<Mpa> getAll() {
        return mpa_rating.values();
    }

}

