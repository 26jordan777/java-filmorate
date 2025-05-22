package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import(GenreDbStorage.class)
public class GenreDbStorageTest {

    @Autowired
    private GenreDbStorage genreDbStorage;

    @BeforeEach
    public void setUp() {
        genreDbStorage.addGenre(new Genre("Комедия"));

    }

    @Test
    public void testAddGenre() {
        Genre genre = new Genre();
        genre.setName("Комедия");

        Genre addedGenre = genreDbStorage.addGenre(genre);

        assertThat(addedGenre).isNotNull();
        assertThat(addedGenre.getId()).isNotNull();
        assertThat(addedGenre.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreDbStorage.getAllGenres();
        assertThat(genres).isNotEmpty();
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreDbStorage.getGenreById(1);
        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isEqualTo(1);
    }
}