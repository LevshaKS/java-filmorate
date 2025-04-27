package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class UserControllerTest {

    Exception exception;
    private final ValidateController validate = new ValidateController();
    User user;
    Set<Long> testList = new HashSet<>() {
    };

    @BeforeEach
    void setUp() {
        user = new User(1L, "ya@yandex.ru", "login", "name", LocalDate.now().minusYears(3), testList);
    }

    @Test
    public void userNullEmail() {
        user.setEmail(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("email пустой", exception.getMessage());
    }

    @Test
    public void userNullLogin() {
        user.setLogin(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("login пустой", exception.getMessage());
    }

    @Test
    public void userNullName() {
        user.setName(null);
        validate.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName(), " имя не заполнено, должно присвоенно как login");
    }

    @Test
    public void userNullBirthday() {
        user.setBirthday(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("дата пустая", exception.getMessage());
    }

    @Test
    public void userEmptyEmail() {
        user.setEmail("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("email пустой", exception.getMessage());
    }

    @Test
    public void userEmptyLogin() {
        user.setLogin("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("login пустой", exception.getMessage());
    }

    @Test
    public void userOtherName() {
        user.setName("");
        validate.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName(), " имя не заполнено, должно присвоенно как login");
    }

    @Test
    public void userNotEmail() {
        user.setEmail("yaya.ru");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("введен не email", exception.getMessage());
    }

    @Test
    public void userWhitespaceLogin() {
        user.setLogin("login login");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("присутствуют пробелы", exception.getMessage());
    }

    @Test
    public void userFutureBirthday() {
        user.setBirthday(LocalDate.now().plusYears(3));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("введена дата из будущего", exception.getMessage());
    }

    @Test
    public void userFriends() {
        User user1 = new User(1L, "ya1@yandex.ru", "login1", "name1", LocalDate.now().minusYears(3), testList);
        User user2 = new User(2L, "ya2@yandex.ru", "login2", "name2", LocalDate.now().minusYears(3), testList);
        user1.getFriendsId().add(2L);
        Assertions.assertEquals(2, user1.getFriendsId().stream().toList().get(0));
    }
}
