package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbc;
    private User user;

    @BeforeEach
    void setUp() {
        jdbc.update("DELETE FROM users");
        jdbc.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");

        user = new User();
        user.setEmail("test1@ya.ru");
        user.setLogin("login1");
        user.setName("name1");
        user.setBirthday(LocalDate.now().minusYears(1));
        userStorage.create(user);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.of(userStorage.getId(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserByName() {
        Optional<User> userOptional = Optional.of(userStorage.getId(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name1")
                );
    }

    @Test
    public void testFindUserByLogin() {
        Optional<User> userOptional = Optional.of(userStorage.getId(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login1")
                );
    }

    @Test
    public void testFindUserByBirthday() {
        Optional<User> userOptional = Optional.of(userStorage.getId(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.now().minusYears(1))
                );
    }

    @Test
    public void testUpdateUserById() {
        user = userStorage.getId(1);
        user.setId(2L);
        Optional<User> userOptional = Optional.of(user);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    public void testUpdateUserByName() {
        user = userStorage.getId(1);
        user.setName("name2");
        Optional<User> userOptional = Optional.of(user);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name2")
                );
    }

    @Test
    public void testUpdateUserByLogin() {
        user = userStorage.getId(1);
        user.setLogin("login2");
        Optional<User> userOptional = Optional.of(user);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login2")
                );
    }

    @Test
    public void testUpdateUserByBirthday() {
        user = userStorage.getId(1);
        user.setBirthday(LocalDate.now().minusYears(2));
        Optional<User> userOptional = Optional.of(user);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.now().minusYears(2))
                );
    }

}