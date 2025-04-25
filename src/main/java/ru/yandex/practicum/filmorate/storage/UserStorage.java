package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.DataModel;


import java.util.Collection;

public interface UserStorage<T extends DataModel>  {

     T create (T data);

     T update (T data);

     T delete(long id);

    T getId (long id);

    public Collection<T> getAll();

    Collection<Long>  getFriendsId(long id);

    Collection<Long>  setFriendsId(long id, long userId);

    Collection<Long>  delFriendsId(long id, long userId);
}
