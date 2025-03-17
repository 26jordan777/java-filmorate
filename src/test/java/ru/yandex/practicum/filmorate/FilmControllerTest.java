package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    @InjectMocks
    private FilmController filmController;

    @Mock
    private FilmService filmService;

    @Mock
    private FilmService filmStorage;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateFilmSuccessfully() throws ValidationException {
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
    void shouldThrowExceptionWhenUpdatingFilmWithNonExistentId() throws ValidationException {
        Film updatedFilm = new Film();
        updatedFilm.setId(999);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        when(filmService.updateFilm(updatedFilm)).thenThrow(new ValidationException("Фильм с ID 999 не найден."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            filmController.update(updatedFilm);
        });
        assertEquals("Фильм с ID 999 не найден.", exception.getMessage());
    }

    @Test
    void shouldUpdateFilmSuccessfully() throws ValidationException {
        Film updatedFilm = new Film();
        updatedFilm.setId(999);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description.");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(130);

        when(filmStorage.getFilmById(updatedFilm.getId())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            filmController.update(updatedFilm);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Фильм с ID 999 не найден.", exception.getReason());
    }

    @Test
    void shouldReturnFilmById() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Valid Film");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        when(filmService.getFilmById(1)).thenReturn(film);

        ResponseEntity<Film> response = filmController.read(1);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Valid Film", response.getBody().getName());
    }
}