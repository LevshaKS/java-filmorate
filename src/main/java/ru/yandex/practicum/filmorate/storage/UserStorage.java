package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataModel;

import java.util.Collection;


public interface UserStorage<T extends DataModel> {

    T create(T data);

    T update(T data);

    T delete(long id);

    T getId(long id);

    Collection<T> getAll();

    Collection<Long> getFriendId(long id);

    Collection<Long> setFriendId(long id, long userId);

    Collection<Long> delFriendId(long id, long userId);
}
