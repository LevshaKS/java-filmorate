package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserId(@PathVariable @RequestBody long id){
        logger.info("вывод пользователя по ID");
        return  userService.getUserId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> userAll() {
        logger.info("вывод списка пользователей");
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        logger.info("Пользователь добавлен");
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody @PathVariable long id) {
        logger.info("Удаление id=" + id);
        userService.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody User newUser) {
        logger.info("запись пользователя обновлена");
        return userService.update(newUser);
    }

    @PutMapping ("/{id}/friends/{friendsId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long>  friendsAdd(@RequestBody @PathVariable long id,@PathVariable long  friendsId){
        logger.info("добавили в друзья");
        return userService.friendsAdd (id, friendsId);
    }

    @DeleteMapping ("/{id}/friends/{friendsId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long>  friendsDelete(@RequestBody @PathVariable long id,@PathVariable long  friendsId){
        logger.info("удалили из друзей");
        return userService.friendsDelete (id, friendsId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> friendsGetList (@RequestBody @PathVariable long id){
        logger.info("показывает список друзей");
        return userService.friendsGetList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Long> friendsGetCommonList (@PathVariable long id, @PathVariable long otherId){
        logger.info("показывает список друзей");
        return userService.friendsGetCommonList(id, otherId);
    }

}
