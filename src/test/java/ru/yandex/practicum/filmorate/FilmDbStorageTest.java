package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(FilmDbStorage.class)
public class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    @BeforeEach
    public void setUp() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(120);

        filmDbStorage.addFilm(film);
    }

    @Test
    public void testFindFilmById() {
        Film foundFilm = filmDbStorage.getFilmById(1);
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getName()).isEqualTo("Test Film");
    }

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("New Description");
        film.setReleaseDate(LocalDate.of(2023, 2, 1));
        film.setDuration(90);

        filmDbStorage.addFilm(film);
        List<Film> films = filmDbStorage.getAllFilms();

        assertThat(films).hasSize(2);
        assertThat(films.get(1).getName()).isEqualTo("New Film");
    }
}