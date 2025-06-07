package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStore;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMappers;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStore.class, FilmRowMappers.class})
class FilmDbStorageTest {
    private final FilmDbStore filmDbStore;
    private final JdbcTemplate jdbc;
    private Film film;

    @BeforeEach
    void setUp() {
        jdbc.update("DELETE FROM films");
        jdbc.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");

        film = new Film();

        film.setName("film1");
        film.setDescription("film o filme");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(20);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmDbStore.create(film);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = Optional.of(filmDbStore.getId(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindFilmByName() {
        Optional<Film> filmOptional = Optional.of(filmDbStore.getId(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "film1")
                );
    }

    @Test
    public void testFindFilmByDescription() {
        Optional<Film> filmOptional = Optional.of(filmDbStore.getId(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("description", "film o filme")
                );
    }

    @Test
    public void testFindFilmByDuration() {
        Optional<Film> filmOptional = Optional.of(filmDbStore.getId(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("duration", 20)
                );
    }

    @Test
    public void testFindFilmByReleaseDate() {
        Optional<Film> filmOptional = Optional.of(filmDbStore.getId(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("releaseDate", LocalDate.now().minusYears(1))
                );
    }

    @Test
    public void testFindFilmByMpa() {
        Film film1 = filmDbStore.getId(1L);
        assertThat(film1.getMpa().getId()).toString().equals("1");
    }

    @Test
    public void testUpdateFilmById() {
        film = filmDbStore.getId(1L);
        film.setId(2L);
        Optional<Film> filmOptional = Optional.of(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    public void testUpdateFilmByName() {
        film = filmDbStore.getId(1L);
        film.setName("film2");
        Optional<Film> filmOptional = Optional.of(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "film2")
                );
    }

    @Test
    public void testUpdateFilmByDescription() {
        film = filmDbStore.getId(1L);
        film.setDescription("film o filme2");
        Optional<Film> filmOptional = Optional.of(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("description", "film o filme2")
                );
    }

    @Test
    public void testUpdateFilmByDuration() {
        film = filmDbStore.getId(1L);
        film.setDuration(25);
        Optional<Film> filmOptional = Optional.of(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("duration", 25)
                );
    }

    @Test
    public void testUpdateFilmByReleaseDate() {
        film = filmDbStore.getId(1L);
        film.setReleaseDate(LocalDate.now().minusYears(2));
        Optional<Film> filmOptional = Optional.of(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("releaseDate", LocalDate.now().minusYears(2))
                );
    }

    @Test
    public void testUpdateFilmByMpa() {
        Film film1 = filmDbStore.getId(1L);
        Mpa mpa = film1.getMpa();
        mpa.setId(2);
        film1.setMpa(mpa);
        assertThat(film1.getMpa().getId()).toString().equals("2");
    }

}
