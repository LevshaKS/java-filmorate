package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage extends Storage<User> implements UserStorage<User> {


    public User create(User user) {
        user.setId(getNextId());
        if (user.getFriendsId() == null) {
            user.setFriendsId(new HashSet<>() {
            });
        }
        dataMap.put(user.getId(), user);
        logger.info("Пользователь добавлен id: " + user.getId());
        return user;
    }


    public User update(User newUser) {
        User oldUser = dataMap.get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setFriendsId(newUser.getFriendsId());
        logger.info("Запись пользователя обновлена");
        return oldUser;
    }

    public Collection<Long> getFriendId(long id) {
        User getFriends = dataMap.get(id);
        return getFriends.getFriendsId();
    }

    public Collection<Long> setFriendId(long id, long friendsId) {
        User getFriends = dataMap.get(id); //добавление в друзья
        Set<Long> newFriendsList = getFriends.getFriendsId();
        newFriendsList.add(friendsId);
        getFriends.setFriendsId(newFriendsList);

        User getFriendsTo = dataMap.get(friendsId);    //зеркальное добавление в друзья
        Set<Long> newFriendsListTo = getFriendsTo.getFriendsId();
        newFriendsListTo.add(id);
        getFriendsTo.setFriendsId(newFriendsListTo);
        return newFriendsList;
    }

    public Collection<Long> delFriendId(long id, long friendsId) {
        User getFriends = dataMap.get(id);  //удаление из друзей
        Set<Long> newFriendsList = getFriends.getFriendsId();
        newFriendsList.remove(friendsId);
        getFriends.setFriendsId(newFriendsList);

        User getFriendsTo = dataMap.get(friendsId);  //удаление из друзей
        Set<Long> newFriendsListTo = getFriendsTo.getFriendsId();
        newFriendsListTo.remove(id);
        getFriendsTo.setFriendsId(newFriendsListTo);

        return newFriendsList;
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