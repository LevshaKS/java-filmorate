package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataModel;

import java.util.Collection;

public interface MpaStorage<T extends DataModel> {


    T getId(long id);

    Collection<T> getAll();

}
