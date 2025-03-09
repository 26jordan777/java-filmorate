package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
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
                .sorted(Comparator.comparingInt(f -> -f.getLikeCount()))
                .limit(count)
                .toList();
    }
}