package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ErrorIsNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage<User> userStorage;
    private final ValidateController validateController;
    private final Logger logger = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public UserService(UserStorage<User> userStorage, ValidateController validateController) {
        this.userStorage = userStorage;
        this.validateController = validateController;
    }

    public Collection<Long> friendsAdd(long id, long friendsId) {
        getUserId(id);
        getUserId(friendsId);
        return userStorage.setFriendId(id, friendsId);

    }

    public Collection<Long> friendsDelete(long id, long friendsId) {
        getUserId(id);
        getUserId(friendsId);
        return userStorage.delFriendId(id, friendsId);
    }

    public Collection<User> friendsGetList(long id) {
        getUserId(id);

        return userStorage.getFriendId(id).stream().map(this::getUserId).collect(Collectors.toList());
    }

    public User create(User user) {
        validateController.validateUser(user);
        User returnUser = userStorage.create(user);
        logger.info("пользователь добавлен id: " + returnUser.getId());
        return returnUser;
    }

    public Collection<User> getAll() {
        logger.info("Вернули список");
        Collection<User> returnAll = userStorage.getAll();
        if (returnAll.isEmpty()) {
            throw new ErrorIsNull("список пуст");
        }
        return returnAll;
    }

    public User getUserId(long id) {
        User getUser = userStorage.getId(id);
        if (getUser == null) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("поиск по id=" + id);
        return getUser;
    }

    public void delete(long id) {
        User delUser = userStorage.delete(id);
        if (delUser == null) {
            throw new ErrorIsNull("нет такого id");
        }
        logger.info("удаление id=" + id);
    }

    public User update(User newUser) {

        if (newUser.getId() == null) {
            logger.warn("ID пустой");
            throw new ValidationException("ID пользователя пустой");
        }
        User oldUser = userStorage.getId(newUser.getId());

        if (oldUser == null) {
            logger.warn("пользователь с таким ID не найден");
            throw new ErrorIsNull("пользователь с таким ID не найден");
        }
        validateController.validateUser(newUser);
        logger.info("запись пользователя обновлена");
        return userStorage.update(newUser);
    }

    public List<User> friendsGetCommonList(long id, long otherId) {
        Collection<User> userList1 = friendsGetList(id);
        Collection<User> userList2 = friendsGetList(otherId);
        return userList1.stream().filter(userList2::contains).collect(Collectors.toList());
    }
}
