package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        validateFilm(updatedFilm);
        return filmStorage.updateFilm(updatedFilm);
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.addLike(userId);
        }
    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            film.removeLike(userId);
        }
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikeCount).reversed())
                .limit(count)
                .toList();
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}