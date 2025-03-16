package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new ResourceNotFoundException("Фильм с ID " + id + " не найден.");
        }
        return film;
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        Film existingFilm = filmStorage.getFilmById(updatedFilm.getId());
        if (existingFilm == null) {
            throw new ResourceNotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден.");
        }
        validateFilm(updatedFilm);

        return filmStorage.updateFilm(updatedFilm);
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms());
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + filmId + " не найден.");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден.");
        }
        film.addLike(userId);
    }

    public ResponseEntity<Void> removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null || user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм или пользователь не найден.");
        }
        film.removeLike(userId);
        return ResponseEntity.ok().build();
    }

    public List<Film> getTopFilms(int count) {
        if (count <= 0) {
            return List.of();
        }
        return filmStorage.getAllFilms().stream().sorted(Comparator.comparingInt(Film::getLikeCount).reversed()).limit(count).toList();
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