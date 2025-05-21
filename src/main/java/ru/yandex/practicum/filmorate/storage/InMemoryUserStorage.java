package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage extends Storage<User> implements UserStorage<User> {

    protected final Map<Long, Set<Long>> friendsMap = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(getNextId());

        dataMap.put(user.getId(), user);
        logger.info("Пользователь добавлен id: " + user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = dataMap.get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        logger.info("Запись пользователя обновлена");
        return oldUser;
    }

    @Override
    public Collection<Long> getFriendId(long id) {
        return friendsMap.get(id);
    }

    @Override
    public Collection<Long> setFriendId(long id, long friendsId) {
        if (!friendsMap.containsKey(id)) {
            Set<Long> newFriendList = new HashSet<>();
            newFriendList.add(friendsId);
            friendsMap.put(id, newFriendList);
        } else {
            friendsMap.get(id).add(friendsId);
        }
        return friendsMap.get(id);
    }

    @Override
    public Collection<Long> delFriendId(long id, long friendsId) {
        friendsMap.get(id).remove(friendsId);
        return friendsMap.get(id);
    }

    private long getNextId() {
        long currentMaxId = dataMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}