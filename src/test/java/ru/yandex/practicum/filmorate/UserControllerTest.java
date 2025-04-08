package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.ValidateController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class UserControllerTest {

    Exception exception;
    private final ValidateController validate = new ValidateController();
    User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "ya@yandex.ru", "login", "name", LocalDate.now().minusYears(3));
    }

    @Test
    public void UserNullEmail() {
        user.setEmail(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("email пустой", exception.getMessage());
    }

    @Test
    public void UserNullLogin() {
        user.setLogin(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("login пустой", exception.getMessage());
    }

    @Test
    public void UserNullName() {
        user.setName(null);
        validate.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName(), " имя не заполнено, должно присвоенно как login");
    }

    @Test
    public void UserNullBirthday() {
        user.setBirthday(null);
        exception = Assertions.assertThrows(ValidationNullException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("дата пустая", exception.getMessage());
    }

    @Test
    public void UserEmptyEmail() {
        user.setEmail("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("email пустой", exception.getMessage());
    }

    @Test
    public void UserEmptyLogin() {
        user.setLogin("");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("login пустой", exception.getMessage());
    }

    @Test
    public void UserOtherName() {
        user.setName("");
        validate.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName(), " имя не заполнено, должно присвоенно как login");
    }

    @Test
    public void UserNotEmail() {
        user.setEmail("yaya.ru");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("введен не email", exception.getMessage());
    }

    @Test
    public void UserWhitespaceLogin() {
        user.setLogin("login login");
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("присутствуют пробелы", exception.getMessage());
    }

    @Test
    public void UserFutureBirthday() {
        user.setBirthday(LocalDate.now().plusYears(3));
        exception = Assertions.assertThrows(ValidationException.class, () -> validate.validateUser(user));
        Assertions.assertEquals("введена дата из будущего", exception.getMessage());
    }

}
