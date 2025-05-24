package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaRowMapper.class})
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testGetMpaById() {

        Optional<Mpa> userOptional = Optional.of(mpaDbStorage.getId(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetMpaAll() {
        List<Mpa> mpaList = new ArrayList<>(mpaDbStorage.getAll());
        Assertions.assertEquals(mpaList.size(), 5, "Колличество записей должно совпадать");
        Assertions.assertEquals(mpaList.get(1), mpaDbStorage.getId(2),"возращается не правильное значение");
    }

}

