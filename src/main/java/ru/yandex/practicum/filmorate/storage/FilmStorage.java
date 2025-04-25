package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.Collection;


public interface FilmStorage<T extends DataModel> {


      T create (T data);

     T update (T data);

     T delete(long id);

     T getId (long id);
     Collection<T> getAll ();

      Collection<Long>  getLikeId(long id);

     Collection<Long>  setLikeId(long id, long userId);

    Collection<Long>  delLikesId(long id, long userId);

           }
