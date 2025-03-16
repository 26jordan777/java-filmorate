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

        Film responseFilm = filmService.updateFilm(updatedFilm);

        assertNotNull(responseFilm);
        assertEquals("Updated Film", responseFilm.getName());
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

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.updateFilm(updatedFilm);
        });

        assertEquals("Фильм с ID 999 не найден.", exception.getMessage());
    }
}