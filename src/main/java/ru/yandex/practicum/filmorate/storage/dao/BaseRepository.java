package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

// базовый блок обработки запросов с db
@RequiredArgsConstructor
public class BaseRepository<T> {


    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) { //возращаем 1 объект и db
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);    // возращаем объект в обёртке optional
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();               //  если ошибка возращаем пустую обертку optional
        } catch (DataAccessException e) {
            throw new ErrorIsNull(e.getMessage());
        }
    }

    protected List<T> findMany(String query, Object... params) {   //возращаем список с объектами из db
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, long id) {   // удаление элемента по id из db
        int rowsDeleted = jdbc.update(query, id);
        return rowsDeleted > 0;
    }

    protected void update(String query, Object... params) {    //обновление данных в db
        int rowUpdated = jdbc.update(query, params);
        if (rowUpdated == 0) {
            throw new NotFoundException("не удалось обновить данные");
        }
    }

    protected long insert(String query, Object... params) { //добавление новой записи с возращением id записи
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new ValidationException("не удалось сохраанить данные");
        }
    }

}
