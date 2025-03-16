package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    @InjectMocks
    private FilmController filmController;

    @Mock
    private FilmService filmService;
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

        when(filmService.addFilm(any(Film.class))).thenReturn(film);

        ResponseEntity<Film> response = filmController.create(film);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Valid Film", response.getBody().getName());
    }

    @Test
    void shouldUpdateFilmSuccessfully() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Initial Film");
        film.setDescription("Initial description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);


        when(filmService.addFilm(any(Film.class))).thenReturn(film);
        when(filmService.getFilmById(1)).thenReturn(film);

        filmService.addFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);


        when(filmService.updateFilm(updatedFilm)).thenReturn(updatedFilm);

        Film responseFilm = filmService.updateFilm(updatedFilm);


        assertNotNull(responseFilm);
        assertEquals("Updated Film", responseFilm.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingFilmWithNonExistentId() {
        Film updatedFilm = new Film();
        updatedFilm.setId(999); // Неизвестный ID
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        when(filmStorage.getFilmById(updatedFilm.getId())).thenReturn(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.updateFilm(updatedFilm);
        });

        assertEquals("Фильм с ID 999 не найден.", exception.getMessage());
    }
}