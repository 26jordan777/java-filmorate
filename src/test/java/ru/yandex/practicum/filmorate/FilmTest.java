package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Exception exception = assertThrows(ValidationException.class, film::validate);
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooLong() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Exception exception = assertThrows(ValidationException.class, film::validate);
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsBefore1895() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        Exception exception = assertThrows(ValidationException.class, film::validate);
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZeroOrNegative() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        Exception exception = assertThrows(ValidationException.class, film::validate);
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void shouldPassValidationWithValidData() throws ValidationException {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        film.validate();
    }
}