/*
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmServiceTest {

    @InjectMocks
    private FilmService filmService;

    @Mock
    private FilmStorage filmStorage;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateFilmSuccessfully() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Initial Film");
        film.setDescription("Initial description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        when(filmStorage.getFilmById(1)).thenReturn(film);

        when(filmStorage.updateFilm(any(Film.class))).thenReturn(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        Film responseFilm = filmStorage.updateFilm(updatedFilm);

        assertNotNull(responseFilm);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingFilmWithNonExistentId() {
        Film updatedFilm = new Film();
        updatedFilm.setId(999);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        when(filmStorage.getFilmById(updatedFilm.getId())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            filmService.updateFilm(updatedFilm);
        });

        assertEquals("Фильм с ID 999 не найден.", exception.getMessage());
    }

    @Test
    void shouldReturnPopularFilms() {
        Film film1 = new Film();
        film1.setId(1);
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(120);
        film1.addLike(1L);
        Film film2 = new Film();
        film2.setId(2);
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2023, 2, 1));
        film2.setDuration(90);
        film2.addLike(2L);

        when(filmStorage.getAllFilms()).thenReturn(List.of(film1, film2));

        List<Film> popularFilms = filmService.getTopFilms(2);

        assertEquals(2, popularFilms.size());
        assertEquals("Film 1", popularFilms.get(0).getName());
        assertEquals("Film 2", popularFilms.get(1).getName());
    }
}

 */