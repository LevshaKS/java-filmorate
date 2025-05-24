package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage<User> {
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    protected final Logger logger = LoggerFactory.getLogger(UserDbStorage.class);   //логгер

    @Override
    public User create(User user) { //создаем пользвателя
        String query = "INSERT INTO users(email, login, name, birthday) VALUES (?,?,?,?)";

        long id = insert(
                query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        logger.info("пользователь создан id: " + id);
        return user;
    }

    @Override
    public User update(User user) { //обновляем запись пользователя
        String query = "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE id=?";
        update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        logger.info("пользователь обновлен");
        return user;
    }

    @Override
    public User delete(long id) {   //удаляем пользователя по id
        User user = getId(id);
        String query = "DELETE FROM users WHERE id=?";
        boolean isDelete = delete(query, id);
        if (isDelete) {
            logger.info("пользователь удален id: " + id);
            return user;
        } else throw new NullPointerException("ошибка удаления пользователя");
    }

    @Override
    public User getId(long id) {    //получаем пользователя по id
        String query = "SELECT * FROM users WHERE id=?";
        Optional<User> user = findOne(query, id);
        if (user.isPresent()) {
            logger.info("вернули пользователя по id: " + id);
            return user.get();
        } else {
            throw new ErrorIsNull("нет такого пользователя");
        }
    }

    @Override
    public Collection<User> getAll() {  //получаем всех пользователей
        String query = "SELECT * FROM users";
        logger.info("вернули всех пользователей");
        return findMany(query);
    }

    @Override
    public Collection<Long> getFriendId(long id) {  //получаем список друзей по id пользователя
        String query = "SELECT to_user_id FROM friends WHERE user_id=" + id;
        logger.info("вернуть список друзей");
        return jdbc.queryForList(query, Long.class);
    }

    @Override
    public Collection<Long> setFriendId(long id, long userId) { //добавляем в сисок друзей
        Collection<Long> friendsList = getFriendId(id);
        if (friendsList.contains(userId)) {
            throw new ErrorIsNull("уже есть в друзьях");
        }
        String query = "INSERT INTO friends (user_id, to_user_id) VALUES (?,?)";
        int rowUpdated = jdbc.update(query, id, userId);
        if (rowUpdated > 0) {
            logger.info("добавлен в список друзей");
            return getFriendId(id);
        } else {
            throw new ErrorIsNull("ошибка добавления в друзья");
        }
    }

    @Override
    public Collection<Long> delFriendId(long id, long userId) { //удаляем из друзей

        String query = "DELETE FROM friends WHERE user_id=? AND to_user_id=?";
        int rowUpdated = jdbc.update(query, id, userId);
        if (rowUpdated > 0) {
            logger.info("удален из списока друзей");
            return getFriendId(id);
        } else {
            logger.info("в списке нет друга для удаления");
            return getFriendId(id);
        }
    }
}
