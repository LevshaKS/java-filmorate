package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage extends Storage<User> implements UserStorage<User> {

    @Override
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

    @Override
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

    @Override
    public Collection<Long> getFriendId(long id) {
        User getFriends = dataMap.get(id);
        logger.info("вывели список друзей");
          return getFriends.getFriendsId()
                .stream()
                .filter(aLong ->(dataMap.get(aLong).getFriendsId().contains(id)))
                .collect(Collectors.toList());
          }

    @Override
    public Collection<Long> setFriendId(long id, long friendsId) {
        User getFriends = dataMap.get(id); //добавление в друзья
        Set<Long> newFriendsList = getFriends.getFriendsId();
        newFriendsList.add(friendsId);
        getFriends.setFriendsId(newFriendsList);
        logger.info("добавили к себе в друзья");
       /* User getFriendsTo = dataMap.get(friendsId);    //зеркальное добавление в друзья
        Set<Long> newFriendsListTo = getFriendsTo.getFriendsId();
        newFriendsListTo.add(id);
        getFriendsTo.setFriendsId(newFriendsListTo);*/
        return newFriendsList;
    }

    @Override
    public Collection<Long> delFriendId(long id, long friendsId) {
        User getFriends = dataMap.get(id);  //удаление из друзей
        Set<Long> newFriendsList = getFriends.getFriendsId();
        newFriendsList.remove(friendsId);
        getFriends.setFriendsId(newFriendsList);
        logger.info("удалили у себя из друзей");
       /* User getFriendsTo = dataMap.get(friendsId);  //удаление из друзей
        Set<Long> newFriendsListTo = getFriendsTo.getFriendsId();
        newFriendsListTo.remove(id);
        getFriendsTo.setFriendsId(newFriendsListTo);*/

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