package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

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
    void shouldAddFilmSuccessfully() throws ValidationException {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        when(filmStorage.addFilm(any(Film.class))).thenReturn(film);

        Film addedFilm = filmService.addFilm(film);
        assertNotNull(addedFilm);
        assertEquals("Valid Film", addedFilm.getName());
        verify(filmStorage, times(1)).addFilm(film);
    }

    @Test
    void shouldThrowExceptionWhenFilmNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void shouldUpdateFilmSuccessfully() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Initial Film");
        film.setDescription("Initial description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        when(filmStorage.addFilm(any(Film.class))).thenReturn(film);
        when(filmStorage.getFilmById(1)).thenReturn(film);
        filmService.addFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        when(filmStorage.updateFilm(updatedFilm)).thenReturn(updatedFilm);

        Film responseFilm = filmService.updateFilm(updatedFilm);

        assertNotNull(responseFilm);
        assertEquals("Updated Film", responseFilm.getName());
    }
}