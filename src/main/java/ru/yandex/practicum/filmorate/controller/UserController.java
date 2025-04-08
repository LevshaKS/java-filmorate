package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ValidateController validate = new ValidateController();
    private final Map<Long, User> users = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping //запрос всех пользователей
    public ResponseEntity<Collection<User>> usersAll() {

        log.info("вывод списка пользователей");
        return ResponseEntity.ok(users.values());
    }

    @PostMapping //добавление нового пользователя
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        validate.validateUser(user);

        if (users.containsValue(user)) {
            log.warn("ошибка добавления пользователя, такой пользователь уже есть");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ошибка добавления пользователя, такой пользователь уже есть");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("пользователь создан id: " + user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user); //отрпавляем ответ с статусом и телом
    }

    @PutMapping //обновление пользователя
    public ResponseEntity<?> update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("ID пустой");
            throw new ValidationException(" ID пустой");
            // изначально сделал чтоб в теле была ошибка, но не прошло тесты в Postman
            //      return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID пустой");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("пользователь с таким ID не найден");
            throw new ValidationException("пользователь с таким ID не найден");
            // изначально сделал чтоб в теле была ошибка, но не прошло тесты в Postman
            //   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("пользователь с таким ID не найден");
        }
        validate.validateUser(newUser);
        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setName(newUser.getName());
        log.info("пользователь изменен");
        return ResponseEntity.ok(oldUser);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;

    }
}
