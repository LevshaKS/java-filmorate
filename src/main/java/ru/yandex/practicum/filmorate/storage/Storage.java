package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.DataModel;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Storage<T extends DataModel>  {
    protected final Map<Long, T> dataMap = new HashMap<>();
protected final Logger logger = LoggerFactory.getLogger(Storage.class);


    public T delete(long id) {
       T delData = dataMap.remove(id);
      return delData;
    }

    public T getId(long id) {
        if (!dataMap.containsKey(id)){
            throw new ErrorIsNull("нет такого id");}
        logger.info("Вернули по id="+ id);
        return dataMap.get(id);
         }

    public Collection<T> getAll() {
        logger.info("Вернули список");
        return dataMap.values();
      }



}
