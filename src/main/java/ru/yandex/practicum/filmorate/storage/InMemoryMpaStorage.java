package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryMpaStorage extends Storage<Mpa> implements MpaStorage<Mpa> {

    protected final Map<Long, Mpa> mpaRating = new HashMap<>();

    protected InMemoryMpaStorage() {

        for (long i = 1; i < 7; i++) {
            mpaRating.put(i, new Mpa());
            mpaRating.get(i).setId(i);
        }
        mpaRating.get(1L).setName("G");
        mpaRating.get(2L).setName("PG");
        mpaRating.get(3L).setName("PG-13");
        mpaRating.get(4L).setName("R");
        mpaRating.get(1L).setName("NC-17");
    }

    @Override
    public Mpa getId(long id) {
        return mpaRating.get(id);
    }

    @Override
    public Collection<Mpa> getAll() {
        return mpaRating.values();
    }

}

